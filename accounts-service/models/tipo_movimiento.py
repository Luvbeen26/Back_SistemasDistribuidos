from __future__ import annotations

from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column, relationship

from utils.database import Base


class TipoMovimiento(Base):
    __tablename__ = "tipo_movimiento"

    id_tipo: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    descripcion: Mapped[str] = mapped_column(String(50), nullable=False)
    action_sum_rest: Mapped[str | None] = mapped_column(String(1), nullable=True)

    movimientos: Mapped[list["Movimiento"]] = relationship("Movimiento", back_populates="tipo_movimiento")