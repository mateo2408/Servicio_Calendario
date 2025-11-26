package org.example.service;

import org.example.dto.AlertaRequest;
import org.example.dto.AlertaResponse;
import org.example.model.Alerta;
import org.example.model.EventoCalendario;
import org.example.model.Usuario;
import org.example.repository.AlertaRepository;
import org.example.repository.EventoRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ServicioNotificaciones - Capa de Servicios de Aplicacion
 * Funciones:
 * - gestionar notificaciones
 * - guardar alerta
 */
@Service
public class NotificationService {

    @Autowired
    private AlertaRepository alertaRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MessageBrokerService messageBrokerService;

    /**
     * Configurar alerta para un evento
     * Endpoint: /alerts (POST)
     */
    @Transactional
    public AlertaResponse guardarAlerta(AlertaRequest request, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        EventoCalendario evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new RuntimeException("Evento no encontrado"));

        // Verificar que el usuario tiene acceso al evento
        if (!evento.getCalendario().getPropietario().getUsername().equals(username)) {
            throw new RuntimeException("No tiene permisos para configurar alertas en este evento");
        }

        Alerta alerta = new Alerta();
        alerta.setUsuario(usuario);
        alerta.setEvento(evento);
        alerta.setFechaAlerta(request.getFechaAlerta());
        alerta.setTipoNotificacion(Alerta.TipoNotificacion.valueOf(request.getTipoNotificacion()));
        alerta.setMinutosAnticipacion(request.getMinutosAnticipacion());
        alerta.setMensaje(request.getMensaje());
        alerta.setEnviada(false);

        alerta = alertaRepository.save(alerta);

        // Publicar evento en el broker de mensajes para programar notificacion
        messageBrokerService.publicarEventoAlerta(alerta);

        return convertirAlertaAResponse(alerta);
    }

    /**
     * Obtener alertas del usuario
     */
    @Transactional(readOnly = true)
    public List<AlertaResponse> obtenerAlertasUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Alerta> alertas = alertaRepository.findByUsuarioId(usuario.getId());

        return alertas.stream()
                .map(this::convertirAlertaAResponse)
                .collect(Collectors.toList());
    }

    /**
     * Procesar alertas pendientes (llamado por scheduler)
     */
    @Transactional
    public void procesarAlertasPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Alerta> alertasPendientes = alertaRepository
                .findByEnviadaFalseAndFechaAlertaBefore(ahora);

        for (Alerta alerta : alertasPendientes) {
            try {
                // Enviar notificacion segun el tipo
                enviarNotificacion(alerta);

                // Marcar como enviada
                alerta.setEnviada(true);
                alerta.setFechaEnvio(LocalDateTime.now());
                alertaRepository.save(alerta);

                // Notificar que fue mostrada
                messageBrokerService.notificarAlertaMostrada(alerta);
            } catch (Exception e) {
                System.err.println("Error procesando alerta " + alerta.getId() + ": " + e.getMessage());
            }
        }
    }

    /**
     * Enviar notificacion segun el tipo (PUSH, EMAIL, SMS)
     */
    private void enviarNotificacion(Alerta alerta) {
        switch (alerta.getTipoNotificacion()) {
            case PUSH:
                enviarNotificacionPush(alerta);
                break;
            case EMAIL:
                enviarNotificacionEmail(alerta);
                break;
            case SMS:
                enviarNotificacionSMS(alerta);
                break;
        }
    }

    private void enviarNotificacionPush(Alerta alerta) {
        // Integracion con proveedor de Push Notifications
        System.out.println("Enviando notificacion PUSH: " + alerta.getMensaje());
        messageBrokerService.enviarNotificacionPush(alerta);
    }

    private void enviarNotificacionEmail(Alerta alerta) {
        // Integracion con proveedor de Email
        System.out.println("Enviando notificacion EMAIL: " + alerta.getMensaje());
        messageBrokerService.enviarNotificacionEmail(alerta);
    }

    private void enviarNotificacionSMS(Alerta alerta) {
        // Integracion con proveedor de SMS
        System.out.println("Enviando notificacion SMS: " + alerta.getMensaje());
        messageBrokerService.enviarNotificacionSMS(alerta);
    }

    /**
     * Convertir alerta a DTO
     */
    private AlertaResponse convertirAlertaAResponse(Alerta alerta) {
        AlertaResponse response = new AlertaResponse();
        response.setId(alerta.getId());
        response.setEventoId(alerta.getEvento().getId());
        response.setEventoTitulo(alerta.getEvento().getTitulo());
        response.setFechaAlerta(alerta.getFechaAlerta());
        response.setTipoNotificacion(alerta.getTipoNotificacion().name());
        response.setEnviada(alerta.getEnviada());
        response.setFechaEnvio(alerta.getFechaEnvio());
        response.setMinutosAnticipacion(alerta.getMinutosAnticipacion());
        response.setMensaje(alerta.getMensaje());

        return response;
    }
}

