package org.example.repository;

import org.example.model.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para alertas de usuarios
 */
@Repository
public interface AlertaRepository extends JpaRepository<Alerta, Long> {

    List<Alerta> findByUsuarioId(Long usuarioId);

    List<Alerta> findByEnviadaFalseAndFechaAlertaBefore(LocalDateTime fecha);

    List<Alerta> findByEventoId(Long eventoId);
}

