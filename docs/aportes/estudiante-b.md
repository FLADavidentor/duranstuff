# Aporte técnico — Estudiante B

**Responsabilidad asumida:** *Implementación de endpoints* (Integrante 2 del enunciado) y
*arquitectura* (parte de la responsabilidad del Integrante 3, dividida por tratarse de un
equipo de dos).

**Rama de trabajo:** `docs/aporte-estudiante-b` → integrada a `Semana3` mediante Pull Request.

---

## Commits realizados

| Commit | Fase del taller | Aporte |
|---|---|---|
| `8dacf5d` | Fase 2 — Configuración | Deja el proyecto con Spring Web como única dependencia. Retira JPA, JDBC, PostgreSQL y Lombok del `pom.xml`, elimina la conexión a base de datos del arranque y descarta las clases `Student` del ejercicio anterior, porque el enunciado exige que los datos permanezcan en memoria y que no se utilice base de datos. Verificado con `mvnw clean compile`. |
| `de6ffa2` | Fase 3 — Modelado | Recurso `Producto` y `AlmacenProductos` con los ocho productos ficticios. **Ver nota de atribución abajo.** |
| `198b8b8` | Fase 3 — Implementación | `ProductoControlador` con la consulta de la colección y la consulta por identificador mediante variable de ruta. `ProductoNoEncontradoException` y `ManejadorDeErrores` para responder `404` en lugar de `500`. |
| `00ec5a3` | Fase 3 — Implementación | Registro de productos con `POST`: recepción del JSON en el cuerpo, respuesta `201 Created` y cabecera `Location` con la URI del recurso creado. |
| `4adcc1c` | Fase 5 — Arquitectura | Diagrama en draw.io con los dos recorridos exigidos y `docs/04-arquitectura.md` explicando la diferencia entre ambos. |

---

## Decisiones técnicas que puedo defender

**Por qué la ruta es `/api/productos` y no `/api/obtenerProductos`.** En REST la URI identifica un
recurso, no una acción. La acción la expresa el método HTTP. Por eso `GET /api/productos` y
`POST /api/productos` comparten ruta: es el mismo recurso, distinta operación.

**Por qué el identificador viaja en la URI y no en el cuerpo.** `/api/productos/{id}` es una URI de
recurso individual: el id forma parte de la dirección de ese recurso. `@PathVariable` es lo que
extrae ese fragmento y lo convierte a `Long`. Los datos que describen al producto (nombre,
presentación, categoría, disponibilidad) sí viajan en el JSON, porque son contenido, no dirección.

**Por qué `201` y no `200` en el registro.** `200 OK` diría solamente que la petición se procesó.
`201 Created` comunica que la petición **creó un recurso nuevo**, y se acompaña de la cabecera
`Location` para que el cliente sepa dónde consultarlo. Verificado: el `POST` devolvió
`Location: /api/productos/9` y `GET /api/productos/9` responde `200`.

**Por qué `404` y no `500` cuando el producto no existe.** `500` significa "el servidor falló", y
eso sería mentir: el servidor entendió la petición y la procesó correctamente. Lo que ocurre es
que el recurso no existe, y para eso HTTP tiene `404 Not Found`. Sin `ManejadorDeErrores` la
excepción llegaría sin controlar al contenedor y la API respondería `500`.

**Por qué el id lo asigna el servidor.** `AlmacenProductos` ignora cualquier id que venga en el
JSON de entrada y asigna el siguiente con un contador atómico. Se usa una lista sincronizada
porque Tomcat atiende cada petición en un hilo distinto, y dos registros simultáneos podrían
corromper la lista o repetir un identificador.

---

## Nota de atribución sobre el commit `de6ffa2`

Según el reparto de roles, el recurso `Producto` y el almacén en memoria corresponden al
**Estudiante A** (*diseño técnico y modelado*). Los commiteó el Estudiante B de forma deliberada,
como **base compartida** y en el primer tramo del trabajo: sin la clase `Producto` compilando, el
controlador no podía escribirse y los dos estudiantes habrían quedado trabajando en serie en
lugar de en paralelo.

Se deja constancia explícita para que la autoría del historial de Git no se lea como una
distribución desigual del trabajo. La especificación de la API, el análisis técnico, las pruebas
de Postman y la matriz de resultados —el contenido conceptual del modelado— son aporte del
Estudiante A.

---

## Estado verificado de la implementación

Ejecutado contra la aplicación corriendo en `localhost:8080`:

| # | Escenario | Petición | Código | Resultado |
|---|---|---|---|---|
| 1 | Colección completa | `GET /api/productos` | `200` | ocho productos en JSON |
| 2 | ID existente | `GET /api/productos/3` | `200` | un único recurso |
| 3 | Primer ID | `GET /api/productos/1` | `200` | corresponde al solicitado |
| 4 | ID inexistente | `GET /api/productos/999` | `404` | cuerpo de error, no `500` |
| 5 | Registrar producto | `POST /api/productos` | `201` | `Location: /api/productos/9` |
| 6 | Colección de nuevo | `GET /api/productos` | `200` | aparece el id 9 |
