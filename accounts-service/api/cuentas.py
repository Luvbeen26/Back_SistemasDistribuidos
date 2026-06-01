from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from sqlalchemy import desc
from datetime import datetime, timedelta

from schema.cuentas import ChangeStatus,Limitday
from utils.dependencies import get_db, get_current_user
from utils.security import decode_access_token, get_bearer_token
from models.usuario import Usuario
from models.cliente import Cliente
from models.cuenta_bancaria import CuentaBancaria
from models.movimiento import Movimiento


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