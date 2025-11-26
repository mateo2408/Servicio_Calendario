package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para crear un nuevo evento en el calendario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoRequest {

    @NotBlank(message = "El título es obligatorio")
    private String titulo;

    private String descripcion;

    @NotNull(message = "La fecha de inicio es obligatoria")
    private LocalDateTime fechaInicio;

    @NotNull(message = "La fecha de fin es obligatoria")
    private LocalDateTime fechaFin;

    private String ubicacion;

    private Boolean todoElDia = false;

    private String tipo = "EVENTO_PERSONAL";

    @NotNull(message = "El ID del calendario es obligatorio")
    private Long calendarioId;
}

