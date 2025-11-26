package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Entidad EventoCalendario
 * Representa un evento dentro de un calendario
 */
@Entity
@Table(name = "eventos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoCalendario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDateTime fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDateTime fechaFin;

    @Column(nullable = false)
    private String ubicacion;

    @ManyToOne
    @JoinColumn(name = "calendario_id", nullable = false)
    private Calendario calendario;

    @Column(name = "todo_el_dia")
    private Boolean todoElDia = false;

    @Column(name = "datos_cifrados", columnDefinition = "TEXT")
    private String datosCifrados; // Información sensible del evento

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Enumerated(EnumType.STRING)
    private TipoEvento tipo;

    public enum TipoEvento {
        REUNION, TAREA, RECORDATORIO, EVENTO_PERSONAL, EVENTO_LABORAL
    }

    @PrePersist
    protected void onCreate() {
        fechaCreacion = LocalDateTime.now();
    }
}

