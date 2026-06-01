from __future__ import annotations

from datetime import date, datetime
from decimal import Decimal

from sqlalchemy import Date, DateTime, ForeignKey, Integer, Numeric, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from utils.database import Base


class CuentaBancaria(Base):
    __tablename__ = "cuenta_bancaria"

    id_cuenta: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    id_cliente: Mapped[int | None] = mapped_column(ForeignKey("cliente.id_cliente"), nullable=True)
    numero_cuenta: Mapped[str | None] = mapped_column(String(16), nullable=True)
    id_tipo_cuenta: Mapped[int] = mapped_column(ForeignKey("tipo_cuenta.id_tipo_cuenta"), nullable=False)
    saldo: Mapped[Decimal] = mapped_column(Numeric(10, 2), nullable=False, default=Decimal("0.00"))
    limite: Mapped[Decimal | None] = mapped_column(Numeric(10, 2), nullable=True)
    fecha_apertura: Mapped[date] = mapped_column(Date, nullable=False)
    estatus: Mapped[str] = mapped_column(String(1), nullable=False)
    clabe_interbancaria: Mapped[str | None] = mapped_column(String(45), nullable=True)
    es_mostrada: Mapped[int | None] = mapped_column(Integer, nullable=True)
    bloq_hasta: Mapped[datetime | None] = mapped_column(DateTime, nullable=True)

    cliente: Mapped["Cliente | None"] = relationship("Cliente", back_populates="cuentas_bancarias")
    tipo_cuenta: Mapped["TipoCuenta"] = relationship("TipoCuenta", back_populates="cuentas_bancarias")
    movimientos: Mapped[list["Movimiento"]] = relationship(
        "Movimiento", back_populates="cuenta_bancaria", cascade="all, delete-orphan"
    )
    tarjetas_plastico: Mapped[list["TarjetasPlastico"]] = relationship(
        "TarjetasPlastico", back_populates="cuenta_bancaria", cascade="all, delete-orphan"
    )
