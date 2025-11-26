package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.CalendarioRequest;
import org.example.dto.CalendarioResponse;
import org.example.dto.EventoRequest;
import org.example.dto.EventoResponse;
import org.example.service.CalendarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * API Gateway - Endpoints de Calendario
 * Capa de Integración - API_Gateway_HTTPS
 * Endpoints:
 * - GET /calendar - solicitar calendario
 * - GET /calendar/view - mostrar calendario (vista preparada)
 * - POST /calendar - crear calendario
 * - POST /calendar/events - agregar evento
 */
@RestController
@RequestMapping("/calendar")
@Tag(name = "Calendario", description = "Endpoints para gestión de calendarios y eventos")
@SecurityRequirement(name = "bearerAuth")
@CrossOrigin(origins = "*")
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    /**
     * Endpoint: GET /calendar
     * Obtener calendarios del usuario autenticado
     */
    @GetMapping
    @Operation(summary = "Obtener calendarios",
               description = "Retorna todos los calendarios del usuario autenticado (descifrados)")
    public ResponseEntity<List<CalendarioResponse>> obtenerCalendarios(Authentication authentication) {
        try {
            String username = authentication.getName();
            List<CalendarioResponse> calendarios = calendarService.obtenerCalendariosUsuario(username);
            return ResponseEntity.ok(calendarios);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint: GET /calendar/view
     * Mostrar calendario específico (vista preparada con descifrado)
     */
    @GetMapping("/view")
    @Operation(summary = "Mostrar calendario",
               description = "Retorna un calendario específico con todos sus eventos (vista preparada)")
    public ResponseEntity<CalendarioResponse> mostrarCalendario(
            @RequestParam Long id,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            CalendarioResponse calendario = calendarService.obtenerCalendarioPorId(id, username);
            return ResponseEntity.ok(calendario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint: GET /calendar/{id}
     * Obtener calendario por ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Obtener calendario por ID",
               description = "Retorna un calendario específico si el usuario tiene permisos")
    public ResponseEntity<CalendarioResponse> obtenerCalendarioPorId(
            @PathVariable Long id,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            CalendarioResponse calendario = calendarService.obtenerCalendarioPorId(id, username);
            return ResponseEntity.ok(calendario);
        } catch (Exception e) {
            return ResponseEntity.status(403).build();
        }
    }

    /**
     * Endpoint: POST /calendar
     * Crear nuevo calendario
     */
    @PostMapping
    @Operation(summary = "Crear calendario",
               description = "Crea un nuevo calendario para el usuario autenticado")
    public ResponseEntity<CalendarioResponse> crearCalendario(
            @Valid @RequestBody CalendarioRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            CalendarioResponse calendario = calendarService.crearCalendario(request, username);
            return ResponseEntity.ok(calendario);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Endpoint: POST /calendar/events
     * Agregar evento al calendario
     */
    @PostMapping("/events")
    @Operation(summary = "Agregar evento",
               description = "Agrega un nuevo evento a un calendario")
    public ResponseEntity<EventoResponse> agregarEvento(
            @Valid @RequestBody EventoRequest request,
            Authentication authentication) {
        try {
            String username = authentication.getName();
            EventoResponse evento = calendarService.agregarEvento(request, username);
            return ResponseEntity.ok(evento);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

