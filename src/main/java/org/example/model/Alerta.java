package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad Alerta
 * Representa alertas configuradas por usuarios para eventos
 */
@Entity
@Table(name = "alertas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private EventoCalendario evento;

    @Column(name = "fecha_alerta", nullable = false)
    private LocalDateTime fechaAlerta;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_notificacion")
    private TipoNotificacion tipoNotificacion;

    @Column(nullable = false)
    private Boolean enviada = false;

    @Column(name = "fecha_envio")
    private LocalDateTime fechaEnvio;

    @Column(name = "minutos_anticipacion")
    private Integer minutosAnticipacion = 15;

    private String mensaje;

    public enum TipoNotificacion {
        PUSH, EMAIL, SMS
    }

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

