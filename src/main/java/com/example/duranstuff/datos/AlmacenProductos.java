package com.example.duranstuff.datos;

import com.example.duranstuff.modelo.Producto;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Component;

/**
 * Almacen de productos en memoria.
 *
 * <p>El taller prohibe usar base de datos, asi que la coleccion vive en una lista dentro
 * del proceso. Al ser un componente de Spring existe una sola instancia compartida por
 * toda la aplicacion, por eso un producto registrado con POST sigue estando disponible
 * en la siguiente consulta GET.</p>
 *
 * <p>Se usa una lista sincronizada y un contador atomico porque Tomcat atiende cada
 * peticion en un hilo distinto y dos peticiones simultaneas podrian corromper la lista
 * o repetir un identificador.</p>
 */
@Component
public class AlmacenProductos {

    private final List<Producto> productos = java.util.Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong siguienteId = new AtomicLong();

    public AlmacenProductos() {
        cargarDatosIniciales();
    }

    /** Carga los ocho productos ficticios exigidos por la Fase 3 del taller. */
    private void cargarDatosIniciales() {
        registrar(new Producto(null, "Cafe Soluble Clasico", "50 g", "Cafe instantaneo", true));
        registrar(new Producto(null, "Cafe Soluble Clasico", "100 g", "Cafe instantaneo", true));
        registrar(new Producto(null, "Cafe Soluble Clasico", "200 g", "Cafe instantaneo", false));
        registrar(new Producto(null, "Cafe Soluble Descafeinado", "50 g", "Descafeinado", true));
        registrar(new Producto(null, "Cafe Soluble Descafeinado", "100 g", "Descafeinado", false));
        registrar(new Producto(null, "Cafe Soluble Intenso", "100 g", "Cafe intenso", true));
        registrar(new Producto(null, "Cafe Soluble con Leche", "200 g", "Mezcla lista", true));
        registrar(new Producto(null, "Cafe Soluble Premium", "200 g", "Linea premium", true));
    }

    /** Devuelve una copia de la coleccion completa para que nadie la modifique por fuera. */
    public List<Producto> listarTodos() {
        synchronized (productos) {
            return new ArrayList<>(productos);
        }
    }

    /**
     * Busca un producto por su identificador.
     *
     * @return el producto envuelto en un Optional, vacio si ese id no existe. Es el
     *         controlador quien decide que codigo HTTP corresponde a cada caso.
     */
    public Optional<Producto> buscarPorId(Long id) {
        synchronized (productos) {
            return productos.stream()
                    .filter(producto -> producto.getId().equals(id))
                    .findFirst();
        }
    }

    /**
     * Guarda un producto nuevo asignandole el siguiente identificador disponible.
     *
     * <p>El id lo asigna el servidor, no el cliente: por eso se ignora cualquier id que
     * venga en el JSON de entrada.</p>
     */
    public Producto registrar(Producto producto) {
        producto.setId(siguienteId.incrementAndGet());
        productos.add(producto);
        return producto;
    }
}
