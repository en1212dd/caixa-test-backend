**Resumen**
- **Descripción:** Proyecto de ejemplo (Spring Boot 3.5.x, Java 17) que implementa una API REST con JPA, seguridad básica, documentación OpenAPI y pruebas unitarias. Esta aplicación es una prueba tecnica por lo que contiene distintos enfoques de diseño, no está pensada como producto listo para producción.

**Requisitos**
- **Java:**: 17
- **Maven:**: 3.x
- **Docker / docker-compose:**: para levantar la base de datos y la app en contenedores
- **Archivos importantes:**: [pom.xml](pom.xml), [docker-compose.yml](docker-compose.yml), [BBDD/init.sql](BBDD/init.sql), [src/main/resources/application.properties](src/main/resources/application.properties)

**Compilar y ejecutar localmente**

**Dockerización**
- Levantar base de datos MariaDB y (opcional) la app con `docker-compose`:

```bash
docker-compose up --build
```
- La base de datos se inicializa con el script [BBDD/init.sql](BBDD/init.sql), que crea las tablas, algunas filas de ejemplo y las vistas `v_client_detail` / `v_loan_request_detail` usadas en los endpoints de consulta.

**Construir JAR**

> [!WARNING]
> En el caso de usar esta opcion se requiere que se levante la base de datos por separado, ya sea el docker o una base de datos local
```bash
mvn -DskipTests package
```

- Ejecutar la aplicación localmente (jar generado):


```bash
java -jar target/caixatest-0.0.1-SNAPSHOT.jar
```


**Credenciales (entorno de desarrollo / pruebas)**
- **API (HTTP Basic):**
  - **admin / admin**: rol `ADMIN` (acceso a endpoints privados/administración)
  - **user / user**: rol `USER` (puede crear solicitudes de préstamo)
- **Base de datos (contenedor MariaDB)**
  - Usuario/Password/DB: configurado en [docker-compose.yml](docker-compose.yml) y en [BBDD/init.sql](BBDD/init.sql). Revisa esas rutas si cambias el entorno.

**Endpoints principales**
- **Clientes**
  - GET `/api/clients/private/details`: lista de vista detallada (vista DB) — `ADMIN`
  - GET `/api/clients/details`: lista de DTOs de clientes — (roles `USER` o `ADMIN`)
- **Solicitudes de préstamo**
  - GET `/api/loan-requests/private/details`: lista detallada (vista DB) — `ADMIN`
  - GET `/api/loan-requests/details`: lista de DTOs — (roles `USER` o `ADMIN`)
  - GET `/api/loan-requests/{id}`: obtener solicitud por id — (roles `USER` o `ADMIN`)
  - POST `/api/loan-requests/create`: crear nueva solicitud (roles `USER` o `ADMIN`), validaciones: cliente existe, moneda existe, amount entre límites
  - PATCH `/api/loan-requests/{id}/status`: cambiar estado (ADMIN) con transiciones controladas (PENDING -> APPROVED|REJECTED; APPROVED -> CANCELLED)

(La documentación interactiva OpenAPI/Swagger se expone automáticamente cuando la app está en marcha gracias a `springdoc`.)

**Ubicación del código**
- Controllers: [src/main/java/com/caixa/test/caixatest/controller](src/main/java/com/caixa/test/caixatest/controller)
- Services: [src/main/java/com/caixa/test/caixatest/service](src/main/java/com/caixa/test/caixatest/service)
- Repositories: [src/main/java/com/caixa/test/caixatest/repository](src/main/java/com/caixa/test/caixatest/repository)
- Entidades y vistas: [src/main/java/com/caixa/test/caixatest/entities](src/main/java/com/caixa/test/caixatest/entities)
- DTOs: [src/main/java/com/caixa/test/caixatest/dto](src/main/java/com/caixa/test/caixatest/dto)
- Tests unitarios: [src/test/java](src/test/java)

**Pruebas unitarias**
- Ejecutar tests unitarios:

```bash
mvn test
```

- Cobertura de pruebas incluidas en el repositorio:
  - Tests de servicio: [src/test/java/com/caixa/test/caixatest/service](src/test/java/com/caixa/test/caixatest/service)

**Sobre la carpeta `ia/`**
- La carpeta `ia/` contiene prompts y notas utilizadas para configurar/orientar a un asistente de IA (actuando como arquitecto) para generar o refactorizar código. Se creo principalmente para demostrar el buen uso de un agente IA intentando siempre focalizar y oriental a la tecnologia para sacar su maximo rendimiento creando codigo de calidad y acotado a los distintos escenarios.

**Pasos rápidos**
- Run with Docker: `docker-compose up --build`


