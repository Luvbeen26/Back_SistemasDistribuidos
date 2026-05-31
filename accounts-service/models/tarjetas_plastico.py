from __future__ import annotations

from datetime import date

from sqlalchemy import Date, ForeignKey, Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from utils.database import Base


class TarjetasPlastico(Base):
    __tablename__ = "tarjetas_plastico"

    id_tarjeta: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    id_cuenta: Mapped[int] = mapped_column(ForeignKey("cuenta_bancaria.id_cuenta"), nullable=False)
    numero_tarjeta: Mapped[str] = mapped_column(String(16), nullable=False)
    mes_expira: Mapped[int] = mapped_column(Integer, nullable=False)
    anio_expira: Mapped[int] = mapped_column(Integer, nullable=False)
    cvc: Mapped[str] = mapped_column(String(3), nullable=False)
    fecha_emision: Mapped[date] = mapped_column(Date, nullable=False)
    estatus: Mapped[str] = mapped_column(String(20), nullable=False, default="activa")
    nip: Mapped[str | None] = mapped_column(String(4), nullable=True)

    cuenta_bancaria: Mapped["CuentaBancaria"] = relationship("CuentaBancaria", back_populates="tarjetas_plastico")