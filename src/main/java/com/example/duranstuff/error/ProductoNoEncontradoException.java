package com.example.duranstuff.error;

/**
 * Se lanza cuando se solicita un producto cuyo identificador no existe en el almacen.
 *
 * <p>Existe para separar responsabilidades: el controlador dice "este recurso no esta",
 * y el manejador de errores traduce esa situacion al codigo HTTP 404. Sin esta clase la
 * excepcion llegaria cruda al contenedor y la API respondaria 500, que significa "fallo
 * del servidor" y no "el recurso no existe".</p>
 */
public class ProductoNoEncontradoException extends RuntimeException {

    public ProductoNoEncontradoException(Long id) {
        super("No existe un producto con el identificador " + id);
    }
}
