# auth-service

Servicio backend basado en Java Servlets, Maven, Hibernate y JWT.

## Requisitos

- Java 21
- Maven 3.9+
- Apache Tomcat 11
- PostgreSQL accesible desde la aplicación

## Estructura del despliegue (actualizada)

Este módulo se empaqueta como WAR. Por configuración del proyecto el artefacto final ahora se genera como `target/ROOT.war` (ver `pom.xml` `<finalName>`).

La URL base cuando se despliega `ROOT.war` es:

`http://localhost:8080`

> Nota: si existe un despliegue viejo `banksoft.war` o una carpeta `banksoft/` en `webapps`, elimínalos para evitar respuestas obsoletas.

## Compilación

Desde la carpeta `auth-service`:

```bash
mvn clean package
```

## Despliegue en Tomcat

Opción manual: copiar el WAR generado como `ROOT.war` a Tomcat `webapps`:

```powershell
copy target\ROOT.war "C:\Program Files\Apache Software Foundation\Tomcat 11.0\webapps\"
```

Luego reinicia Tomcat si está en ejecución, o deja que el contenedor despliegue el WAR al iniciar.

Si migras desde el comportamiento anterior (context path `banksoft`), borra `banksoft.war` y la carpeta `banksoft/` en `webapps` antes de copiar `ROOT.war`.

## Endpoints

### Auth

- `POST /api/auth/login`  → devuelve `AuthResponse` con `token`, `nombreUsuario` y `idUsuario`.
- `POST /api/auth/register` → ahora devuelve la misma `AuthResponse` (token JWT) al registrar un usuario.

El token está firmado con HS256; conserva la `sub` (id del usuario) y otros claims como `nombreUsuario`.

## Variables de entorno y configuración relevantes

- `JWT_SECRET` / `SECRET_KEY` (en el proyecto se usa `SECRET_KEY` como valor legado; asegúrate de que tenga al menos 32 bytes para HS256).
- `ALGORITHM` (debe ser `HS256` para compatibilidad con los servicios Python del gateway).

Archivos importantes para revisar antes de desplegar:

- `src/main/resources/hibernate.cfg.xml`: conexión a PostgreSQL, usuario y contraseña
- `src/main/resources/application.properties`: propiedades externas
- `src/main/java/banksoft/filter/JwtFilter.java` y `src/main/java/banksoft/util/JwtUtil.java`: configuración y firma/verificación del JWT

## Integración con el API Gateway

En el `API-Gateway` la variable debe apuntar al host donde corre Tomcat:

- `AUTH_SERVICE_URL=http://localhost:8080`

Y el `ACCOUNTS_SERVICE_URL` suele ser `http://localhost:8082` (ajusta según tu despliegue).

## Uso y verificación rápida

1. Ejecuta `mvn clean package`
2. Copia `target/ROOT.war` a `webapps` (elimina `banksoft.war`/`banksoft/` si existen)
3. Reinicia Tomcat
4. Prueba login:

```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
	"username": "tu_usuario",
	"password": "tu_password"
}
```

5. El response contendrá `token`. Usa ese token en los demás servicios como `Authorization: Bearer <token>`.

## Notas importantes

- Si sigues viendo respuestas antiguas, probablemente Tomcat está sirviendo un WAR viejo (`banksoft.war`/`banksoft/`). Elimínalos y reinicia Tomcat.
- Después de cambiar la clave secreta o algoritmo, los tokens anteriores dejarán de ser válidos; regenera tokens mediante `register` o `login`.

CREDENCIALES PARA los usuarios de MOSQUITTO
 	auth-service
	auth1234

	accounts-service
	accounts1234	

	transfer-service
	transfer1234

## Levantar el sistema

### Requisitos previos
- Docker Desktop instalado y corriendo
- Java 21 instalado
- Tomcat 11 instalado

### Orden de arranque

#### 1. Levantar Mosquitto (broker MQTT)
Desde la carpeta `auth-service/` en PowerShell:
```powershell
docker-compose up -d
```
Verificar que está corriendo:
```powershell
docker ps
# Debe aparecer mosquitto-bank con status Up
docker logs mosquitto-bank
# Debe decir: mosquitto version 2.1.2 running
```

#### 2. Compilar el proyecto
```powershell
mvn clean package
# Genera target/ROOT.war
```

#### 3. Desplegar en Tomcat
```powershell
copy target\ROOT.war C:\ruta\a\tomcat\webapps\ROOT.war
```

#### 4. Arrancar Tomcat
```powershell
C:\ruta\a\tomcat\bin\startup.bat
```

### Para apagar
```powershell
# Apagar Tomcat
C:\ruta\a\tomcat\bin\shutdown.bat

# Apagar Mosquitto
docker-compose down
```

> **Importante:** Mosquitto debe estar corriendo **antes** de arrancar Tomcat.
> Si Mosquitto no está activo, el sistema funciona pero sin eventos MQTT entre microservicios.

## Arquitectura MQTT

MQTT actúa como bus de eventos interno entre microservicios. El frontend nunca interactúa con Mosquitto directamente.

### Topics
| Topic | Publica | Evento |
|---|---|---|
| `bank/auth/login-success` | Auth Service | Login o registro exitoso |
| `bank/auth/suspicious-login` | Auth Service | Usuario o contraseña incorrectos |
| `bank/accounts/blocked` | Accounts Service | Cuenta bloqueada |
| `bank/accounts/unblocked` | Accounts Service | Cuenta desbloqueada |
| `bank/accounts/limit-changed` | Accounts Service | Límite de transferencia modificado |
| `bank/transfers/completed` | Transfer Service | Transferencia registrada |

