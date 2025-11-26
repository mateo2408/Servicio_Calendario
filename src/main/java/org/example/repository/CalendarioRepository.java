package org.example.repository;

import org.example.model.Calendario;
import org.example.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para acceso a BD_Calendarios (cifrados)
 */
@Repository
public interface CalendarioRepository extends JpaRepository<Calendario, Long> {

    List<Calendario> findByPropietario(Usuario propietario);

    List<Calendario> findByPropietarioId(Long propietarioId);

    List<Calendario> findByPublicoTrue();
}

