import datetime
from zoneinfo import ZoneInfo

from fastapi import HTTPException, Header, Depends
from fastapi.security import HTTPBearer, HTTPAuthorizationCredentials
from passlib.context import CryptContext
from sqlalchemy.orm import Session
from jose import jwt, JWTError, ExpiredSignatureError
from models.usuario import Usuario
from config import settings


access_key = settings.JWT_ACCESS_KEY
pwd_context = CryptContext(schemes=["bcrypt"], deprecated="auto")
MAZATLAN_TZ = ZoneInfo("America/Mazatlan")


def check_email(db: Session, correo: str):
    return db.query(Usuario).filter(Usuario.correo == correo).first()


def check_user(db: Session, user: str):
    return db.query(Usuario).filter(Usuario.nombre_usuario == user).first()


http_bearer = HTTPBearer(auto_error=False)


def _normalize_token(token: str) -> str:
    token = token.strip()
    if token.lower().startswith("bearer "):
        return token.split(None, 1)[1].strip()
    return token


def get_bearer_token(
    credentials: HTTPAuthorizationCredentials | None = Depends(http_bearer),
) -> str:

    if not credentials or not credentials.credentials:
        raise HTTPException(status_code=401, detail="Token no proporcionado")
    return _normalize_token(credentials.credentials)


def decode_access_token(token: str) -> dict:
    token = _normalize_token(token)
    if not token:
        raise HTTPException(status_code=401, detail="Token no proporcionado")
    try:
        payload = jwt.decode(token, key=access_key, algorithms=["HS256"])
        return payload
    except ExpiredSignatureError:
        raise HTTPException(status_code=401, detail="Token expirado")
    except JWTError:
        raise HTTPException(status_code=401, detail="Token inválido o firmado con otra clave")

