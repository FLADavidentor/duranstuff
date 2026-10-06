package ni.edu.uam.gestion_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ni.edu.uam.gestion_productos.dto.EtiquetaRequestDTO;
import ni.edu.uam.gestion_productos.entity.Etiqueta;
import ni.edu.uam.gestion_productos.service.EtiquetaService;

@RestController
@RequestMapping("/api/etiquetas")
public class EtiquetaController {

    private final EtiquetaService etiquetaService;

    public EtiquetaController(EtiquetaService etiquetaService) {
        this.etiquetaService = etiquetaService;
    }

    @GetMapping
    public List<Etiqueta> listar() {
        return etiquetaService.listar();
    }

    @GetMapping("/{id}")
    public Etiqueta buscar(@PathVariable Integer id) {
        return etiquetaService.buscarPorId(id);
    }

    @PostMapping
    public Etiqueta guardar(@RequestBody EtiquetaRequestDTO dto) {
        return etiquetaService.guardar(dto);
    }
}
