package org.example.config;

import org.example.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Scheduler para procesar alertas pendientes
 * Revisa cada minuto si hay alertas que deben ser enviadas
 */
@Configuration
@EnableScheduling
public class AlertScheduler {

    @Autowired
    private NotificationService notificationService;

    /**
     * Procesa alertas pendientes cada minuto
     */
    @Scheduled(fixedRate = 60000) // Cada 60 segundos
    public void procesarAlertas() {
        try {
            notificationService.procesarAlertasPendientes();
        } catch (Exception e) {
            System.err.println("Error en scheduler de alertas: " + e.getMessage());
        }
    }
}

