package ni.edu.uam.gestion_productos.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProveedorRequestDTO {

    private String nombre;
    private String telefono;
    private String correo;
    private boolean activo;

    // Getters y Setters
}
