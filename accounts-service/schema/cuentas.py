from __future__ import annotations
from datetime import datetime
from pydantic import BaseModel, ConfigDict


class MovimientoOut(BaseModel):
	id_movimiento: int
	tipo_movimiento: str | None = None
	concepto: str | None = None
	monto: str
	fecha: datetime


class CuentaOut(BaseModel):
	id_cuenta: int
	tipo_cuenta: str | None = None
	numero_cuenta: str
	saldo: str
	estatus: str | None = None
	clabe_interbancaria: str | None = None
	movimientos: list[MovimientoOut] = []

	model_config = ConfigDict(from_attributes=True)


class CuentaResumenOut(BaseModel):
	id_cuenta: int
	tipo_cuenta: str | None = None
	numero_cuenta: str
	saldo: str
	estatus: str | None = None
	clabe_interbancaria: str | None = None

	model_config = ConfigDict(from_attributes=True)


class ChangeStatus(BaseModel):
    estatus: str
    indefinido: bool | None = None
    horas_bloqueo: int | None = None

    model_config = ConfigDict(from_attributes=True)


class Limitday (BaseModel):
    limite_diario: int

    model_config = ConfigDict(from_attributes=True)

class idSaves(BaseModel):
	id_destino: int
	tipo: str

	model_config = ConfigDict(from_attributes=True)