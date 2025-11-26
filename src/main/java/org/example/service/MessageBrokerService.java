package org.example.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.Alerta;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Servicio para interactuar con el BrokerMensajes_BusEventos (RabbitMQ)
 * Capa de Infraestructura - Mensajería
 */
@Service
public class MessageBrokerService {

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String QUEUE_ALERTAS = "alertas.queue";
    private static final String QUEUE_NOTIFICACIONES_PUSH = "notificaciones.push.queue";
    private static final String QUEUE_NOTIFICACIONES_EMAIL = "notificaciones.email.queue";
    private static final String QUEUE_NOTIFICACIONES_SMS = "notificaciones.sms.queue";
    private static final String QUEUE_CONFIRMACIONES = "confirmaciones.queue";

    @Bean
    public Queue alertasQueue() {
        return new Queue(QUEUE_ALERTAS, true);
    }

    @Bean
    public Queue notificacionesPushQueue() {
        return new Queue(QUEUE_NOTIFICACIONES_PUSH, true);
    }

    @Bean
    public Queue notificacionesEmailQueue() {
        return new Queue(QUEUE_NOTIFICACIONES_EMAIL, true);
    }

    @Bean
    public Queue notificacionesSmsQueue() {
        return new Queue(QUEUE_NOTIFICACIONES_SMS, true);
    }

    @Bean
    public Queue confirmacionesQueue() {
        return new Queue(QUEUE_CONFIRMACIONES, true);
    }

    /**
     * Publicar evento de nueva alerta al bus de eventos
     */
    public void publicarEventoAlerta(Alerta alerta) {
        try {
            Map<String, Object> mensaje = new HashMap<>();
            mensaje.put("alertaId", alerta.getId());
            mensaje.put("eventoId", alerta.getEvento().getId());
            mensaje.put("usuarioId", alerta.getUsuario().getId());
            mensaje.put("fechaAlerta", alerta.getFechaAlerta().toString());
            mensaje.put("tipoNotificacion", alerta.getTipoNotificacion().name());
            mensaje.put("mensaje", alerta.getMensaje());

            if (rabbitTemplate != null) {
                rabbitTemplate.convertAndSend(QUEUE_ALERTAS, mensaje);
                System.out.println("Evento de alerta publicado en el bus de eventos");
            } else {
                System.out.println("RabbitMQ no disponible - Simulando publicación de alerta");
            }
        } catch (Exception e) {
            System.err.println("Error publicando evento de alerta: " + e.getMessage());
        }
    }

    /**
     * Enviar notificación Push a proveedor externo
     */
    public void enviarNotificacionPush(Alerta alerta) {
        try {
            Map<String, Object> mensaje = crearMensajeNotificacion(alerta);

            if (rabbitTemplate != null) {
                rabbitTemplate.convertAndSend(QUEUE_NOTIFICACIONES_PUSH, mensaje);
            }
            System.out.println("Notificación PUSH enviada a proveedor");
        } catch (Exception e) {
            System.err.println("Error enviando notificación Push: " + e.getMessage());
        }
    }

    /**
     * Enviar notificación Email a proveedor externo
     */
    public void enviarNotificacionEmail(Alerta alerta) {
        try {
            Map<String, Object> mensaje = crearMensajeNotificacion(alerta);
            mensaje.put("email", alerta.getUsuario().getEmail());

            if (rabbitTemplate != null) {
                rabbitTemplate.convertAndSend(QUEUE_NOTIFICACIONES_EMAIL, mensaje);
            }
            System.out.println("Notificación EMAIL enviada a proveedor");
        } catch (Exception e) {
            System.err.println("Error enviando notificación Email: " + e.getMessage());
        }
    }

    /**
     * Enviar notificación SMS a proveedor externo
     */
    public void enviarNotificacionSMS(Alerta alerta) {
        try {
            Map<String, Object> mensaje = crearMensajeNotificacion(alerta);

            if (rabbitTemplate != null) {
                rabbitTemplate.convertAndSend(QUEUE_NOTIFICACIONES_SMS, mensaje);
            }
            System.out.println("Notificación SMS enviada a proveedor");
        } catch (Exception e) {
            System.err.println("Error enviando notificación SMS: " + e.getMessage());
        }
    }

    /**
     * Notificar que una alerta fue mostrada (confirmación de entrega)
     * Endpoint: /notifications/shown (POST)
     */
    public void notificarAlertaMostrada(Alerta alerta) {
        try {
            Map<String, Object> mensaje = new HashMap<>();
            mensaje.put("alertaId", alerta.getId());
            mensaje.put("usuarioId", alerta.getUsuario().getId());
            mensaje.put("fechaMostrada", alerta.getFechaEnvio().toString());
            mensaje.put("tipo", alerta.getTipoNotificacion().name());

            if (rabbitTemplate != null) {
                rabbitTemplate.convertAndSend(QUEUE_CONFIRMACIONES, mensaje);
            }
            System.out.println("Confirmación de entrega de notificación enviada");
        } catch (Exception e) {
            System.err.println("Error notificando alerta mostrada: " + e.getMessage());
        }
    }

    private Map<String, Object> crearMensajeNotificacion(Alerta alerta) {
        Map<String, Object> mensaje = new HashMap<>();
        mensaje.put("alertaId", alerta.getId());
        mensaje.put("usuarioId", alerta.getUsuario().getId());
        mensaje.put("usuarioNombre", alerta.getUsuario().getNombre());
        mensaje.put("eventoTitulo", alerta.getEvento().getTitulo());
        mensaje.put("eventoFecha", alerta.getEvento().getFechaInicio().toString());
        mensaje.put("mensaje", alerta.getMensaje());
        mensaje.put("tipo", alerta.getTipoNotificacion().name());

        return mensaje;
    }
}

