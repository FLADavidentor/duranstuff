package com.example.duranstuff.error;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Traduce las excepciones de la aplicacion a codigos de estado HTTP.
 *
 * <p>{@code @RestControllerAdvice} registra esta clase para todos los controladores REST.
 * Sin ella, una excepcion no controlada llegaria al contenedor y la API respondaria 500,
 * que significa "el servidor fallo". Pedir un producto que no existe no es un fallo del
 * servidor: es una peticion bien formada sobre un recurso ausente, y eso se comunica con
 * 404 Not Found.</p>
 */
@RestControllerAdvice
public class ManejadorDeErrores {

    @ExceptionHandler(ProductoNoEncontradoException.class)
    public ResponseEntity<Map<String, Object>> manejarProductoNoEncontrado(ProductoNoEncontradoException ex) {
        Map<String, Object> cuerpo = new LinkedHashMap<>();
        cuerpo.put("estado", HttpStatus.NOT_FOUND.value());
        cuerpo.put("error", HttpStatus.NOT_FOUND.getReasonPhrase());
        cuerpo.put("mensaje", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(cuerpo);
    }
}
