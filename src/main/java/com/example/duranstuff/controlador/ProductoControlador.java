package com.example.duranstuff.controlador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.duranstuff.datos.AlmacenProductos;
import com.example.duranstuff.error.ProductoNoEncontradoException;
import com.example.duranstuff.modelo.Producto;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    private final AlmacenProductos almacen;

    public ProductoControlador(AlmacenProductos almacen) {
        this.almacen = almacen;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> consultarTodos() {
        return ResponseEntity.ok(almacen.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Producto> consultarPorId(@PathVariable Long id) {
        Producto producto = almacen.buscarPorId(id)
                .orElseThrow(() -> new ProductoNoEncontradoException(id));
        return ResponseEntity.ok(producto);
    }
}
