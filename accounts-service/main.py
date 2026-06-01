from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware

from routers import api_router
from utils.database import Base, engine


import models.cliente  
import models.cuenta_bancaria  
import models.tarjetas_plastico 
import models.tipo_cuenta 
import models.usuario 


app = FastAPI(title="Accounts Service")

app.add_middleware(
    CORSMiddleware,
    allow_origins=[
        "http://localhost:5173",
        "http://127.0.0.1:5173",
        "http://localhost:3000",
        "http://127.0.0.1:3000",
        "http://localhost:4200"
    ],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.on_event("startup")
def on_startup() -> None:
    Base.metadata.create_all(bind=engine)


app.include_router(api_router)
