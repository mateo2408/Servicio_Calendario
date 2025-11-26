package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para respuesta de alerta configurada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaResponse {

    private Long id;
    private Long eventoId;
    private String eventoTitulo;
    private LocalDateTime fechaAlerta;
    private String tipoNotificacion;
    private Boolean enviada;
    private LocalDateTime fechaEnvio;
    private Integer minutosAnticipacion;
    private String mensaje;
}

