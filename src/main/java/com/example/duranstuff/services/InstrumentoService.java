package com.example.duranstuff.services;

import com.example.duranstuff.dto.InstrumentoDTO;
import com.example.duranstuff.entity.Instrumento;
import com.example.duranstuff.repository.InstrumentoRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class InstrumentoService {

    private final InstrumentoRepository repositorio;

    public InstrumentoService(InstrumentoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public List<InstrumentoDTO> listar() {
        return repositorio.findAll().stream().map(this::aDTO).toList();
    }

    public Optional<InstrumentoDTO> buscarPorId(Long id) {
        return repositorio.findById(id).map(this::aDTO);
    }

    public InstrumentoDTO guardar(InstrumentoDTO datos) {
        Instrumento guardado = repositorio.save(aEntidad(datos, null));
        return aDTO(guardado);
    }

    public Optional<InstrumentoDTO> actualizar(Long id, InstrumentoDTO datos) {
        if (!repositorio.existsById(id)) {
            return Optional.empty();
        }
        return Optional.of(aDTO(repositorio.save(aEntidad(datos, id))));
    }

    public boolean eliminar(Long id) {
        if (!repositorio.existsById(id)) {
            return false;
        }
        repositorio.deleteById(id);
        return true;
    }

    private InstrumentoDTO aDTO(Instrumento e) {
        InstrumentoDTO dto = new InstrumentoDTO();
        dto.setId(e.getId());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        dto.setNumeroPreguntas(e.getNumeroPreguntas());
        dto.setDuracionMinutos(e.getDuracionMinutos());
        dto.setPuntajeMaximo(e.getPuntajeMaximo());
        return dto;
    }

    private Instrumento aEntidad(InstrumentoDTO dto, Long id) {
        return new Instrumento(id, dto.getNombre(), dto.getDescripcion(),
                dto.getNumeroPreguntas(), dto.getDuracionMinutos(), dto.getPuntajeMaximo());
    }
}
