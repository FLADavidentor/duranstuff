package ni.edu.uam.gestion_productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ni.edu.uam.gestion_productos.dto.CategoriaRequestDTO;
import ni.edu.uam.gestion_productos.entity.Categoria;
import ni.edu.uam.gestion_productos.repository.CategoriaRepository;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Integer id) {
        return categoriaRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Categoría no encontrada"));
    }

    public Categoria guardar(CategoriaRequestDTO dto) {

        Categoria categoria = new Categoria();

        categoria.setNombre(dto.getNombre());
        categoria.setActiva(dto.isActiva());

        return categoriaRepository.save(categoria);
    }
}
