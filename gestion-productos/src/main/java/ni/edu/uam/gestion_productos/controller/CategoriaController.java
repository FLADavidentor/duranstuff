package ni.edu.uam.gestion_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ni.edu.uam.gestion_productos.dto.CategoriaRequestDTO;
import ni.edu.uam.gestion_productos.entity.Categoria;
import ni.edu.uam.gestion_productos.service.CategoriaService;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public List<Categoria> listar() {
        return categoriaService.listar();
    }

    @GetMapping("/{id}")
    public Categoria buscar(@PathVariable Integer id) {
        return categoriaService.buscarPorId(id);
    }

    @PostMapping
    public Categoria guardar(@RequestBody CategoriaRequestDTO dto) {
        return categoriaService.guardar(dto);
    }
}
