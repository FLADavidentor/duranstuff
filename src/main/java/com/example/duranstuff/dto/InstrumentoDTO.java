package com.example.duranstuff.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Data;

@Data
public class InstrumentoDTO {

    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    private String nombre;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(min = 2, max = 255, message = "La descripción debe tener entre 2 y 255 caracteres")
    private String descripcion;

    @NotNull(message = "El número de preguntas es obligatorio")
    @Min(value = 1, message = "El número de preguntas debe ser mayor que 0")
    private Integer numeroPreguntas;

    @NotNull(message = "La duración es obligatoria")
    @Min(value = 1, message = "La duración en minutos debe ser mayor que 0")
    private Integer duracionMinutos;

    @NotNull(message = "El puntaje máximo es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El puntaje máximo debe ser mayor que 0")
    private BigDecimal puntajeMaximo;
}
