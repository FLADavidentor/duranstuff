package ni.edu.uam.gestion_productos.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ni.edu.uam.gestion_productos.dto.ProveedorRequestDTO;
import ni.edu.uam.gestion_productos.entity.Proveedor;
import ni.edu.uam.gestion_productos.service.ProveedorService;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    private final ProveedorService proveedorService;

    public ProveedorController(ProveedorService proveedorService) {
        this.proveedorService = proveedorService;
    }

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorService.listar();
    }

    @GetMapping("/{id}")
    public Proveedor buscar(@PathVariable Integer id) {
        return proveedorService.buscarPorId(id);
    }

    @PostMapping
    public Proveedor guardar(@RequestBody ProveedorRequestDTO dto) {
        return proveedorService.guardar(dto);
    }
}
