# TransportOrdersAPI

API REST de órdenes de transporte con Java 17, Spring Boot 3.3.5, PostgreSQL, JWT, Swagger y Docker.

## Requisitos
- Docker y Docker Compose (opción recomendada), o Java 17 + Maven 3.9 + PostgreSQL 14+.

## Inicio rápido con Docker
```bash
# En la raíz del proyecto
# Para uso fuera de desarrollo, configure DB_PASSWORD, APP_USERNAME, APP_PASSWORD y JWT_SECRET
# (JWT_SECRET: al menos 32 bytes, use un secreto aleatorio largo)
docker compose up --build -d
docker compose logs -f api
```
La API queda en http://localhost:8080 y Swagger en http://localhost:8080/swagger-ui/index.html.

## Ejecución local en STS4
1. Inicie PostgreSQL y cree base `transportdb`, usuario `transport` y contraseña `transport`, o cambie DB_URL, DB_USER, DB_PASSWORD.
2. File > Import > Maven > Existing Maven Projects y seleccione la carpeta.
3. Ejecute `TransportOrdersApplication` como Spring Boot App.
4. Alternativa por terminal: `mvn spring-boot:run`.
5. Pruebas: `mvn test`.

## Autenticación
Para pruebas locales el usuario inicial es `admin` y contraseña `admin123` (variables `APP_USERNAME` y `APP_PASSWORD`). **No use estos valores en producción.**
```bash
curl -X POST http://localhost:8080/api/auth/login -H 'Content-Type: application/json' -d '{"username":"admin","password":"admin123"}'
```
Copie `token` de la respuesta y envíelo en `Authorization: Bearer <token>` en las demás rutas. Expira en 1 hora. Swagger permite pegar el JWT en **Authorize**.

## Endpoints
| Método | Ruta | Cuerpo / filtros |
|---|---|---|
| POST | /api/auth/login | `{ "username":"admin", "password":"admin123" }` |
| POST | /api/orders | `{ "origin":"CDMX", "destination":"Puebla" }` |
| GET | /api/orders/{id} | UUID |
| GET | /api/orders | `status`, `origin`, `destination`, `start`, `end` (fechas ISO-8601 UTC, ej. `2026-11-01T00:00:00Z`) |
| PATCH | /api/orders/{id}/status | `{ "status":"IN_TRANSIT" }` |
| POST | /api/drivers | `{ "name":"Ana", "licenseNumber":"LIC-123", "active":true }` |
| GET | /api/drivers/active | Sin cuerpo |
| POST | /api/assignments | `{ "orderId":"UUID", "driverId":"UUID" }` |
| POST | /api/assignments/{id}/pdf | `multipart/form-data`, campo `file` (PDF) |
| POST | /api/assignments/{id}/image | `multipart/form-data`, campo `file` (PNG/JPEG) |

## Reglas
- Orden nueva: CREATED.
- CREATED -> IN_TRANSIT o CANCELLED; IN_TRANSIT -> DELIVERED. Estados finales sin transiciones.
- Solo conductores activos y órdenes CREATED se pueden asignar; una asignación por orden.
- PDF, PNG y JPG se validan por cabecera de archivo; máximo 10 MB.
- Las rutas de almacenamiento se mantienen internas; las respuestas solo indican si hay archivos.

## Errores
- 400: validación o archivo inválido; 401: token ausente/incorrecto; 404: recurso inexistente; 409: transición, asignación o licencia duplicada; 413: archivo demasiado grande; 500: error interno.
- Errores de negocio retornan JSON con timestamp, status, error, message y path.
- Logs de aplicación en formato JSON por consola.

## Ejemplo de flujo
1. Inicie sesión y obtenga el JWT.
2. Cree una orden y un conductor activo; conserve sus UUID.
3. Cree una asignación con ambos UUID y conserve el ID de asignación.
4. Suba un PDF y una imagen al ID de asignación (campo `file`).
5. Cambie la orden a IN_TRANSIT y después DELIVERED.
6. Consulte filtros de órdenes por estado y fechas.

## GitHub
```bash
git init
git add .
git commit -m "feat: implement transport orders API with JWT and Docker"
git branch -M main
git remote add origin https://github.com/TU_USUARIO/transport-orders-api.git
git push -u origin main
```
Cree primero un repositorio vacío en GitHub. No suba secretos ni `.env`.

## Consideraciones para producción
Esta es una implementación de evaluación técnica. Antes de producción: credenciales seguras y secret manager, migraciones Flyway en vez de `ddl-auto: update`, almacenamiento de archivos externo, escaneo antimalware, control de acceso granular, HTTPS, paginación, métricas y pruebas de integración.
