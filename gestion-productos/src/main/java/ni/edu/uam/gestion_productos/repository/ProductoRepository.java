package ni.edu.uam.gestion_productos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ni.edu.uam.gestion_productos.entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {
    List<Producto> findByCategoriaId(Integer categoriaId);
    List<Producto> findByEtiquetasId(Integer etiquetaId);
}
