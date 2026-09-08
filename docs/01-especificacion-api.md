# 01 — Especificación técnica de la API

**Taller #1 · Servicios Web · Spring Boot**
Caso académico **ficticio**: Café Soluble S.A.

> Corresponde a la **Fase 1** del enunciado: la especificación de los endpoints acordada por el
> equipo antes de programar. Es el contrato contra el que se implementa y contra el que se prueba.
> Si el equipo cambia una ruta, un método o una respuesta, debe actualizarse aquí y justificarse
> en el `README.md`.

---

## 1. Recurso modelado

El recurso de esta API es **`Producto`**. Su URI de colección es `/api/productos`.

| Atributo | Tipo Java | Tipo JSON | Descripción | Ejemplo |
|---|---|---|---|---|
| `id` | `Long` | número | Identificador único. Lo asigna **el servidor**, nunca el cliente. | `3` |
| `nombre` | `String` | cadena | Nombre ficticio del producto. | `"Cafe Soluble Clasico"` |
| `presentacion` | `String` | cadena | Contenido del empaque. | `"200 g"` |
| `categoria` | `String` | cadena | Categoría ficticia del producto. | `"Cafe instantaneo"` |
| `disponible` | `boolean` | booleano | Indica si se encuentra disponible. | `false` |

Representación JSON de un producto:

```json
{
  "id": 3,
  "nombre": "Cafe Soluble Clasico",
  "presentacion": "200 g",
  "categoria": "Cafe instantaneo",
  "disponible": false
}
```

### 1.1 Cómo se transforma el objeto Java en JSON

Jackson, que viene incluido con el starter web, construye el JSON a partir de los **métodos de
acceso públicos** de la clase `Producto`, no de sus campos privados:

| Método en `Producto.java` | Propiedad JSON |
|---|---|
| `getId()` | `id` |
| `getNombre()` | `nombre` |
| `getPresentacion()` | `presentacion` |
| `getCategoria()` | `categoria` |
| `isDisponible()` | `disponible` |

Para el camino inverso —el JSON que llega en el cuerpo de un `POST`— Jackson usa el **constructor
vacío** y luego los **setters**. Por eso `Producto` declara los dos constructores.

---

## 2. Datos cargados en memoria

El enunciado exige un mínimo de ocho productos ficticios y **prohíbe usar base de datos**. Los
productos viven en una lista dentro de `AlmacenProductos`, que Spring instancia una sola vez.

| id | nombre | presentacion | categoria | disponible |
|---|---|---|---|---|
| 1 | Cafe Soluble Clasico | 50 g | Cafe instantaneo | `true` |
| 2 | Cafe Soluble Clasico | 100 g | Cafe instantaneo | `true` |
| 3 | Cafe Soluble Clasico | 200 g | Cafe instantaneo | `false` |
| 4 | Cafe Soluble Descafeinado | 50 g | Descafeinado | `true` |
| 5 | Cafe Soluble Descafeinado | 100 g | Descafeinado | `false` |
| 6 | Cafe Soluble Intenso | 100 g | Cafe intenso | `true` |
| 7 | Cafe Soluble con Leche | 200 g | Mezcla lista | `true` |
| 8 | Cafe Soluble Premium | 200 g | Linea premium | `true` |

Al detener la aplicación los datos se pierden y al volver a arrancarla se recargan estos ocho.

---

## 3. Convenciones fijas

| Elemento | Valor |
|---|---|
| Base URL en desarrollo | `http://localhost:8080` |
| Ruta base del recurso | `/api/productos` |
| Formato de intercambio | `application/json` |
| Persistencia | Lista en memoria (**sin base de datos**) |
| Paquete base | `com.example.duranstuff` |
| Puerto | `8080` |

---

## 4. Tabla de endpoints (Fase 1)

| # | Operación | Método HTTP | Ruta | Entrada | Respuesta esperada | Código HTTP |
|---|---|---|---|---|---|---|
| 1 | Consultar todos los productos | `GET` | `/api/productos` | No requiere cuerpo. | Colección JSON con todos los productos. | `200 OK` |
| 2 | Consultar producto por ID | `GET` | `/api/productos/{id}` | Variable de ruta `id`. Sin cuerpo. | Un único objeto JSON `Producto`. | `200 OK` |
| 3 | Registrar producto | `POST` | `/api/productos` | Objeto `Producto` en JSON en el cuerpo. | El recurso creado con su `id` asignado, más la cabecera `Location`. | `201 Created` |
| 4 | Consultar producto inexistente | `GET` | `/api/productos/{id}` | Variable de ruta con un `id` que no existe. | Sin representación del recurso; un cuerpo de error explicando la situación. | `404 Not Found` |

### 4.1 Detalle por operación

**1) `GET /api/productos` — consultar la colección**

Petición sin cuerpo ni parámetros. Respuesta `200 OK` con un arreglo JSON:

```json
[
  { "id": 1, "nombre": "Cafe Soluble Clasico", "presentacion": "50 g", "categoria": "Cafe instantaneo", "disponible": true },
  { "id": 2, "nombre": "Cafe Soluble Clasico", "presentacion": "100 g", "categoria": "Cafe instantaneo", "disponible": true }
]
```

Una colección vacía **no** es un error: se responde `200 OK` con `[]`, porque la colección existe
aunque no tenga elementos.

**2) `GET /api/productos/{id}` — consultar un recurso individual**

`GET http://localhost:8080/api/productos/3` responde `200 OK` con **un objeto**, no un arreglo:

```json
{ "id": 3, "nombre": "Cafe Soluble Clasico", "presentacion": "200 g", "categoria": "Cafe instantaneo", "disponible": false }
```

El `3` de la ruta es una **variable de URI**: Spring la extrae con `@PathVariable` y la convierte
a `Long` antes de pasarla al método.

**3) `POST /api/productos` — registrar un producto**

Cuerpo enviado por el cliente, con `Content-Type: application/json` y **sin `id`**:

```json
{
  "nombre": "Cafe Soluble Selecto",
  "presentacion": "100 g",
  "categoria": "Linea premium",
  "disponible": true
}
```

Respuesta `201 Created`, con la cabecera `Location: /api/productos/9` y el recurso ya creado:

```json
{ "id": 9, "nombre": "Cafe Soluble Selecto", "presentacion": "100 g", "categoria": "Linea premium", "disponible": true }
```

**Decisión de diseño:** el `id` lo asigna el servidor. Si el cliente envía uno, se descarta. El
identificador forma parte de la identidad del recurso, y esa identidad la controla quien puede
garantizar que sea única.

**4) `GET /api/productos/{id}` con `id` inexistente**

`GET http://localhost:8080/api/productos/999` responde `404 Not Found`. No se devuelve ninguna
representación de producto; en su lugar viaja un cuerpo de error que describe qué pasó:

```json
{
  "estado": 404,
  "error": "Not Found",
  "mensaje": "No existe un producto con el identificador 999"
}
```

**Decisión de diseño:** no se responde `200 OK` con cuerpo vacío, porque eso le diría al cliente
que la consulta encontró algo. Tampoco `500`, que significaría que la aplicación falló: acá la
aplicación funcionó perfectamente y la respuesta correcta es "ese recurso no existe".

---

## 5. Códigos de estado usados

| Código | Cuándo se usa | Qué le comunica al cliente |
|---|---|---|
| `200 OK` | Operaciones 1 y 2 | La consulta se procesó y la representación viaja en el cuerpo. |
| `201 Created` | Operación 3 | Se creó un recurso nuevo; su ubicación viaja en `Location`. |
| `404 Not Found` | Operación 4 | La URI es válida pero no identifica ningún recurso existente. |

Códigos que **no** se usan y por qué: `204 No Content` (toda respuesta exitosa aquí lleva
representación), `400 Bad Request` (este taller no pide validación de entrada) y `500` (indicaría
una falla del servidor, no un caso previsto por el contrato).

---

## 6. Trazabilidad: dónde vive cada parte del contrato

| Elemento de la especificación | Archivo |
|---|---|
| Recurso `Producto` y su mapeo a JSON | `src/main/java/com/example/duranstuff/modelo/Producto.java` |
| Ocho productos y almacenamiento en memoria | `src/main/java/com/example/duranstuff/datos/AlmacenProductos.java` |
| Operaciones 1, 2 y 3 | `src/main/java/com/example/duranstuff/controlador/ProductoControlador.java` |
| Traducción del recurso ausente a `404` | `error/ProductoNoEncontradoException.java` + `error/ManejadorDeErrores.java` |
| Verificación de las cuatro operaciones | `postman/catalogo-productos.postman_collection.json`, `docs/03-matriz-pruebas.md` |

### 6.1 Contrato de la capa de datos

El controlador no manipula la lista: la consume a través de `AlmacenProductos`, que Spring le
inyecta por constructor.

| Método | Comportamiento |
|---|---|
| `List<Producto> listarTodos()` | Copia de la colección completa. Nunca `null`. |
| `Optional<Producto> buscarPorId(Long id)` | `Optional.empty()` si el recurso no existe. Ese vacío es lo que el controlador convierte en `404`. |
| `Producto registrar(Producto producto)` | Asigna el siguiente `id` disponible y devuelve el producto ya identificado. |

> **Por qué `Optional` y no `null`:** obliga al controlador a decidir explícitamente qué hacer
> cuando el recurso no existe. Ese punto de decisión es exactamente el que produce el `404`.

---

## 7. Coherencia entre la especificación y la implementación

Las cuatro operaciones de la tabla de la sección 4 están implementadas tal como se especificaron:
mismas rutas, mismos métodos, mismos códigos de estado. **No hubo desviaciones respecto al diseño
inicial**, por lo que no hay cambios que justificar en el README.

La verificación de esta coherencia está en `docs/03-matriz-pruebas.md`, donde cada escenario compara
el resultado esperado con el obtenido en Postman.
