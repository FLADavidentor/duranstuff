package ni.edu.uam.gestion_productos.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductoRequestDTO {

    private String codigo;
    private String nombre;
    private BigDecimal precioVenta;
    private Integer existencia;
    private Integer categoriaId;

    // Getters y Setters
}