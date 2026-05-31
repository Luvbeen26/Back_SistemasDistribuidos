from __future__ import annotations

from decimal import Decimal

from sqlalchemy import Integer, Numeric, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from utils.database import Base


class TipoCuenta(Base):
    __tablename__ = "tipo_cuenta"

    id_tipo_cuenta: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    descripcion: Mapped[str] = mapped_column(String(50), nullable=False)
    estatus: Mapped[str] = mapped_column(String(1), nullable=False)
    tasa_interes: Mapped[Decimal | None] = mapped_column(Numeric(5, 2), nullable=True)

    cuentas_bancarias: Mapped[list["CuentaBancaria"]] = relationship(
        "CuentaBancaria", back_populates="tipo_cuenta"
    )