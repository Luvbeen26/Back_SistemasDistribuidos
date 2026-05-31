# auth-service

Servicio backend basado en Java Servlets, Maven, Hibernate y JWT.

## Requisitos

- Java 21
- Maven 3.9+
- Apache Tomcat 11
- PostgreSQL accesible desde la aplicación

## Estructura del despliegue

Este módulo se empaqueta como WAR. El artefacto final se genera en `target/banksoft.war` y, por la configuración de Maven, Tomcat lo desplegará con el contexto `banksoft`.

La URL base queda así:

`http://localhost:8080/banksoft`

## Compilación

Desde la carpeta `auth-service`:

```bash
*este es el bueno
mvn clean package
```

Si además quieres generar el JAR lanzable configurado en el proyecto, el mismo ciclo de Maven lo deja en `target/launcher.jar`.

## Despliegue en Tomcat

Opción 1: copiar el WAR manualmente

```bash
copy target\banksoft.war "C:\Program Files\Apache Software Foundation\Tomcat 11.0\webapps\"
```

Opción 2: reiniciar Tomcat si ya está ejecutándose y dejar que tome el WAR desde `webapps`.

## Endpoints

### Auth

- `POST /api/auth/login`
- `POST /api/auth/register`

## Configuración necesaria

Antes de desplegar verifica estos archivos:

- `src/main/resources/hibernate.cfg.xml`: conexión a PostgreSQL, usuario y contraseña
- `src/main/resources/application.properties`: propiedades de la aplicación si las usas para valores externos
- `src/main/java/banksoft/filter/JwtFilter.java`: clave secreta JWT y reglas de exclusión
- `src/main/java/banksoft/util/`: inicialización de recursos, si aplica en tu entorno

## Notas importantes

- Las rutas de los servlets no cambian por mover el proyecto de carpeta; solo cambia el context path si cambias el nombre del WAR o la configuración de Tomcat.
- Este proyecto usa Jakarta Servlet, así que debe ejecutarse en Tomcat 11 o un contenedor compatible con Jakarta EE 10/11.
- Si el frontend o el API Gateway consumen este servicio, asegúrate de que apunten a `http://localhost:8080/banksoft`.

## Verificación rápida

1. Ejecuta `mvn clean package`
2. Copia `target/banksoft.war` a `webapps`
3. Inicia o reinicia Tomcat
4. Prueba `http://localhost:8080/banksoft/api/auth/login`
