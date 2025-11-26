package org.example.service;

import org.example.dto.CalendarioRequest;
import org.example.dto.CalendarioResponse;
import org.example.dto.EventoRequest;
import org.example.dto.EventoResponse;
import org.example.model.Calendario;
import org.example.model.EventoCalendario;
import org.example.model.Usuario;
import org.example.repository.CalendarioRepository;
import org.example.repository.EventoRepository;
import org.example.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ServicioCalendario - Capa de Servicios de Aplicación
 * Funciones:
 * - operaciones de calendario
 * - peticion de calendario
 * - vista preparada (con descifrado)
 */
@Service
public class CalendarService {

    @Autowired
    private CalendarioRepository calendarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EncryptionService encryptionService;

    /**
     * Obtener calendarios del usuario (descifrados)
     * Endpoint: /calendar (GET)
     */
    @Transactional(readOnly = true)
    public List<CalendarioResponse> obtenerCalendariosUsuario(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Calendario> calendarios = calendarioRepository.findByPropietario(usuario);

        return calendarios.stream()
                .map(this::prepararVistaCalendario)
                .collect(Collectors.toList());
    }

    /**
     * Obtener calendario por ID con descifrado
     * Endpoint: /calendar/{id} (GET) - vista preparada
     */
    @Transactional(readOnly = true)
    public CalendarioResponse obtenerCalendarioPorId(Long id, String username) {
        Calendario calendario = calendarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Calendario no encontrado"));

        // Verificar permisos
        if (!calendario.getPropietario().getUsername().equals(username) && !calendario.getPublico()) {
            throw new RuntimeException("No tiene permisos para acceder a este calendario");
        }

        // Preparar vista con descifrado
        return prepararVistaCalendario(calendario);
    }

    /**
     * Preparar vista del calendario - descifra datos
     */
    private CalendarioResponse prepararVistaCalendario(Calendario calendario) {
        // Descifrar datos del calendario
        String descripcionDescifrada = calendario.getDatosCifrados() != null
            ? encryptionService.descifrarCalendario(calendario.getDatosCifrados())
            : calendario.getDescripcion();

        // Obtener eventos y descifrarlos
        List<EventoResponse> eventos = calendario.getEventos().stream()
                .map(this::convertirEventoAResponse)
                .collect(Collectors.toList());

        CalendarioResponse response = new CalendarioResponse();
        response.setId(calendario.getId());
        response.setNombre(calendario.getNombre());
        response.setDescripcion(descripcionDescifrada);
        response.setColor(calendario.getColor());
        response.setPublico(calendario.getPublico());
        response.setFechaCreacion(calendario.getFechaCreacion());
        response.setFechaModificacion(calendario.getFechaModificacion());
        response.setEventos(eventos);
        response.setPropietarioId(calendario.getPropietario().getId());
        response.setPropietarioNombre(calendario.getPropietario().getNombre());

        return response;
    }

    /**
     * Crear nuevo calendario
     */
    @Transactional
    public CalendarioResponse crearCalendario(CalendarioRequest request, String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Calendario calendario = new Calendario();
        calendario.setNombre(request.getNombre());
        calendario.setDescripcion(request.getDescripcion());
        calendario.setColor(request.getColor());
        calendario.setPublico(request.getPublico());
        calendario.setPropietario(usuario);

        // Cifrar información sensible
        if (request.getDescripcion() != null && !request.getDescripcion().isEmpty()) {
            String cifrado = encryptionService.cifrar(request.getDescripcion());
            calendario.setDatosCifrados(cifrado);
        }

        calendario = calendarioRepository.save(calendario);

        return prepararVistaCalendario(calendario);
    }

    /**
     * Agregar evento al calendario
     */
    @Transactional
    public EventoResponse agregarEvento(EventoRequest request, String username) {
        Calendario calendario = calendarioRepository.findById(request.getCalendarioId())
                .orElseThrow(() -> new RuntimeException("Calendario no encontrado"));

        // Verificar permisos
        if (!calendario.getPropietario().getUsername().equals(username)) {
            throw new RuntimeException("No tiene permisos para modificar este calendario");
        }

        EventoCalendario evento = new EventoCalendario();
        evento.setTitulo(request.getTitulo());
        evento.setDescripcion(request.getDescripcion());
        evento.setFechaInicio(request.getFechaInicio());
        evento.setFechaFin(request.getFechaFin());
        evento.setUbicacion(request.getUbicacion());
        evento.setTodoElDia(request.getTodoElDia());
        evento.setTipo(EventoCalendario.TipoEvento.valueOf(request.getTipo()));
        evento.setCalendario(calendario);

        // Cifrar datos sensibles del evento
        if (request.getDescripcion() != null && !request.getDescripcion().isEmpty()) {
            String cifrado = encryptionService.cifrar(request.getDescripcion());
            evento.setDatosCifrados(cifrado);
        }

        evento = eventoRepository.save(evento);

        return convertirEventoAResponse(evento);
    }

    /**
     * Convertir evento a DTO con descifrado
     */
    private EventoResponse convertirEventoAResponse(EventoCalendario evento) {
        String descripcionDescifrada = evento.getDatosCifrados() != null
            ? encryptionService.descifrar(evento.getDatosCifrados())
            : evento.getDescripcion();

        EventoResponse response = new EventoResponse();
        response.setId(evento.getId());
        response.setTitulo(evento.getTitulo());
        response.setDescripcion(descripcionDescifrada);
        response.setFechaInicio(evento.getFechaInicio());
        response.setFechaFin(evento.getFechaFin());
        response.setUbicacion(evento.getUbicacion());
        response.setTodoElDia(evento.getTodoElDia());
        response.setTipo(evento.getTipo().name());
        response.setFechaCreacion(evento.getFechaCreacion());

        return response;
    }
}

