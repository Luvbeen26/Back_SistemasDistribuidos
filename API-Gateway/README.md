# API-Gateway

Gateway en FastAPI para enrutar peticiones hacia los microservicios del banco.

## Qué hace

- Recibe llamadas del frontend
- Aplica CORS para cualquier origen
- Reenvía rutas de autenticación a `auth-service`
- Reenvía rutas bancarias a `bank-core-service`
- Expone `/health` para verificación rápida

## Variables de entorno

- `AUTH_SERVICE_URL` por defecto `http://localhost:8081`
- `BANK_CORE_SERVICE_URL` por defecto `http://localhost:8082`

El archivo `.env` se carga automáticamente al iniciar `main.py`.

## Rutas

- `GET /health`
- `GET /`
- `ANY /api/auth/{path}` -> `AUTH_SERVICE_URL/api/auth/{path}`
- `ANY /api/{path}` -> `BANK_CORE_SERVICE_URL/api/{path}`
- `ANY /auth/{path}` -> compatibilidad directa con auth
- `ANY /bank/{path}` -> compatibilidad directa con core
- `ANY /banksoft/api/auth/{path}` -> compatibilidad con contexto `banksoft`
- `ANY /banksoft/api/{path}` -> compatibilidad con contexto `banksoft`

## Ejecutar

```bash
cd API-Gateway
python -m venv .venv
.venv\Scripts\activate
pip install -r requirements.txt
uvicorn main:app --reload --port 8000
```

## Ejemplo

Si `auth-service` corre en `http://localhost:8080`:

```bash
curl -X POST http://localhost:8000/banksoft/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"nombreUsuario\":\"luvbeen\",\"contrasena\":\"123\"}"
```
