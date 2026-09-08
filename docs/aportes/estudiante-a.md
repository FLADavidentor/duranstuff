# Aporte técnico — Estudiante A

**Responsabilidad asumida:** *Diseño técnico y modelado* (Integrante 1 del enunciado) y
*pruebas y documentación* (parte de la responsabilidad del Integrante 3, dividida por tratarse
de un equipo de dos).

**Ramas de trabajo:**

| Rama | Contenido | Integración |
|---|---|---|
| `feat/modelo-y-especificacion` | Especificación de la API y análisis técnico | Pull Request → `Semana3` |
| `test/pruebas-y-documentacion` | Pruebas de Postman, matriz de resultados y README | Pull Request → `Semana3` |

---

## Commits realizados

| Commit | Fase del taller | Aporte |
|---|---|---|
| `a45c4e0` | Fase 1 — Especificación | `docs/01-especificacion-api.md`: tabla completa de las cuatro operaciones con método, ruta, entrada, respuesta y código HTTP; ejemplos de JSON de petición y respuesta; tabla de los ocho productos con sus identificadores; correspondencia getter → propiedad JSON; justificación de cada código de estado y de los que se descartaron; contrato de la capa de datos que consume el controlador. |
| `666756a` | Fase 1 — Análisis | `docs/02-analisis-tecnico.md`: las cinco preguntas de análisis obligatorio respondidas y ancladas al código real del repositorio (`@RequestMapping`, `@PathVariable`, `registrar()` sobrescribiendo el `id`, `@RestControllerAdvice`). |
| *(colección)* | Fase 4 — Pruebas | `postman/catalogo-productos.postman_collection.json`: los seis escenarios del enunciado, cada uno con aserciones automáticas y variables de colección encadenadas (la prueba 6 verifica el `id` que devolvió la prueba 5). |
| `085cf4a` | Fase 4 — Pruebas | `docs/03-matriz-pruebas.md` y `docs/evidencias/`: ejecución de las seis pruebas contra la aplicación corriendo, con código obtenido, respuesta recibida, aserciones aprobadas y **conclusión técnica** por escenario, más la interpretación global. Seis capturas de pantalla como evidencia. |
| *(README)* | Cierre | `README.md` consolidado y este documento de aporte. |

---

## Decisiones de diseño que puedo defender

**Por qué la especificación se escribió antes de programar.** El enunciado lo pide explícitamente,
pero además tiene una razón práctica: el contrato de la sección 4 de `docs/01` es lo que permitió
que los dos estudiantes trabajaran en paralelo. La colección de Postman se construyó **desde el
contrato**, no desde el código, y por eso pudo escribirse antes de que el controlador existiera.

**Por qué `buscarPorId` devuelve `Optional` y no `null`.** Un `null` se puede ignorar sin que el
compilador diga nada; un `Optional` obliga al controlador a decidir explícitamente qué hacer cuando
el recurso no está. Ese punto de decisión es exactamente el que produce el `404` en lugar de una
`NullPointerException` que terminaría en `500`. La prueba 4 de la matriz lo verifica.

**Por qué el identificador lo asigna el servidor.** El `id` forma parte de la identidad del recurso,
y esa identidad solo la puede garantizar como única quien la controla: el servidor. Por eso el JSON
de entrada de la prueba 5 no lleva `id`, y por eso el almacén sobrescribe cualquiera que llegue.
La respuesta devuelve el recurso ya identificado y la cabecera `Location` con su URI definitiva.

**Por qué una colección vacía es `200` y no `404`.** La colección existe aunque no tenga elementos;
lo que no existiría en un `404` es el recurso solicitado. Distinguir esos dos casos es lo que
diferencia una URI de colección de una URI de recurso individual.

**Por qué las pruebas llevan aserciones y no solo capturas.** El enunciado advierte que "no basta
con colocar capturas". Con `pm.test` la verificación es reproducible: cualquiera puede volver a
correr la colección y obtener el mismo veredicto. Las capturas documentan una ejecución concreta;
las aserciones documentan el criterio.

---

## Resultado de la verificación

Las seis pruebas se ejecutaron en una sola corrida sobre la aplicación recién levantada.
**22 de 22 aserciones aprobadas.** Ninguna operación se desvió de lo especificado en la Fase 1, por
lo que no hubo cambios de ruta ni de código de estado que justificar.

El detalle está en [`docs/03-matriz-pruebas.md`](../03-matriz-pruebas.md).

---

## Nota sobre el commit `de6ffa2`

El recurso `Producto` y `AlmacenProductos` corresponden a esta responsabilidad según el reparto de
roles, pero los commiteó el Estudiante B como base compartida al inicio del trabajo, para no
bloquear la escritura del controlador. Está documentado desde ambos lados: ver la nota de
atribución en [`estudiante-b.md`](estudiante-b.md).

El diseño de ese modelo —qué atributos tiene el recurso, por qué el `id` lo asigna el servidor, por
qué la búsqueda devuelve `Optional`, cómo se corresponden los getters con las propiedades JSON—
está documentado en `docs/01` y es aporte del Estudiante A. Ambos estudiantes pueden defender esas
clases.
