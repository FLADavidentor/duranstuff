package com.example.duranstuff;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicacion Spring Boot.
 *
 * <p>La anotacion {@code @SpringBootApplication} activa tres cosas a la vez: marca la clase
 * como fuente de configuracion, habilita la autoconfiguracion (Spring detecta que hay un
 * starter web en el classpath y levanta un Tomcat embebido con el DispatcherServlet ya
 * registrado) y activa el escaneo de componentes a partir de este paquete, que es como
 * encuentra al controlador y al almacen de productos.</p>
 *
 * <p>{@code SpringApplication.run} arranca ese contexto y deja el servidor escuchando en el
 * puerto 8080. Este taller no usa base de datos: los productos viven en memoria.</p>
 */
@SpringBootApplication
public class DuranstuffApplication {

    public static void main(String[] args) {
        SpringApplication.run(DuranstuffApplication.class, args);
    }
}
