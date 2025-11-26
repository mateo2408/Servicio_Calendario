package org.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo calendario
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioRequest {

    @NotBlank(message = "El nombre del calendario es obligatorio")
    private String nombre;

    private String descripcion;

    private String color = "#3788d8";

    private Boolean publico = false;
}

