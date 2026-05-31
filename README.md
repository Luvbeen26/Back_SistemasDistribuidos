# Estructura del Proyecto banco-backend

Este proyecto es un sistema bancario backend desarrollado con **Java Servlets**, **Hibernate ORM** y **Maven**.

## Estructura de Carpetas

```
banco-backend/
├── pom.xml                                    # Configuración de Maven
├── src/
│   └── main/
│       ├── java/mx/edu/uas/banco/
│       │   ├── servlet/                       # Servlets REST
│       │   │   ├── CuentaServlet.java
│       │   │   ├── MovimientoServlet.java
│       │   │   └── ClienteServlet.java
│       │   ├── filter/                        # Filtros (Dev 3)
│       │   │   └── JwtFilter.java
│       │   ├── model/                         # Entidades JPA
│       │   │   ├── Cliente.java
│       │   │   ├── CuentaBancaria.java
│       │   │   ├── Movimiento.java
│       │   │   └── CajeroAtm.java
│       │   ├── dao/                           # Data Access Objects
│       │   │   ├── ClienteDAO.java
│       │   │   ├── CuentaDAO.java
│       │   │   └── MovimientoDAO.java
│       │   └── util/                          # Utilidades
│       │       ├── HibernateUtil.java         # SessionFactory singleton
│       │       └── MqttPublisher.java         # Cliente MQTT
│       ├── resources/
│       │   └── hibernate.cfg.xml              # Configuración Hibernate
│       └── webapp/
│           └── WEB-INF/
│               └── web.xml                    # Mapeo servlets/filtros
└── target/                                    # Artefactos compilados
```

## Configuración

### 1. Actualizar hibernate.cfg.xml
Edita `src/main/resources/hibernate.cfg.xml` con los datos del servidor MySQL (IP del master de Dev 2):

```xml
<property name="hibernate.connection.url">jdbc:mysql://IP_MASTER:3306/banco_db</property>
<property name="hibernate.connection.username">usuario</property>
<property name="hibernate.connection.password">contraseña</property>
```

### 2. Compilar el proyecto
```bash
mvn clean install
```

### 3. Desplegar en Tomcat
Copiar el WAR generado a `$CATALINA_HOME/webapps/`

## Endpoints

### Cliente
- `GET /api/clientes` - Listar todos
- `GET /api/clientes/{id}` - Obtener por ID
- `POST /api/clientes` - Crear
- `PUT /api/clientes` - Actualizar
- `DELETE /api/clientes/{id}` - Eliminar

### Cuenta
- `GET /api/cuentas?clienteId={id}` - Listar cuentas del cliente
- `GET /api/cuentas/{id}` - Obtener por ID
- `POST /api/cuentas` - Crear
- `PUT /api/cuentas` - Actualizar
- `DELETE /api/cuentas/{id}` - Eliminar

### Movimiento
- `GET /api/movimientos?cuentaId={id}` - Listar movimientos
- `GET /api/movimientos/{id}` - Obtener por ID
- `POST /api/movimientos` - Crear
- `PUT /api/movimientos` - Actualizar
- `DELETE /api/movimientos/{id}` - Eliminar

## Dependencias Principales

- **Servlet API 4.0** - API de Servlets
- **Hibernate 5.6** - ORM
- **MySQL Connector 8.0** - Driver JDBC
- **MQTT Client** - Publicación de eventos
- **JWT (jjwt)** - Tokens JWT
- **Gson** - Serialización JSON
- **SLF4J + Logback** - Logging

## Notas

- Dev 3 es responsable del **JwtFilter**
- Dev 2 proporciona IP del master para conectar BD
- Reemplaza la clave secreta en `JwtFilter.java` por una real
- Configura la URL de MQTT en `MqttPublisher.java`
