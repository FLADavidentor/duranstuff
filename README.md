# API de catálogo de productos — Taller #1

**Servicios Web · Spring Boot · UAM**
Caso de estudio **ficticio**: Café Soluble S.A.

API REST que permite consultar y registrar productos de un catálogo. Los datos permanecen **en
memoria**: el taller prohíbe expresamente el uso de una base de datos.

> **Nota sobre el caso.** El nombre *Café Soluble S.A.* se utiliza únicamente para contextualizar el
> aprendizaje en un entorno empresarial nicaragüense. La situación, los requerimientos, los datos y
> la API son ficticios y fueron creados con fines exclusivamente académicos.

> ⚠️ **Equipo de 2 integrantes.** El enunciado establece equipos de 3. Este equipo trabajó con 2
> personas **con autorización del docente**. No se recortó ningún entregable: las tres
> responsabilidades del enunciado se repartieron entre los dos estudiantes según la tabla de la
> sección [Aporte de cada estudiante](#aporte-de-cada-estudiante).

---

## Cómo ejecutar

**Requisitos:** JDK 17, IntelliJ IDEA. No hace falta base de datos ni configuración adicional.

```bash
./mvnw spring-boot:run          # Linux / macOS
.\mvnw.cmd spring-boot:run      # Windows
```

Desde IntelliJ: abrir el proyecto y ejecutar `DuranstuffApplication`.

La aplicación queda escuchando en **`http://localhost:8080`** y carga automáticamente 8 productos
en memoria. Al detenerla, los datos se pierden y al volver a arrancarla se recargan los mismos 8.

---

## Especificación de endpoints

| # | Operación | Método HTTP | Ruta | Entrada | Respuesta esperada | Código HTTP |
|---|---|---|---|---|---|---|
| 1 | Consultar todos los productos | `GET` | `/api/productos` | No requiere cuerpo. | Colección JSON con todos los productos. | `200 OK` |
| 2 | Consultar producto por ID | `GET` | `/api/productos/{id}` | Variable de ruta `id`. | Un único objeto JSON. | `200 OK` |
| 3 | Registrar producto | `POST` | `/api/productos` | Objeto `Producto` en JSON en el cuerpo. | Recurso creado con su `id`, más la cabecera `Location`. | `201 Created` |
| 4 | Consultar producto inexistente | `GET` | `/api/productos/{id}` | Variable de ruta con un `id` que no existe. | Cuerpo de error, sin representación del recurso. | `404 Not Found` |

Especificación completa, con ejemplos de JSON y el detalle de cada operación:
**[`docs/01-especificacion-api.md`](docs/01-especificacion-api.md)**

### Recurso `Producto`

| Atributo | Tipo | Descripción |
|---|---|---|
| `id` | `Long` | Identificador único. Lo asigna el servidor. |
| `nombre` | `String` | Nombre ficticio del producto. |
| `presentacion` | `String` | Contenido del empaque (`50 g`, `100 g`, `200 g`). |
| `categoria` | `String` | Categoría ficticia. |
| `disponible` | `boolean` | Indica si se encuentra disponible. |

```json
{
  "id": 3,
  "nombre": "Cafe Soluble Clasico",
  "presentacion": "200 g",
  "categoria": "Cafe instantaneo",
  "disponible": false
}
```

---

## Justificación técnica

**Las rutas usan sustantivos, no acciones.** `/api/productos` identifica un recurso, no una
operación. La acción la expresa el método HTTP, y por eso `GET` y `POST` comparten la misma URI: es
el mismo recurso, distinta operación. Poner el verbo en la ruta duplicaría esa información y
rompería la uniformidad de la interfaz.

**La URI lleva identidad; el JSON lleva contenido.** El `id` viaja en la ruta porque forma parte de
la dirección del recurso; `nombre`, `presentacion`, `categoria` y `disponible` viajan en el cuerpo
porque son su estado. Por eso `GET /api/productos/3` no lleva cuerpo y `POST /api/productos` no
lleva identificador en la ruta.

**El identificador lo asigna el servidor.** `AlmacenProductos` sobrescribe cualquier `id` que venga
en el JSON de entrada y asigna el siguiente de un contador atómico. La identidad de un recurso solo
la puede garantizar como única quien la controla. El identificador asignado se comunica de vuelta
en el cuerpo de la respuesta y en la cabecera `Location`.

**Tres códigos de estado con significados distintos.** `200 OK` para consultas exitosas, `201
Created` para la creación —que además informa *dónde* quedó el recurso mediante `Location`—, y
`404 Not Found` para un identificador que no corresponde a ningún recurso. Un `404` **no** es un
error del servidor: la aplicación lanza `ProductoNoEncontradoException` y un `@RestControllerAdvice`
la traduce a `404`. Sin esa traducción la excepción llegaría cruda al contenedor y la API
respondería `500`, que significaría que la aplicación falló, y sería falso.

**Los datos viven en una única instancia en memoria.** `AlmacenProductos` está anotado con
`@Component`, así que Spring mantiene un solo objeto compartido entre peticiones. Esa unicidad es
lo que hace que un producto registrado con `POST` siga apareciendo en el `GET` siguiente. La lista
está sincronizada y el contador de identificadores es atómico porque Tomcat atiende cada petición
en un hilo distinto.

**Coherencia entre especificación e implementación.** Las cuatro operaciones se diseñaron y
documentaron **antes** de programar, y la implementación final coincide con la tabla original:
mismas rutas, mismos métodos, mismos códigos de estado. **No hubo desviaciones**, por lo que no hay
cambios que justificar en este apartado.

---

## Pruebas

Colección: **[`postman/catalogo-productos.postman_collection.json`](postman/catalogo-productos.postman_collection.json)**
Matriz completa: **[`docs/03-matriz-pruebas.md`](docs/03-matriz-pruebas.md)**
Evidencias: **[`docs/evidencias/`](docs/evidencias/)**

Los seis escenarios se ejecutaron en una sola corrida contra la aplicación recién levantada.
Cada petición incluye aserciones automáticas, de modo que la verificación es reproducible y no
depende de interpretar una captura.

| # | Escenario | Esperado | Obtenido | Aserciones |
|---|---|---|---|---|
| 1 | Consultar la colección completa | `200 OK` | `200 OK`, 8 productos | 5/5 ✅ |
| 2 | Consultar un ID existente | `200 OK` | `200 OK`, un objeto | 3/3 ✅ |
| 3 | Consultar el primer ID existente | `200 OK` | `200 OK`, `id: 1` | 3/3 ✅ |
| 4 | Consultar un ID inexistente | `404 Not Found` | `404 Not Found` | 3/3 ✅ |
| 5 | Registrar un producto válido | `201 Created` | `201 Created`, `Location: /api/productos/9` | 5/5 ✅ |
| 6 | Consultar nuevamente la colección | `200 OK` con el nuevo | `200 OK`, 9 productos | 3/3 ✅ |

**Total: 22 de 22 aserciones aprobadas.**

---

## Arquitectura

Diagrama: **[`docs/diagrama/arquitectura.drawio`](docs/diagrama/arquitectura.drawio)** — abrir en
<https://app.diagrams.net>
Explicación: **[`docs/04-arquitectura.md`](docs/04-arquitectura.md)**

Representa los dos recorridos exigidos por el enunciado: una consulta exitosa
(`GET /api/productos/3` → `200 OK`) y la solicitud de un producto inexistente
(`GET /api/productos/999` → `404 Not Found`), con la explicación de en qué punto se separan.

---

## Estructura del repositorio

```
├── pom.xml                                          configuración Maven (solo Spring Web)
├── README.md
├── docs/
│   ├── 01-especificacion-api.md                     especificación de los 4 endpoints
│   ├── 02-analisis-tecnico.md                       las 5 preguntas de análisis obligatorio
│   ├── 03-matriz-pruebas.md                         matriz de pruebas con resultados
│   ├── 04-arquitectura.md                           explicación del diagrama
│   ├── diagrama/arquitectura.drawio                 diagrama con los dos recorridos
│   ├── evidencias/                                  capturas de las 6 pruebas
│   └── aportes/                                     aporte técnico de cada estudiante
├── postman/
│   └── catalogo-productos.postman_collection.json   6 escenarios con aserciones
└── src/main/
    ├── java/com/example/duranstuff/
    │   ├── DuranstuffApplication.java               clase principal de Spring Boot
    │   ├── modelo/Producto.java                     el recurso
    │   ├── datos/AlmacenProductos.java              8 productos en memoria
    │   ├── controlador/ProductoControlador.java     las 4 operaciones REST
    │   └── error/                                   traducción de excepciones a códigos HTTP
    └── resources/application.properties             puerto 8080, sin datasource
```

---

## Aporte de cada estudiante

| Responsabilidad del enunciado | Quién la asumió |
|---|---|
| Integrante 1 — Diseño técnico y modelado | **Estudiante A** |
| Integrante 2 — Implementación de endpoints | **Estudiante B** |
| Integrante 3 — Pruebas, documentación y arquitectura | **dividida**: pruebas y documentación → A; arquitectura → B |

### Estudiante A — [detalle](docs/aportes/estudiante-a.md)

Especificación técnica de la API (`docs/01`), análisis obligatorio de las cinco preguntas
(`docs/02`), colección de Postman con los seis escenarios y sus aserciones, ejecución de las
pruebas, matriz de resultados con conclusión técnica por escenario (`docs/03`), evidencias y este
README.

Ramas: `feat/modelo-y-especificacion`, `test/pruebas-y-documentacion`.

### Estudiante B — [detalle](docs/aportes/estudiante-b.md)

Configuración del proyecto Spring Boot sin base de datos, recurso `Producto` y `AlmacenProductos`
con los ocho productos, `ProductoControlador` con las cuatro operaciones, manejo del `404` mediante
excepción propia y `@RestControllerAdvice`, diagrama de arquitectura y su explicación (`docs/04`).

Ramas: `docs/aporte-estudiante-b`, `docs/arquitectura`.

> **Nota de atribución.** El recurso `Producto` y `AlmacenProductos` corresponden al Estudiante A
> según el reparto, pero los commiteó el Estudiante B al inicio como base compartida: sin la clase
> `Producto` compilando, el controlador no podía escribirse y los dos habrían trabajado en serie.
> El diseño de ese modelo está documentado por el Estudiante A en `docs/01`. Ambos estudiantes
> pueden defender esas clases.

---

## Entregables

| # | Entregable | Dónde está |
|---|---|---|
| 1 | Enlace público del repositorio | este repositorio, rama `Semana3` |
| 2 | Proyecto Spring Boot funcional en IntelliJ IDEA | raíz del repositorio |
| 3 | README con especificación de endpoints y justificación técnica | este archivo |
| 4 | Evidencia de la matriz de pruebas de Postman | `docs/03-matriz-pruebas.md`, `docs/evidencias/` |
| 5 | Diagrama de arquitectura con los dos recorridos | `docs/diagrama/arquitectura.drawio`, `docs/04-arquitectura.md` |
| 6 | Historial de commits de los integrantes | historial de la rama `Semana3` y sus Pull Requests |
| 7 | Descripción del aporte técnico de cada integrante | `docs/aportes/` y la sección anterior |
