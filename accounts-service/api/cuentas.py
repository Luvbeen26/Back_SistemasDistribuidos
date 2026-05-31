from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session

from utils.dependencies import get_db, get_current_user
from models.usuario import Usuario
from models.cliente import Cliente
from models.cuenta_bancaria import CuentaBancaria


router = APIRouter(prefix="/api/cuentas", tags=["cuentas"])


@router.get("/me")
def read_me() -> dict[str, str]:
    return {"me": "hola"}


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
    

