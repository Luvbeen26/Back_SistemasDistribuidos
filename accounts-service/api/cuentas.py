from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from datetime import datetime, timedelta
from sqlalchemy import or_, desc
from schema.cuentas import ChangeStatus,Limitday, idSaves
from utils.dependencies import get_db, get_current_user
from utils.security import decode_access_token, get_bearer_token
from models.usuario import Usuario
from models.cliente import Cliente
from models.cuenta_bancaria import CuentaBancaria

from models.tarjetas_plastico import TarjetasPlastico


router = APIRouter(prefix="/api/cuentas", tags=["cuentas"])


@router.get("/me")
def read_me() -> dict[str, str]:
    return {"me": "hola"}


@router.get("/token")
def inspect_token(token: str = Depends(get_bearer_token)) -> dict:
    """Return decoded JWT payload for the supplied Bearer token."""
    payload = decode_access_token(token)
    return {"token_payload": payload}


@router.get("/getbyUser")
def get_by_user(
    db: Session = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
):
    cliente = db.query(Cliente).filter(Cliente.id_usuario == current_user.id_usuario).first()
    cuentas = cliente.cuentas_bancarias if cliente else []
    return [
        {
            "id_cuenta": cuenta.id_cuenta,
            "tipo_cuenta": cuenta.tipo_cuenta.descripcion,
            "numero_cuenta": cuenta.numero_cuenta,
            "saldo": str(cuenta.saldo),
            "estatus": cuenta.estatus,
            "clabe_interbancaria": cuenta.clabe_interbancaria,
        }
        for cuenta in cuentas
    ]


@router.get("/getbyUser/{id_cuenta}")
def get_account(
    id_cuenta: int,
    db: Session = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
):
    cuenta = db.query(CuentaBancaria).filter(CuentaBancaria.id_cuenta == id_cuenta).first()

    if cuenta is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cuenta no encontrada")

    if cuenta.cliente.id_cliente != current_user.id_cliente:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="No tienes permiso para acceder a esta cuenta")

    # Obtener últimos 100 movimientos ordenados por fecha descendente
   # movimientos = db.query(Movimiento).filter(
    #    Movimiento.id_cuenta == id_cuenta
    #).order_by(desc(Movimiento.fecha_hora)).limit(50).all()

    return {
        "id_cuenta": cuenta.id_cuenta,
        "tipo_cuenta": cuenta.tipo_cuenta.descripcion,
        "numero_cuenta": cuenta.numero_cuenta,
        "saldo": str(cuenta.saldo),
        "estatus": cuenta.estatus,
        "clabe_interbancaria": cuenta.clabe_interbancaria,
        "limite_diario": cuenta.limite,
     #   "movimientos": [
      #      {
        #        "id_movimiento": movimiento.id_movimiento,
       #         "tipo_movimiento": movimiento.tipo_movimiento.descripcion,
         #       "concepto": movimiento.concepto,
          #      "monto": str(movimiento.importe),
           #     "fecha": movimiento.fecha_hora.strftime("%Y-%m-%d %H:%M:%S"),
            #    "accion" : movimiento.tipo_movimiento.action_sum_rest,
            #}
            #for movimiento in movimientos
        #],
    }




@router.patch("/change_status/{id_cuenta}")
def change_status(
    id_cuenta: int,
    change_status: ChangeStatus,
    db: Session = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
):
    cuenta = db.query(CuentaBancaria).filter(CuentaBancaria.id_cuenta == id_cuenta).first()

    if cuenta is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cuenta no encontrada")

    if cuenta.cliente.id_cliente != current_user.id_cliente:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="No tienes permiso para acceder a esta cuenta")

    #SE PASA A PARA ACTIVA Y I PARA DESACTIVA, SI ES INDEFINIDO SE BLOQUEA HASTA QUE SE CAMBIE A ACTIVA
    cuenta.estatus = change_status.estatus
    if cuenta.estatus == "I" and change_status.indefinido:
        cuenta.bloq_hasta = None  # Bloqueo indefinido
    elif cuenta.estatus == "I" and change_status.horas_bloqueo is not None:
        cuenta.bloq_hasta = datetime.now() + timedelta(hours=change_status.horas_bloqueo)  # Bloqueo temporal
    if cuenta.estatus == "A":
        cuenta.bloq_hasta = None  # Desbloquear si se activa
    db.commit()
    
    return {
        "msg" : f"Cuenta {id_cuenta} actualizada a estatus {cuenta.estatus}",
    }


@router.patch("/limit_day/{id_cuenta}")
def change_status(
    id_cuenta: int,
    limit: Limitday,
    db: Session = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
):
    cuenta = db.query(CuentaBancaria).filter(CuentaBancaria.id_cuenta == id_cuenta).first()

    if cuenta is None:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Cuenta no encontrada")

    if cuenta.cliente.id_cliente != current_user.id_cliente:
        raise HTTPException(status_code=status.HTTP_403_FORBIDDEN, detail="No tienes permiso para acceder a esta cuenta")

    cuenta.limite = limit.limite_diario
    db.commit()
    
    return {
        "msg" : f"Cuenta {id_cuenta} actualizada a limite {cuenta.limite}",
    }


@router.get("/get_limit_day/{id_cuenta}")
def get_limit(
    id_cuenta: int,
    db: Session = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
):
    cuenta = db.get(CuentaBancaria, id_cuenta)

    if cuenta is None:
        raise HTTPException(status_code=404, detail="Cuenta no encontrada")

    if cuenta.cliente.id_cliente != current_user.id_cliente:
        raise HTTPException(status_code=403, detail="No tienes permiso para acceder a esta cuenta")

    return {"limite_diario": cuenta.limite}



@router.get("/check_num_account/{numero_cuenta}")
def get_account(
    numero_cuenta: str,
    db: Session = Depends(get_db),
  #  current_user: Usuario = Depends(get_current_user),
):
    # Buscar por número de cuenta o CLABE, tomando la más reciente con estatus "A"
    cuenta = db.query(CuentaBancaria).filter(
        or_(
            CuentaBancaria.numero_cuenta == numero_cuenta,
            CuentaBancaria.clabe_interbancaria == numero_cuenta,
        ),
        CuentaBancaria.estatus == "A",
    ).order_by(desc(CuentaBancaria.fecha_apertura)).first()

    if cuenta:
        tarjeta_activa = next(
            (t for t in cuenta.tarjetas_plastico if t.estatus == "activa"),
            None
        )
        return {
            "tipo": "C",
            "id_cuenta": cuenta.id_cuenta,
            "tipo_cuenta": cuenta.tipo_cuenta.descripcion,
            "destinatario": get_nombre_completo(cuenta.cliente),
            "id_tipo_cuenta": cuenta.id_tipo_cuenta,
            "numero_cuenta": cuenta.numero_cuenta,
            "clabe_interbancaria": cuenta.clabe_interbancaria,
            "estatus": cuenta.estatus,
            "fecha_apertura": cuenta.fecha_apertura,
            "id_tarjeta" : tarjeta_activa.id_tarjeta if tarjeta_activa else None,
            "tarjeta": tarjeta_activa.numero_tarjeta if tarjeta_activa else None,
        }

    # Fallback: buscar por número de tarjeta
    tarjeta = db.query(TarjetasPlastico).filter(
        TarjetasPlastico.numero_tarjeta == numero_cuenta,
        TarjetasPlastico.estatus == "A",
    ).first()

    if tarjeta:
        cuenta_de_tarjeta = tarjeta.cuenta_bancaria
        return {
            "tipo": "T",
            "id_cuenta": cuenta_de_tarjeta.id_cuenta,
            "tipo_cuenta": cuenta_de_tarjeta.tipo_cuenta.descripcion,
            "id_tipo_cuenta": cuenta_de_tarjeta.id_tipo_cuenta,
            "destinatario": get_nombre_completo(cuenta_de_tarjeta.cliente),
            "numero_cuenta": cuenta_de_tarjeta.numero_cuenta,
            "clabe_interbancaria": cuenta_de_tarjeta.clabe_interbancaria,
            "estatus": cuenta_de_tarjeta.estatus,
            "fecha_apertura": cuenta_de_tarjeta.fecha_apertura,
            "id_tarjeta" : tarjeta.id_tarjeta,
            "tarjeta": tarjeta.numero_tarjeta,
        }

    raise HTTPException(status_code=404, detail="Cuenta no encontrada")




@router.post("/infoSaves")
def get_info_saves(
    saves: list[idSaves],
    db: Session = Depends(get_db),
    current_user: Usuario = Depends(get_current_user),
):
    resultados = []

    for save in saves:
        if save.tipo == "C":
            cuenta = db.query(CuentaBancaria).filter(
                CuentaBancaria.id_cuenta == save.id_destino,
                CuentaBancaria.estatus == "A",
            ).first()

            if not cuenta:
                continue

            tarjeta_activa = next(
                (t for t in cuenta.tarjetas_plastico if t.estatus == "A"),
                None
            )
            resultados.append({
                "tipo": "C",
                "destinatario": get_nombre_completo(cuenta.cliente),
                "numero": cuenta.numero_cuenta,
                #"tarjeta": tarjeta_activa.numero_tarjeta if tarjeta_activa else None,
            })

        elif save.tipo == "T":
            tarjeta = db.query(TarjetasPlastico).filter(
                TarjetasPlastico.id_tarjeta == save.id_destino,
                TarjetasPlastico.estatus == "A",
            ).first()

            if not tarjeta:
                continue

            cuenta_de_tarjeta = tarjeta.cuenta_bancaria
            resultados.append({
                #"id_save": save.id_save,
                "tipo": "T",
               # "id_cuenta": cuenta_de_tarjeta.id_cuenta,
                #"tipo_cuenta": cuenta_de_tarjeta.tipo_cuenta.descripcion,
                "destinatario": get_nombre_completo(cuenta_de_tarjeta.cliente),
                "numero": tarjeta.numero_tarjeta,
                #"clabe_interbancaria": cuenta_de_tarjeta.clabe_interbancaria,
                #"tarjeta": tarjeta.numero_tarjeta,
            })

    return resultados


def get_nombre_completo(cliente) -> str:
    partes = [
        cliente.nombre,
        cliente.apellido_1,
        cliente.apellido_2,  # puede ser None
    ]
    return " ".join(p for p in partes if p)


