package org.example.unit.service;

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
import org.example.service.CalendarService;
import org.example.service.EncryptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para CalendarService usando JUnit 5 + Mockito
 *
 * Cobertura:
 * - Creación de calendarios
 * - Obtención de calendarios
 * - Creación de eventos
 * - Cifrado/Descifrado de datos
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - CalendarService")
class CalendarServiceTest {

    @Mock
    private CalendarioRepository calendarioRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EncryptionService encryptionService;

    @InjectMocks
    private CalendarService calendarService;

    private Usuario usuarioMock;
    private Calendario calendarioMock;
    private EventoCalendario eventoMock;
    private CalendarioRequest calendarioRequest;
    private EventoRequest eventoRequest;

    @BeforeEach
    void setUp() {
        // Usuario mock
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setUsername("testuser");
        usuarioMock.setEmail("test@email.com");
        usuarioMock.setNombre("Test User");
        usuarioMock.setActivo(true);

        // Calendario mock
        calendarioMock = new Calendario();
        calendarioMock.setId(1L);
        calendarioMock.setNombre("Mi Calendario");
        calendarioMock.setDescripcion("Descripción del calendario");
        calendarioMock.setDatosCifrados("datos-cifrados-mock");
        calendarioMock.setColor("#3788d8");
        calendarioMock.setPublico(false);
        calendarioMock.setPropietario(usuarioMock);
        calendarioMock.setFechaCreacion(LocalDateTime.now());
        calendarioMock.setFechaModificacion(LocalDateTime.now());
        calendarioMock.setEventos(new ArrayList<>());

        // Evento mock
        eventoMock = new EventoCalendario();
        eventoMock.setId(1L);
        eventoMock.setTitulo("Reunión");
        eventoMock.setDescripcion("Descripción del evento");
        eventoMock.setDatosCifrados("evento-cifrado-mock");
        eventoMock.setFechaInicio(LocalDateTime.now().plusDays(1));
        eventoMock.setFechaFin(LocalDateTime.now().plusDays(1).plusHours(2));
        eventoMock.setCalendario(calendarioMock);
        eventoMock.setTodoElDia(false);
        eventoMock.setTipo(EventoCalendario.TipoEvento.EVENTO_LABORAL);

        // Request objects
        calendarioRequest = new CalendarioRequest();
        calendarioRequest.setNombre("Nuevo Calendario");
        calendarioRequest.setDescripcion("Descripción del calendario");
        calendarioRequest.setColor("#FF5733");
        calendarioRequest.setPublico(false);

        eventoRequest = new EventoRequest();
        eventoRequest.setTitulo("Nueva Reunión");
        eventoRequest.setDescripcion("Descripción del evento");
        eventoRequest.setFechaInicio(LocalDateTime.now().plusDays(1));
        eventoRequest.setFechaFin(LocalDateTime.now().plusDays(1).plusHours(1));
        eventoRequest.setCalendarioId(1L);
        eventoRequest.setTodoElDia(false);
        eventoRequest.setTipo("EVENTO_LABORAL");
    }

    @Test
    @DisplayName("Debe crear calendario correctamente")
    void testCrearCalendario_Success() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuarioMock));
        when(encryptionService.cifrar(anyString())).thenReturn("encrypted-data");
        when(calendarioRepository.save(any(Calendario.class))).thenReturn(calendarioMock);
        when(encryptionService.descifrarCalendario(anyString())).thenReturn("Descripción descifrada");

        // Act
        CalendarioResponse response = calendarService.crearCalendario(calendarioRequest, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(calendarioMock.getId(), response.getId());
        assertEquals(calendarioMock.getNombre(), response.getNombre());

        verify(usuarioRepository).findByUsername("testuser");
        verify(encryptionService).cifrar(anyString());
        verify(calendarioRepository).save(any(Calendario.class));
    }

    @Test
    @DisplayName("Debe obtener calendarios del usuario")
    void testObtenerCalendariosUsuario_Success() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuarioMock));
        when(calendarioRepository.findByPropietario(any(Usuario.class))).thenReturn(Arrays.asList(calendarioMock));
        when(encryptionService.descifrarCalendario(anyString())).thenReturn("Descripción descifrada");

        // Act
        List<CalendarioResponse> calendarios = calendarService.obtenerCalendariosUsuario("testuser");

        // Assert
        assertNotNull(calendarios);
        assertFalse(calendarios.isEmpty());
        assertEquals(1, calendarios.size());
        assertEquals(calendarioMock.getId(), calendarios.get(0).getId());

        verify(usuarioRepository).findByUsername("testuser");
        verify(calendarioRepository).findByPropietario(usuarioMock);
    }

    @Test
    @DisplayName("Debe obtener calendario por ID correctamente")
    void testObtenerCalendarioPorId_Success() {
        // Arrange
        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.of(calendarioMock));
        when(encryptionService.descifrarCalendario(anyString())).thenReturn("Descripción descifrada");

        // Act
        CalendarioResponse response = calendarService.obtenerCalendarioPorId(1L, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(calendarioMock.getId(), response.getId());
        assertEquals(calendarioMock.getNombre(), response.getNombre());

        verify(calendarioRepository).findById(1L);
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando calendario no existe")
    void testObtenerCalendarioPorId_NoExiste() {
        // Arrange
        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            calendarService.obtenerCalendarioPorId(99L, "testuser");
        });

        verify(calendarioRepository).findById(99L);
    }

    @Test
    @DisplayName("Debe crear evento correctamente")
    void testAgregarEvento_Success() {
        // Arrange
        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.of(calendarioMock));
        when(encryptionService.cifrar(anyString())).thenReturn("encrypted-event-data");
        when(eventoRepository.save(any(EventoCalendario.class))).thenReturn(eventoMock);
        when(encryptionService.descifrar(anyString())).thenReturn("Descripción descifrada");

        // Act
        EventoResponse response = calendarService.agregarEvento(eventoRequest, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(eventoMock.getId(), response.getId());
        assertEquals(eventoMock.getTitulo(), response.getTitulo());

        verify(calendarioRepository).findById(eventoRequest.getCalendarioId());
        verify(encryptionService).cifrar(anyString());
        verify(eventoRepository).save(any(EventoCalendario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando calendario no existe al crear evento")
    void testAgregarEvento_CalendarioNoExiste() {
        // Arrange
        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            calendarService.agregarEvento(eventoRequest, "testuser");
        });

        verify(eventoRepository, never()).save(any(EventoCalendario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando usuario no es propietario del calendario")
    void testAgregarEvento_UsuarioNoEsPropietario() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setUsername("otrousuario");
        calendarioMock.setPropietario(otroUsuario);

        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.of(calendarioMock));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            calendarService.agregarEvento(eventoRequest, "testuser");
        });

        verify(eventoRepository, never()).save(any(EventoCalendario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción al acceder calendario privado sin permisos")
    void testObtenerCalendarioPorId_SinPermisos() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setUsername("otrousuario");
        calendarioMock.setPropietario(otroUsuario);
        calendarioMock.setPublico(false);

        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.of(calendarioMock));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            calendarService.obtenerCalendarioPorId(1L, "testuser");
        });
    }

    @Test
    @DisplayName("Debe permitir acceso a calendario público")
    void testObtenerCalendarioPorId_CalendarioPublico() {
        // Arrange
        Usuario otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setUsername("otrousuario");
        calendarioMock.setPropietario(otroUsuario);
        calendarioMock.setPublico(true); // Calendario público

        when(calendarioRepository.findById(anyLong())).thenReturn(Optional.of(calendarioMock));
        when(encryptionService.descifrarCalendario(anyString())).thenReturn("Descripción descifrada");

        // Act
        CalendarioResponse response = calendarService.obtenerCalendarioPorId(1L, "testuser");

        // Assert
        assertNotNull(response);
        assertEquals(calendarioMock.getId(), response.getId());
    }

    @Test
    @DisplayName("Debe descifrar datos al obtener calendario")
    void testDescifradoDeCalendario() {
        // Arrange
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuarioMock));
        when(calendarioRepository.findByPropietario(any(Usuario.class))).thenReturn(Arrays.asList(calendarioMock));
        when(encryptionService.descifrarCalendario("datos-cifrados-mock")).thenReturn("Descripción descifrada");

        // Act
        List<CalendarioResponse> calendarios = calendarService.obtenerCalendariosUsuario("testuser");

        // Assert
        assertNotNull(calendarios);
        verify(encryptionService).descifrarCalendario("datos-cifrados-mock");
    }
}

