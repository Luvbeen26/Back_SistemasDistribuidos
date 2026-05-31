from __future__ import annotations

from datetime import datetime
from decimal import Decimal

from sqlalchemy import DateTime, ForeignKey, Integer, Numeric, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from utils.database import Base


class Movimiento(Base):
    __tablename__ = "movimiento"

    id_movimiento: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    id_cuenta: Mapped[int] = mapped_column(ForeignKey("cuenta_bancaria.id_cuenta"), nullable=False)
    id_tipo_movimiento: Mapped[int] = mapped_column(ForeignKey("tipo_movimiento.id_tipo"), nullable=False)
    fecha_hora: Mapped[datetime] = mapped_column(DateTime, nullable=False)
    importe: Mapped[Decimal] = mapped_column(Numeric(12, 2), nullable=False)
    concepto: Mapped[str | None] = mapped_column(String(100), nullable=True)
    numero_autorizacion: Mapped[str] = mapped_column(String(6), nullable=False)
    referencia_numerica: Mapped[str] = mapped_column(String(45), nullable=False)
    referencia_alfanumerica: Mapped[str] = mapped_column(String(45), nullable=False)
    clabe_interbancaria: Mapped[str | None] = mapped_column(String(18), nullable=True)
    estatus: Mapped[str] = mapped_column(String(20), nullable=False)

    cuenta_bancaria: Mapped["CuentaBancaria"] = relationship("CuentaBancaria", back_populates="movimientos")
    tipo_movimiento: Mapped["TipoMovimiento"] = relationship("TipoMovimiento", back_populates="movimientos")