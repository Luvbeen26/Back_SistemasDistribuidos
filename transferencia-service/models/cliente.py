from __future__ import annotations

from datetime import date

from sqlalchemy import Date, Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from utils.database import Base


class Cliente(Base):
    __tablename__ = "cliente"

    id_cliente: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    nombre: Mapped[str] = mapped_column(String(50), nullable=False)
    apellido_1: Mapped[str | None] = mapped_column(String(20), nullable=True)
    apellido_2: Mapped[str | None] = mapped_column(String(20), nullable=True)
    rfc: Mapped[str | None] = mapped_column(String(13), nullable=True)
    fecha_alta: Mapped[date] = mapped_column(Date, nullable=False)
    fecha_nacimiento: Mapped[date | None] = mapped_column(Date, nullable=True)
    curp: Mapped[str | None] = mapped_column(String(18), nullable=True)
    email: Mapped[str] = mapped_column(String(100), nullable=False)
    telefono: Mapped[str | None] = mapped_column(String(15), nullable=True)
    numero_exterior: Mapped[str | None] = mapped_column(String(45), nullable=True)
    numero_interior: Mapped[str | None] = mapped_column(String(45), nullable=True)
    calle: Mapped[str] = mapped_column(String(45), nullable=False)
    id_usuario: Mapped[int | None] = mapped_column(Integer, nullable=True)
    estatus: Mapped[str] = mapped_column(String(1), nullable=False)

    cuentas_bancarias: Mapped[list["CuentaBancaria"]] = relationship(
        "CuentaBancaria", back_populates="cliente", cascade="all, delete-orphan"
    )