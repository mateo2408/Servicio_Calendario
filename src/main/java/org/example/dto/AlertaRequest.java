package org.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO para configurar una alerta
 * Endpoint: /alerts (request)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaRequest {

    @NotNull(message = "El ID del evento es obligatorio")
    private Long eventoId;

    @NotNull(message = "La fecha de la alerta es obligatoria")
    private LocalDateTime fechaAlerta;

    @NotNull(message = "El tipo de notificación es obligatorio")
    private String tipoNotificacion; // PUSH, EMAIL, SMS

    private Integer minutosAnticipacion = 15;

    private String mensaje;
}

