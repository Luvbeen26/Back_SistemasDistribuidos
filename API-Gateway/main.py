import os
from jose import jwt, JWTError, ExpiredSignatureError
import httpx
from fastapi import FastAPI, HTTPException, Request, Response
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv
from fastapi.responses import JSONResponse 

load_dotenv()


AUTH_SERVICE_URL = os.getenv("AUTH_SERVICE_URL", "http://localhost:8080")
ACCOUNTS_SERVICE_URL = os.getenv("ACCOUNTS_SERVICE_URL", "http://localhost:8082")
TRANSFER_SERVICE_URL = os.getenv("TRANSFER_SERVICE_URL", "http://localhost:8083")
SECRET_KEY = os.getenv("SECRET_KEY")

RUTAS_PUBLICAS = {"/api/auth/login", "/api/auth/register", "/", "/health"}


app = FastAPI(title="API Gateway", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)


@app.middleware("http")
async def jwt_middleware(request: Request, call_next):
    if request.method == "OPTIONS":
        return await call_next(request)

    # Permitir acceso a rutas públicas sin autenticación (los de auth)
    if request.url.path in RUTAS_PUBLICAS:
        return await call_next(request)

    auth_header = request.headers.get("Authorization", "")
    if not auth_header.startswith("Bearer "):
        return JSONResponse(status_code=401, content={"detail": "Token no proporcionado"})

    token = auth_header.split(" ", 1)[1].strip()
    try:
        jwt.decode(token, key=SECRET_KEY, algorithms=["HS256"])
    except ExpiredSignatureError:
        return JSONResponse(status_code=401, content={"detail": "Token expirado"})
    except JWTError:
        return JSONResponse(status_code=401, content={"detail": "Token inválido"})

    return await call_next(request)

HOP_BY_HOP_HEADERS = {
    "connection",
    "keep-alive",
    "proxy-authenticate",
    "proxy-authorization",
    "te",
    "trailer",
    "transfer-encoding",
    "upgrade",
    "host",
    "content-length",
}


def _filtered_headers(headers: dict[str, str]) -> dict[str, str]:
    return {
        key: value
        for key, value in headers.items()
        if key.lower() not in HOP_BY_HOP_HEADERS
    }


async def _proxy_request(request: Request, base_url: str, target_path: str) -> Response:
    url = f"{base_url.rstrip('/')}/{target_path.lstrip('/')}"
    body = await request.body()
    headers = _filtered_headers(dict(request.headers))

    async with httpx.AsyncClient(follow_redirects=False, timeout=30.0) as client:
        try:
            upstream = await client.request(
                method=request.method,
                url=url,
                params=dict(request.query_params),
                content=body if body else None,
                headers=headers,
            )
        except httpx.RequestError as exc:
            raise HTTPException(status_code=502, detail=f"Error conectando con el servicio destino: {exc}") from exc

    response_headers = _filtered_headers(dict(upstream.headers))
    return Response(
        content=upstream.content,
        status_code=upstream.status_code,
        headers=response_headers,
        media_type=upstream.headers.get("content-type"),
    )


@app.get("/")
async def root() -> dict[str, str]:
    return {
        "message": "API Gateway running",
        "auth_service": AUTH_SERVICE_URL,
        "accounts_service": ACCOUNTS_SERVICE_URL,
        "transfer_service": TRANSFER_SERVICE_URL,
    }


@app.get("/health")
async def health() -> dict[str, str]:
    return {"status": "ok"}

@app.api_route("/api/auth/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_auth(path: str, request: Request):
    return await _proxy_request(request, AUTH_SERVICE_URL, f"api/auth/{path}")


@app.api_route("/api/cuentas/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_cuentas(path: str, request: Request):
    return await _proxy_request(request, ACCOUNTS_SERVICE_URL, f"api/cuentas/{path}")


@app.api_route("/api/movimientos/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_movimientos(path: str, request: Request):
    return await _proxy_request(request, TRANSFER_SERVICE_URL, f"api/movimientos/{path}")