package ni.edu.uam.gestion_productos.service;

import java.util.List;

import org.springframework.stereotype.Service;

import ni.edu.uam.gestion_productos.dto.EtiquetaRequestDTO;
import ni.edu.uam.gestion_productos.entity.Etiqueta;
import ni.edu.uam.gestion_productos.repository.EtiquetaRepository;

@Service
public class EtiquetaService {

    private final EtiquetaRepository etiquetaRepository;

    public EtiquetaService(EtiquetaRepository etiquetaRepository) {
        this.etiquetaRepository = etiquetaRepository;
    }

    public List<Etiqueta> listar() {
        return etiquetaRepository.findAll();
    }

    public Etiqueta buscarPorId(Integer id) {
        return etiquetaRepository.findById(id)
                .orElseThrow(()
                        -> new RuntimeException("Etiqueta no encontrada"));
    }

    public Etiqueta guardar(EtiquetaRequestDTO dto) {

        Etiqueta etiqueta = new Etiqueta();

        etiqueta.setNombre(dto.getNombre());

        return etiquetaRepository.save(etiqueta);
    }
}
