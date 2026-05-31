from collections.abc import Generator

from fastapi import Depends, HTTPException, status
from sqlalchemy.orm import Session

from models.usuario import Usuario
from utils.database import SessionLocal
from utils.security import decode_access_token, get_bearer_token


def get_db() -> Generator[Session, None, None]:
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()


def get_current_user(
    token: str = Depends(get_bearer_token),
    db: Session = Depends(get_db),
) -> Usuario:
    # token is the raw JWT string
    data = decode_access_token(token)

    # prefer explicit claim 'id_usuario', fallback to subject ('sub')
    user_id = data.get("id_usuario") or data.get("sub")
    if not user_id:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Token inválido")

    try:
        user_id_int = int(user_id)
    except (TypeError, ValueError) as exc:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Token con formato inválido") from exc

    user = db.query(Usuario).filter(Usuario.id_usuario == user_id_int).first()
    if not user:
        raise HTTPException(status_code=status.HTTP_404_NOT_FOUND, detail="Usuario no encontrado")

    return user
