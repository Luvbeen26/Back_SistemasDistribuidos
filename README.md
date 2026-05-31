# Microservices Banking

Este repositorio queda organizado como un monorepo con tres servicios:

```
Microservices/
├── API-Gateway/
├── auth-service/
└── bank-core-service/
```

## Responsabilidad de cada servicio

### `API-Gateway/`
Entrada única para el frontend. Aquí irían reglas de ruteo, CORS centralizado, rate limiting y, si se desea, validación ligera de token antes de reenviar.

### `auth-service/`
Autenticación y registro:
- login
- registro de usuario
- emisión de JWT
- validación de credenciales

### `bank-core-service/`
Lógica bancaria principal:
- clientes
- cuentas
- movimientos
- tarjetas
- catálogos y consultas operativas

## Estructura objetivo

```text
Microservices/
├── API-Gateway/
│   └── README.md
├── auth-service/
│   └── README.md
└── bank-core-service/
	└── README.md
```

## Mapeo sugerido de código actual

- `AuthServlet`, `JwtFilter`, `RegistrationService` -> `auth-service`
- `ClienteServlet`, `CuentaServlet`, `MovimientoServlet`, DAOs y modelos de negocio -> `bank-core-service`
- `CorsFilter` -> `API-Gateway` o `auth-service` si el gateway no se implementa todavía

## Siguiente paso

El siguiente paso es mover el código a proyectos Maven separados para que cada servicio compile y despliegue por su cuenta.
