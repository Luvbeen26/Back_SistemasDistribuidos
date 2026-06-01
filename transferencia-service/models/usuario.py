from sqlalchemy import Integer, String
from sqlalchemy.orm import Mapped, mapped_column

from utils.database import Base


class Usuario(Base):
    __tablename__ = "usuario"

    id_usuario: Mapped[int] = mapped_column(Integer, primary_key=True, index=True)
    id_cliente: Mapped[int | None] = mapped_column(Integer, nullable=True)
    nombre_usuario: Mapped[str | None] = mapped_column(String(30), nullable=True)
    contrasena: Mapped[str | None] = mapped_column(String(200), nullable=True)
    estatus: Mapped[str | None] = mapped_column(String(1), nullable=True)
    correo: Mapped[str | None] = mapped_column(String(200), nullable=True)