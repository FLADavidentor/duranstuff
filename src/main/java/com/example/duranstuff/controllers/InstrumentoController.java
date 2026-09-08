package com.example.duranstuff.controllers;

import com.example.duranstuff.dto.InstrumentoDTO;
import com.example.duranstuff.services.InstrumentoService;
import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/instrumentos")
public class InstrumentoController {

    private final InstrumentoService servicio;

    public InstrumentoController(InstrumentoService servicio) {
        this.servicio = servicio;
    }

    @GetMapping
    public ResponseEntity<List<InstrumentoDTO>> listar() {
        return ResponseEntity.ok(servicio.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable Long id) {
        return servicio.buscarPorId(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "Instrumento no encontrado")));
    }

    @PostMapping
    public ResponseEntity<InstrumentoDTO> registrar(@Valid @RequestBody InstrumentoDTO instrumento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.guardar(instrumento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable Long id, @Valid @RequestBody InstrumentoDTO instrumento) {
        return servicio.actualizar(id, instrumento)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("mensaje", "Instrumento no encontrado")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id) {
        if (!servicio.eliminar(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("mensaje", "Instrumento no encontrado"));
        }
        return ResponseEntity.ok(Map.of("mensaje", "Instrumento eliminado correctamente"));
    }
}
