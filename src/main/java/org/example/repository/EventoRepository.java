package org.example.repository;

import org.example.model.EventoCalendario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para eventos de calendario
 */
@Repository
public interface EventoRepository extends JpaRepository<EventoCalendario, Long> {

    List<EventoCalendario> findByCalendarioId(Long calendarioId);

    List<EventoCalendario> findByCalendarioIdAndFechaInicioBetween(
            Long calendarioId,
            LocalDateTime inicio,
            LocalDateTime fin
    );
}

