# gestion-productos — API REST con Spring Boot

Proyecto de la asignatura **Servicios Web** (UAM). Cada rama corresponde a una
práctica: `semana7`, `semana8`, `semana9`.

## Requisitos

- Java 21
- PostgreSQL en `localhost:5432`, base `gestion_productos`, usuario `postgres`
- Maven (incluido mediante `mvnw`)

## Configurar el secreto JWT

La aplicación **no arranca sin el secreto**. No se guarda en el repositorio: se
lee de la variable de entorno `JWT_SECRET_BASE64`.

Genere una clave de 32 bytes en Base64:

```bash
java -e "" 2>/dev/null; powershell -Command "$b=New-Object byte[] 32; [System.Security.Cryptography.RandomNumberGenerator]::Create().GetBytes($b); [Convert]::ToBase64String($b)"
```

En **IntelliJ**: `Run` → `Edit Configurations…` → `Environment variables` →
agregar `JWT_SECRET_BASE64=<valor generado>`.

En **PowerShell**, antes de ejecutar:

```bash
$env:JWT_SECRET_BASE64 = '<valor generado>'
```

Si el valor falta o tiene menos de 32 bytes, la aplicación se detiene con un
mensaje explícito. **Nunca publique este valor ni un token vigente.**

## Ejecutar

```bash
cd gestion-productos
```

```bash
.\mvnw.cmd spring-boot:run
```

La API queda en `http://localhost:8080`.

## Usuarios de laboratorio

| Usuario | Contraseña | Rol |
|---|---|---|
| `admin` | `Admin123!` | ADMIN |
| `operador` | `Operador123!` | USER |

Son usuarios en memoria, exclusivos para desarrollo.

## Autenticarse

```bash
curl -X POST http://localhost:8080/api/auth/login -H "Content-Type: application/json" -d "{\"username\":\"admin\",\"password\":\"Admin123!\"}"
```

La respuesta trae `token`. Úselo en las peticiones protegidas como
`Authorization: Bearer <token>`. El token vence a los 30 minutos.

## Reglas de acceso

| Ruta | Método | Acceso |
|---|---|---|
| `/api/publico/**` | GET | Público |
| `/api/auth/login` | POST | Público |
| `/api/auth/perfil` | GET | Autenticado |
| `/api/productos/**` | GET | USER o ADMIN |
| `/api/productos/**` | POST, PUT, DELETE | Solo ADMIN |
| `/api/categorias/**` | GET | USER o ADMIN |
| `/api/categorias/**` | POST, PUT, DELETE | Solo ADMIN |

## Colecciones de pruebas

Se usa **Bruno** (equivalente a Postman). Abrir con *Open Collection* y
seleccionar el entorno **Local**:

| Carpeta | Contenido |
|---|---|
| `bruno` | Práctica semana 7: migraciones y relaciones |
| `bruno-lab1` | Laboratorio 1: servicios, DTOs y relación N–N |
| `bruno-seguridad` | Práctica semana 9: autenticación y roles con JWT |

Ejecutar la colección de seguridad completa desde la terminal:

```bash
npx @usebruno/cli run . --env Local
```

## Documentos de entrega

- `Entrega-Semana7.html`
- `Entrega-Lab1.html`
- `Entrega-Seguridad.html`

Ábralos en un navegador.

Las cinco evidencias del reto final de la semana 9 están en `capturas/` y se
muestran incrustadas dentro de `Entrega-Seguridad.html`.
