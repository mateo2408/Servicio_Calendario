package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO para respuesta de calendario (vista preparada)
 * Endpoint: /calendar (response)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalendarioResponse {

    private Long id;
    private String nombre;
    private String descripcion;
    private String color;
    private Boolean publico;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaModificacion;
    private List<EventoResponse> eventos;
    private Long propietarioId;
    private String propietarioNombre;
}

