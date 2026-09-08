package com.example.duranstuff.modelo;

/**
 * Recurso Producto del catalogo.
 *
 * <p>Es un objeto Java plano: no tiene anotaciones de persistencia porque en este taller
 * los datos viven en memoria. Los metodos getXxx son los que Jackson usa para construir
 * el JSON de la respuesta, y los setXxx los que usa para reconstruir el objeto a partir
 * del JSON que llega en el cuerpo de una peticion POST.</p>
 */
public class Producto {

    private Long id;
    private String nombre;
    private String presentacion;
    private String categoria;
    private boolean disponible;

    /** Constructor vacio: lo necesita Jackson para instanciar el objeto antes de poblarlo. */
    public Producto() {
    }

    public Producto(Long id, String nombre, String presentacion, String categoria, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.categoria = categoria;
        this.disponible = disponible;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPresentacion() {
        return presentacion;
    }

    public void setPresentacion(String presentacion) {
        this.presentacion = presentacion;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }
}
