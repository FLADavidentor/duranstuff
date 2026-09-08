# 03 — Matriz de pruebas técnicas (Postman)

**Taller #1 · Servicios Web · Spring Boot** — Fase 4 del enunciado.

Colección ejecutada: [`postman/catalogo-productos.postman_collection.json`](../postman/catalogo-productos.postman_collection.json)
Evidencias: [`docs/evidencias/`](evidencias/)

**Entorno de ejecución:** aplicación levantada desde IntelliJ IDEA sobre `http://localhost:8080`,
con los 8 productos iniciales recién cargados en memoria. Las seis peticiones se ejecutaron **en
orden y en una sola corrida**, porque el escenario 6 depende del producto que registra el 5.

Cada petición de la colección lleva **aserciones automáticas** (`pm.test`), de modo que el
resultado no depende de leer la captura a ojo: la herramienta verifica el código de estado, la
forma de la respuesta y su contenido.

---

## Resumen

| # | Escenario | Método | URL | Esperado | Obtenido | Tests | Resultado |
|---|---|---|---|---|---|---|---|
| 1 | Consultar la colección completa | `GET` | `/api/productos` | `200 OK` | `200 OK` | 5/5 | ✅ |
| 2 | Consultar un ID existente | `GET` | `/api/productos/3` | `200 OK` | `200 OK` | 3/3 | ✅ |
| 3 | Consultar el primer ID existente | `GET` | `/api/productos/1` | `200 OK` | `200 OK` | 3/3 | ✅ |
| 4 | Consultar un ID inexistente | `GET` | `/api/productos/999` | `404 Not Found` | `404 Not Found` | 3/3 | ✅ |
| 5 | Registrar un producto válido | `POST` | `/api/productos` | `201 Created` + `Location` | `201 Created` + `Location` | 5/5 | ✅ |
| 6 | Consultar nuevamente la colección | `GET` | `/api/productos` | `200 OK` con el nuevo producto | `200 OK`, 9 productos | 3/3 | ✅ |

**Total: 22 de 22 aserciones aprobadas. Ninguna desviación respecto a la especificación de la Fase 1.**

---

## Prueba 1 — Consultar la colección completa

| Campo | Valor |
|---|---|
| **Objetivo** | Comprobar que la URI de colección devuelve todos los productos en JSON. |
| **Método / URL** | `GET http://localhost:8080/api/productos` |
| **JSON de entrada** | No aplica. |
| **Código esperado** | `200 OK` |
| **Código obtenido** | `200 OK` (376 ms, 1.04 KB) |
| **Evidencia** | [`01-coleccion-completa.png`](evidencias/01-coleccion-completa.png) |

**Respuesta recibida:** un arreglo JSON con los 8 productos cargados en memoria, con identificadores
del 1 al 8. Los primeros elementos:

```json
[
  { "id": 1, "nombre": "Cafe Soluble Clasico", "presentacion": "50 g",  "categoria": "Cafe instantaneo", "disponible": true },
  { "id": 2, "nombre": "Cafe Soluble Clasico", "presentacion": "100 g", "categoria": "Cafe instantaneo", "disponible": true },
  { "id": 3, "nombre": "Cafe Soluble Clasico", "presentacion": "200 g", "categoria": "Cafe instantaneo", "disponible": false }
]
```

**Aserciones aprobadas (5/5):** código `200`; `Content-Type: application/json`; la respuesta es un
arreglo; contiene exactamente 8 elementos; cada elemento expone las cinco propiedades del recurso.

**Conclusión técnica.** La operación 1 de la especificación se cumple. La respuesta es un **arreglo**,
que es la forma correcta de representar una colección, y no un objeto. El hecho de que cada elemento
traiga exactamente `id`, `nombre`, `presentacion`, `categoria` y `disponible` confirma que Jackson
está serializando a partir de los getters de `Producto` —incluido `isDisponible()`, que aparece como
`"disponible"`— y que no se filtra ningún campo interno. Los 8 productos demuestran el requisito de
la Fase 3 de cargar un mínimo de ocho elementos en memoria.

---

## Prueba 2 — Consultar un ID existente

| Campo | Valor |
|---|---|
| **Objetivo** | Comprobar que la URI de recurso individual devuelve exactamente un recurso. |
| **Método / URL** | `GET http://localhost:8080/api/productos/3` |
| **JSON de entrada** | No aplica. |
| **Código esperado** | `200 OK` |
| **Código obtenido** | `200 OK` (31 ms, 277 B) |
| **Evidencia** | [`02-id-existente.png`](evidencias/02-id-existente.png) |

**Respuesta recibida:**

```json
{ "id": 3, "nombre": "Cafe Soluble Clasico", "presentacion": "200 g", "categoria": "Cafe instantaneo", "disponible": false }
```

**Aserciones aprobadas (3/3):** código `200`; la respuesta es un objeto y **no** un arreglo; expone
las cinco propiedades del recurso.

**Conclusión técnica.** Esta prueba demuestra la diferencia entre URI de colección y URI de recurso
individual analizada en `docs/02`: la misma API, sobre el mismo método `GET`, devuelve un arreglo en
`/api/productos` y un objeto en `/api/productos/3`. El tamaño de la respuesta lo confirma: 277 bytes
contra 1.04 KB. Además el producto devuelto tiene `disponible: false`, lo que verifica que el valor
booleano viaja tal cual está en memoria y no se pierde en la serialización.

---

## Prueba 3 — Consultar el primer ID existente

| Campo | Valor |
|---|---|
| **Objetivo** | Comprobar que la respuesta corresponde al recurso solicitado y no a otro. |
| **Método / URL** | `GET http://localhost:8080/api/productos/1` |
| **JSON de entrada** | No aplica. |
| **Código esperado** | `200 OK` |
| **Código obtenido** | `200 OK` (7 ms, 275 B) |
| **Evidencia** | [`03-primer-id.png`](evidencias/03-primer-id.png) |

**Respuesta recibida:**

```json
{ "id": 1, "nombre": "Cafe Soluble Clasico", "presentacion": "50 g", "categoria": "Cafe instantaneo", "disponible": true }
```

**Aserciones aprobadas (3/3):** código `200`; **el `id` de la respuesta es exactamente `1`**, el
mismo que se pidió en la URI; el `nombre` no viene vacío.

**Conclusión técnica.** Esta es la prueba que valida el uso de la **variable de ruta**. Comparada con
la prueba 2, cambia únicamente un carácter de la URI (`/1` en vez de `/3`) y la respuesta es un
recurso distinto: `50 g` y `disponible: true` frente a `200 g` y `disponible: false`. Eso demuestra
que `@PathVariable` extrae el fragmento de la URI, lo convierte de texto a `Long` y ese valor llega
efectivamente hasta `buscarPorId`. Si el mapeo estuviera mal, la respuesta sería siempre el mismo
producto o un error de conversión.

---

## Prueba 4 — Consultar un ID inexistente

| Campo | Valor |
|---|---|
| **Objetivo** | Comprobar que un recurso inexistente produce un código HTTP coherente. |
| **Método / URL** | `GET http://localhost:8080/api/productos/999` |
| **JSON de entrada** | No aplica. |
| **Código esperado** | `404 Not Found` |
| **Código obtenido** | `404 Not Found` (16 ms, 264 B) |
| **Evidencia** | [`04-id-inexistente.png`](evidencias/04-id-inexistente.png) |

**Respuesta recibida:**

```json
{
  "estado": 404,
  "error": "Not Found",
  "mensaje": "No existe un producto con el identificador 999"
}
```

**Aserciones aprobadas (3/3):** código `404`; el código está por debajo de 500, es decir **no** es un
error del servidor; el cuerpo **no** contiene la propiedad `presentacion`, o sea no devuelve una
representación de producto.

**Conclusión técnica.** Es la prueba más importante del conjunto, porque es donde una API mal
construida falla. Tres resultados incorrectos habrían sido posibles y ninguno ocurrió: un `500`
(la excepción llegando cruda al contenedor), un `200` con cuerpo vacío (que le diría al cliente que
la consulta encontró algo), o un `200` con `null`. El `404` demuestra que la cadena
`buscarPorId` → `Optional.empty()` → `orElseThrow` → `ProductoNoEncontradoException` →
`@RestControllerAdvice` funciona completa. El cuerpo de error no es la representación del recurso
—no tiene `nombre` ni `presentacion`— sino una descripción de la situación, que es lo correcto: la
petición estaba bien formada, simplemente ese recurso no existe. La familia `4xx` comunica
exactamente eso: la responsabilidad es del cliente, no del servidor.

---

## Prueba 5 — Registrar un producto válido

| Campo | Valor |
|---|---|
| **Objetivo** | Comprobar que la API recibe un producto en JSON y comunica la creación. |
| **Método / URL** | `POST http://localhost:8080/api/productos` |
| **Cabecera** | `Content-Type: application/json` |
| **Código esperado** | `201 Created` + cabecera `Location` |
| **Código obtenido** | `201 Created` (137 ms, 306 B) + `Location: /api/productos/9` |
| **Evidencia** | [`05-registrar-producto.png`](evidencias/05-registrar-producto.png) |

**JSON de entrada** (nótese que **no lleva `id`**):

```json
{
  "nombre": "Cafe Soluble Selecto",
  "presentacion": "100 g",
  "categoria": "Linea premium",
  "disponible": true
}
```

**Respuesta recibida:**

```json
{ "id": 9, "nombre": "Cafe Soluble Selecto", "presentacion": "100 g", "categoria": "Linea premium", "disponible": true }
```

**Aserciones aprobadas (5/5):** código `201`; existe la cabecera `Location`; el `id` es un número
mayor que cero asignado por el servidor; los cuatro datos enviados se conservan intactos; **la
cabecera `Location` apunta al mismo `id` del recurso creado**.

**Conclusión técnica.** Se demuestra el recorrido inverso al de las consultas: Jackson toma el JSON
del cuerpo, instancia un `Producto` con el constructor vacío y lo puebla con los setters. El
servidor asignó el `id` `9`, que continúa la secuencia después de los ocho productos iniciales; el
cliente nunca lo envió. Esa es la decisión de diseño documentada en `docs/01`: la identidad del
recurso la controla el servidor. El `201` en lugar de `200` es lo que distingue "la petición
funcionó" de "la petición creó un recurso nuevo", y la cabecera `Location` le dice al cliente
exactamente dónde consultarlo, sin que tenga que deducir la URI por su cuenta.

---

## Prueba 6 — Consultar nuevamente la colección

| Campo | Valor |
|---|---|
| **Objetivo** | Comprobar que el producto registrado aparece en la colección. |
| **Método / URL** | `GET http://localhost:8080/api/productos` |
| **JSON de entrada** | No aplica. |
| **Código esperado** | `200 OK` con 9 productos, incluido el nuevo. |
| **Código obtenido** | `200 OK` (7 ms, 1.14 KB) |
| **Evidencia** | [`06-coleccion-actualizada.png`](evidencias/06-coleccion-actualizada.png) |

**Aserciones aprobadas (3/3):** código `200`; la colección tiene **9** elementos, uno más que en la
prueba 1; existe **exactamente un** producto con el `id` devuelto por la prueba 5, y su `nombre` es
`"Cafe Soluble Selecto"`.

**Conclusión técnica.** Cierra el ciclo completo de la API. El crecimiento de la respuesta de
1.04 KB a 1.14 KB, y de 8 a 9 elementos, confirma que el `POST` **modificó el estado del servidor**
de forma persistente durante la vida del proceso, y no solo devolvió un eco de lo enviado. Esto
verifica de paso una decisión de arquitectura: `AlmacenProductos` está anotado con `@Component`, así
que Spring mantiene **una sola instancia** compartida entre peticiones. Si se creara una instancia
nueva por petición, el producto registrado en la prueba 5 habría desaparecido en esta. También
confirma que `POST` no es idempotente: repetir la prueba 5 crearía un décimo producto con un `id`
distinto.

---

## Interpretación global

1. **La implementación coincide con la especificación inicial.** Las cuatro operaciones de la tabla
   de `docs/01` respondieron con la ruta, el método y el código de estado que se habían diseñado
   **antes** de programar. No hubo que cambiar ninguna ruta ni ningún código, así que no hay
   desviaciones que justificar en el README.

2. **Los tres códigos de estado hacen trabajo distinto y verificable.** `200` para consultas,
   `201` para creación con `Location`, `404` para recurso ausente. Las pruebas 4 y 5 son las que
   demuestran que no se está usando `200` para todo.

3. **La conversión entre objeto Java y JSON funciona en ambas direcciones.** Las pruebas 1 a 3
   verifican la salida (getters → JSON) y la prueba 5 verifica la entrada (JSON → constructor vacío
   + setters).

4. **El estado en memoria se comporta como se esperaba.** La prueba 6 confirma la instancia única
   de `AlmacenProductos`. Como no hay base de datos, al reiniciar la aplicación la colección vuelve
   a los 8 productos iniciales: el producto con `id` 9 desaparece.

5. **Ninguna prueba falló**, por lo que no hubo que abrir correcciones sobre el controlador.
