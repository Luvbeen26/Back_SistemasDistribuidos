# API-Gateway

Puerta de entrada del frontend hacia los microservicios.

Responsabilidades previstas:
- CORS centralizado
- ruteo hacia `auth-service` y `bank-core-service`
- políticas de seguridad ligeras
- balanceo o proxy reverso si se desea

Este directorio se convertirá en el proyecto independiente del gateway.