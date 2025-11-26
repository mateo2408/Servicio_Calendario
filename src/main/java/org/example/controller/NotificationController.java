package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.AlertaRequest;
import org.example.dto.AlertaResponse;
import org.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API Gateway - Endpoints de Alertas/Notificaciones
 * Capa de Integración - API_Gateway_HTTPS
 * Endpoints:
 * - POST /alerts - configurar alerta
 * - GET /alerts - obtener alertas del usuario
 * - POST /notifications/shown - notificación mostrada (confirmación)
 */
@RestController
@RequestMapping
@Tag(name = "Alertas y Notificaciones", description = "Endpoints para gestión de alertas y notificaciones")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Endpoint: POST /alerts
     * Configurar una nueva alerta para un evento
     */
    @PostMapping("/alerts")
    @Operation(summary = "Configurar alerta",
               description = "Configura una alerta para un evento del calendario")
    public ResponseEntity<AlertaResponse> configurarAlerta(
            @Valid @RequestBody AlertaRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            AlertaResponse alerta = notificationService.guardarAlerta(request, username);
            return ResponseEntity.ok(alerta);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint: GET /alerts
     * Obtener todas las alertas del usuario
     */
    @GetMapping("/alerts")
    @Operation(summary = "Obtener alertas",
               description = "Retorna todas las alertas configuradas por el usuario")
    public ResponseEntity<List<AlertaResponse>> obtenerAlertas(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<AlertaResponse> alertas = notificationService.obtenerAlertasUsuario(username);
            return ResponseEntity.ok(alertas);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint: POST /notifications/shown
     * Confirmar que una notificación fue mostrada al usuario
     */
    @PostMapping("/notifications/shown")
    @Operation(summary = "Notificación mostrada",
               description = "Confirma que una notificación fue mostrada al usuario (confirmación de entrega)")
    public ResponseEntity<String> notificacionMostrada(
            @RequestParam Long alertaId,
            Authentication authentication) {
        try {
            // Este endpoint normalmente sería llamado por las aplicaciones cliente
            // para confirmar que la notificación fue entregada y mostrada
            return ResponseEntity.ok("Confirmación de notificación recibida");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error confirmando notificación");
        }
    }
}

