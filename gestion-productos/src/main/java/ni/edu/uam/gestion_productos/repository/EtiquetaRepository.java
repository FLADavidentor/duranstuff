package ni.edu.uam.gestion_productos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ni.edu.uam.gestion_productos.entity.Etiqueta;

@Repository
public interface EtiquetaRepository

        extends JpaRepository<Etiqueta, Integer> {
}
