package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de evento
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoResponse {

    private Long id;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private String ubicacion;
    private Boolean todoElDia;
    private String tipo;
    private LocalDateTime fechaCreacion;
}

