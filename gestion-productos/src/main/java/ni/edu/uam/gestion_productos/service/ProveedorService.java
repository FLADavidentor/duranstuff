package ni.edu.uam.gestion_productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ni.edu.uam.gestion_productos.dto.ProveedorRequestDTO;
import ni.edu.uam.gestion_productos.entity.Proveedor;
import ni.edu.uam.gestion_productos.repository.ProveedorRepository;

@Service
public class ProveedorService {

    private final ProveedorRepository proveedorRepository;

    public ProveedorService(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    public List<Proveedor> listar() {
        return proveedorRepository.findAll();
    }

    public Proveedor buscarPorId(Integer id) {
        return proveedorRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Proveedor no encontrado"));
    }

    public Proveedor guardar(ProveedorRequestDTO dto) {

        Proveedor proveedor = new Proveedor();

        proveedor.setNombre(dto.getNombre());
        proveedor.setTelefono(dto.getTelefono());
        proveedor.setCorreo(dto.getCorreo());
        proveedor.setActivo(dto.isActivo());

        return proveedorRepository.save(proveedor);
    }
}
