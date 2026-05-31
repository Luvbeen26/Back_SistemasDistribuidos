import os

import httpx
from fastapi import FastAPI, HTTPException, Request, Response
from fastapi.middleware.cors import CORSMiddleware
from dotenv import load_dotenv


load_dotenv()


AUTH_SERVICE_URL = os.getenv("AUTH_SERVICE_URL", "http://localhost:8080/banksoft")
BANK_CORE_SERVICE_URL = os.getenv("BANK_CORE_SERVICE_URL", "http://localhost:8081")


app = FastAPI(title="API Gateway", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=False,
    allow_methods=["*"],
    allow_headers=["*"],
)


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
        "bank_core_service": BANK_CORE_SERVICE_URL,
    }


@app.get("/health")
async def health() -> dict[str, str]:
    return {"status": "ok"}


@app.api_route("/api/auth/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_auth(path: str, request: Request):
    return await _proxy_request(request, AUTH_SERVICE_URL, f"api/auth/{path}")


@app.api_route("/banksoft/api/auth/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_auth_banksoft(path: str, request: Request):
    return await _proxy_request(request, AUTH_SERVICE_URL, f"api/auth/{path}")


@app.api_route("/api/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_bank(path: str, request: Request):
    if path.startswith("auth/"):
        return await _proxy_request(request, AUTH_SERVICE_URL, f"api/{path}")
    return await _proxy_request(request, BANK_CORE_SERVICE_URL, f"api/{path}")


@app.api_route("/banksoft/api/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_bank_banksoft(path: str, request: Request):
    if path.startswith("auth/"):
        return await _proxy_request(request, AUTH_SERVICE_URL, f"api/{path}")
    return await _proxy_request(request, BANK_CORE_SERVICE_URL, f"api/{path}")


@app.api_route("/auth/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_auth_legacy(path: str, request: Request):
    return await _proxy_request(request, AUTH_SERVICE_URL, f"api/auth/{path}")


@app.api_route("/bank/{path:path}", methods=["GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"])
async def proxy_bank_legacy(path: str, request: Request):
    return await _proxy_request(request, BANK_CORE_SERVICE_URL, f"api/{path}")