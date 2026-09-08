# 02 — Análisis técnico obligatorio

**Taller #1 · Servicios Web · Spring Boot**

Respuestas a las cinco preguntas de análisis obligatorio de la Fase 1, aplicadas a la API de
catálogo de productos de Café Soluble S.A. (caso ficticio).

---

## a. ¿Por qué la ruta utiliza un sustantivo y no una acción?

Porque en REST la URI identifica **un recurso**, no una operación. `/api/productos` nombra la
colección de productos: es una cosa direccionable, no algo que se hace. La acción ya la expresa el
método HTTP, así que ponerla también en la ruta —`/api/obtenerProductos`, `/api/crearProducto`—
sería duplicar información y romper la uniformidad de la interfaz. Con sustantivos, una sola URI
soporta varias operaciones: en nuestra API, `GET /api/productos` consulta y `POST /api/productos`
registra, sobre exactamente la misma ruta. Eso hace la API predecible: quien conoce el recurso
puede deducir las rutas sin documentación adicional. En el código esto se ve en la anotación
`@RequestMapping("/api/productos")` del controlador: una sola ruta base, cuatro comportamientos
distintos según el método y la presencia de la variable de ruta.

---

## b. ¿Qué diferencia existe entre una URI de colección y una URI de recurso individual?

La **URI de colección**, `/api/productos`, identifica al conjunto completo; la **URI de recurso
individual**, `/api/productos/{id}`, identifica a un elemento concreto dentro de ese conjunto. La
diferencia se nota en tres cosas. Primero, en la representación: la colección devuelve un **arreglo
JSON** y el recurso individual devuelve **un objeto JSON**. Segundo, en los estados posibles: una
colección vacía sigue siendo `200 OK` con `[]`, porque la colección existe aunque no tenga
elementos, mientras que un identificador que no corresponde a ningún producto produce
`404 Not Found`. Tercero, en el significado de las operaciones: `POST` sobre la colección crea un
elemento nuevo dentro de ella, mientras que operar sobre la URI individual afecta únicamente a ese
elemento. La URI individual se construye agregando el identificador como segmento de ruta de la
colección, y esa jerarquía expresa que el producto pertenece a ese catálogo.

---

## c. ¿Por qué el método HTTP forma parte del significado de la operación?

Porque la URI dice **sobre qué** se opera y el método dice **qué se hace** con ello: el significado
completo es la combinación de ambos. En nuestra API, `GET /api/productos` y `POST /api/productos`
comparten exactamente la misma URI y son operaciones distintas; lo único que las separa es el
método. Además, cada método trae una semántica estándar que el cliente puede asumir sin conocer la
implementación: `GET` es **seguro** (no modifica el estado del servidor) e **idempotente**
(repetirlo da el mismo resultado), mientras que `POST` **no** es idempotente, y por eso enviarlo dos
veces crea dos productos distintos con dos identificadores distintos. Esa semántica compartida
permite que intermediarios como cachés y proxies actúen correctamente sin entender el dominio:
pueden guardar en caché un `GET`, pero nunca un `POST`. Si la acción viviera en la ruta, esa
información se perdería y cada cliente tendría que aprenderse la API una por una.

---

## d. ¿Qué información debe viajar en la URI y cuál en el JSON?

En la **URI** viaja lo que **identifica** al recurso: la ruta base y el identificador del elemento,
como el `3` de `/api/productos/3`. Es información de direccionamiento, corta, visible en los
registros del servidor y en el historial del navegador, y es lo que hace que la petición sea
repetible: la misma URI siempre apunta al mismo recurso. En el **cuerpo JSON** viaja el **estado o
contenido** del recurso: `nombre`, `presentacion`, `categoria` y `disponible` cuando se registra un
producto con `POST`. Ese contenido puede ser extenso, estructurado y contener caracteres que no se
representan cómodamente en una URI. Por eso `GET /api/productos/3` no lleva cuerpo —le basta el
identificador— y `POST /api/productos` no lleva identificador en la ruta, porque el recurso todavía
no existe. De hecho, en nuestra implementación el `id` que el cliente incluya en el JSON se
descarta: `registrar()` siempre sobrescribe el identificador. La identidad es responsabilidad del
servidor y se comunica de vuelta en el cuerpo de la respuesta y en la cabecera `Location`.

---

## e. ¿Qué código HTTP permite distinguir una consulta exitosa, una creación y un recurso inexistente?

Cada situación tiene su propio código de estado, y esa es la forma en que la API comunica el
resultado sin obligar al cliente a interpretar el cuerpo:

- **`200 OK`** — consulta exitosa. La petición se procesó y la representación solicitada viaja en el
  cuerpo. Es la respuesta de `GET /api/productos` y de `GET /api/productos/{id}` cuando el producto
  existe.
- **`201 Created`** — creación exitosa. Además de indicar éxito, informa que **se creó un recurso
  nuevo**, algo que un `200` no expresa. Se acompaña de la cabecera `Location` con la URI del
  recurso recién creado, para que el cliente sepa dónde consultarlo después.
- **`404 Not Found`** — recurso inexistente. La URI está bien formada y el servidor la entendió,
  pero no hay ningún recurso con ese identificador.

La distinción importa porque separa tres cosas que se confunden con facilidad: que la petición
funcionara, que produjera un recurso nuevo, y que el recurso solicitado no exista. Un `404` **no**
es un error del servidor: por eso la aplicación lanza una excepción propia,
`ProductoNoEncontradoException`, y un `@RestControllerAdvice` la traduce a `404`. Sin esa
traducción, la excepción llegaría cruda al contenedor y la API respondería `500`, que significa
"el servidor falló" y le diría al cliente algo falso. Los tres códigos pertenecen a familias
distintas —`2xx` éxito, `4xx` error atribuible al cliente— y esa clasificación permite que el
cliente decida cómo actuar leyendo únicamente la línea de estado de la respuesta.
