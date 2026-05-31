from fastapi import APIRouter

from api import cuentas


api_router = APIRouter()
api_router.include_router(cuentas.router)
