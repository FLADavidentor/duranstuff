# Arquitectura de la solución — Taller #1

Diagrama fuente: [`docs/diagrama/arquitectura.drawio`](diagrama/arquitectura.drawio) (abrir en <https://app.diagrams.net>).

El diagrama representa **la solución desarrollada por el equipo**, no un esquema genérico de
Spring: los bloques llevan los nombres reales de las clases de este repositorio.

---

## 1. Bloques del diagrama

| Bloque | Clase real en el repositorio | Rol |
|---|---|---|
| Cliente / Postman | — | Origen de la petición HTTP |
| Aplicación Spring Boot | `DuranstuffApplication` | Contenedor: Tomcat embebido en el puerto 8080 |
| DispatcherServlet | (lo aporta Spring) | *Front controller*: recibe **toda** petición y decide quién la atiende |
| ProductoControlador | `controlador/ProductoControlador` | Traduce la petición HTTP a una llamada Java y arma la respuesta |
| AlmacenProductos | `datos/AlmacenProductos` | Los 8 productos en memoria (no hay base de datos) |
| Jackson | (lo aporta Spring) | Convierte el objeto Java en JSON y viceversa |
| ManejadorDeErrores | `error/ManejadorDeErrores` | Traduce excepciones de la aplicación a códigos HTTP |

---

## 2. Recorrido A — consulta exitosa

Petición concreta: **`GET /api/productos/3`** → **`200 OK`**

1. Postman envía la petición al puerto 8080.
2. El **DispatcherServlet** la recibe. Es el único punto de entrada: examina la ruta
   (`/api/productos/3`) y el método (`GET`) y busca qué método Java tiene un mapeo que coincida.
   Encuentra `consultarPorId` porque está anotado con `@GetMapping("/{id}")` dentro de una clase
   con `@RequestMapping("/api/productos")`.
3. El **ProductoControlador** recibe el valor `3`. `@PathVariable` es lo que extrae ese fragmento
   de la URI y lo convierte de texto a `Long`. Con ese id llama a `almacen.buscarPorId(3)`.
4. El **AlmacenProductos** recorre su lista y devuelve un `Optional` **con** el producto dentro.
5. El controlador obtiene el objeto Java `Producto` y lo devuelve envuelto en `ResponseEntity.ok(...)`.
6. Como la clase está anotada con `@RestController`, lo devuelto **no** es el nombre de una vista
   sino el cuerpo de la respuesta: **Jackson** recorre los getters del `Producto`
   (`getId`, `getNombre`, `getPresentacion`, `getCategoria`, `isDisponible`) y construye el JSON.
   El resultado viaja de vuelta como `200 OK`.

Respuesta observada:

```
HTTP/1.1 200
{"id":3,"nombre":"Cafe Soluble Clasico","presentacion":"200 g","categoria":"Cafe instantaneo","disponible":false}
```

---

## 3. Recorrido B — producto inexistente

Petición concreta: **`GET /api/productos/999`** → **`404 Not Found`**

1. Postman envía la petición. **Idéntico al recorrido A.**
2. El DispatcherServlet la enruta al mismo método del mismo controlador. **Idéntico al recorrido A**:
   la petición está perfectamente bien formada.
3. El controlador llama a `almacen.buscarPorId(999)`. **Idéntico al recorrido A.**
4. **Acá se separan los dos recorridos:** el almacén devuelve un `Optional` **vacío**.
5. El controlador ejecuta `orElseThrow` y lanza `ProductoNoEncontradoException`. Jackson nunca
   llega a serializar un `Producto`, porque no hay ningún producto que serializar.
6. El **ManejadorDeErrores**, anotado con `@RestControllerAdvice`, intercepta esa excepción y
   responde `404 Not Found` con un cuerpo JSON que explica el error.

Respuesta observada:

```
HTTP/1.1 404
{"estado":404,"error":"Not Found","mensaje":"No existe un producto con el identificador 999"}
```

---

## 4. Qué cambia entre ambos recorridos

Esta es la pregunta que exige el enunciado, y la respuesta corta es: **casi nada, y por eso
importa dónde cambia.**

| | Recorrido A | Recorrido B |
|---|---|---|
| Ruta | `/api/productos/{id}` | la misma |
| Método HTTP | `GET` | el mismo |
| Método Java que atiende | `consultarPorId` | el mismo |
| Enrutamiento del DispatcherServlet | igual | igual |
| **Resultado de `buscarPorId`** | **`Optional` con valor** | **`Optional` vacío** |
| Quién arma la respuesta | Jackson, sobre un `Producto` | `ManejadorDeErrores`, sobre una excepción |
| Código de estado | `200 OK` | `404 Not Found` |

**El punto de bifurcación es el paso 4**, no antes. Los dos recorridos son la misma operación
sobre el mismo recurso; lo único que cambia es si ese recurso existe. Por eso ambos usan la misma
ruta y el mismo método: en REST la URI identifica *qué* se pide y el método *qué se quiere hacer*,
y ninguna de las dos cosas cambia porque el producto falte. Lo que cambia es el **código de
estado**, que es precisamente el mecanismo que HTTP ofrece para comunicar el desenlace.

**Por qué 404 y no 500.** Sin `ManejadorDeErrores` la excepción llegaría sin controlar al
contenedor y la API respondería `500 Internal Server Error`, que significa "el servidor falló".
Eso sería mentir: el servidor funcionó perfectamente, entendió la petición y la procesó. Lo que
ocurre es que el recurso solicitado no existe, y para eso HTTP tiene `404 Not Found`. Un cliente
que recibe 500 concluye que debe reintentar más tarde; uno que recibe 404 concluye que ese
producto no está y no lo va a estar. Elegir el código correcto es parte del diseño de la API.

---

## 5. Códigos de estado usados en la solución

| Código | Cuándo se devuelve | Por qué ese y no otro |
|---|---|---|
| `200 OK` | Consulta de la colección y consulta por id existente | La petición se procesó y la respuesta trae el recurso pedido. Una colección vacía también es `200`: consultar y no encontrar nada **en una colección** no es un error. |
| `201 Created` | Registro de un producto con `POST` | No basta con `200`: la petición **creó un recurso nuevo**. `201` lo comunica y se acompaña de la cabecera `Location` con la URI del recurso creado, para que el cliente sepa dónde consultarlo. |
| `404 Not Found` | Consulta de un id que no existe | La petición es válida pero el recurso individual no existe. No es `400` (la petición está bien formada) ni `500` (el servidor no falló). |
