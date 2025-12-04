package org.example.integration.repository;

import org.example.model.Calendario;
import org.example.model.EventoCalendario;
import org.example.model.Usuario;
import org.example.repository.CalendarioRepository;
import org.example.repository.EventoRepository;
import org.example.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración para CalendarioRepository usando TestContainers
 *
 * Cobertura:
 * - Operaciones CRUD con calendarios
 * - Relaciones con usuarios y eventos
 * - Consultas personalizadas
 * - Integridad referencial
 */
@DataJpaTest
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("Pruebas de Integración - CalendarioRepository")
class CalendarioRepositoryIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Autowired
    private CalendarioRepository calendarioRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private TestEntityManager entityManager;

    private Usuario usuario;
    private Calendario calendario;

    @BeforeEach
    void setUp() {
        eventoRepository.deleteAll();
        calendarioRepository.deleteAll();
        usuarioRepository.deleteAll();

        usuario = new Usuario();
        usuario.setUsername("testuser");
        usuario.setPassword("password");
        usuario.setEmail("test@email.com");
        usuario.setNombre("Test User");
        usuario.setFechaCreacion(LocalDateTime.now());
        usuario.setActivo(true);
        usuario.setRol(Usuario.RolUsuario.USUARIO);
        usuario = usuarioRepository.save(usuario);

        calendario = new Calendario();
        calendario.setNombre("Mi Calendario");
        calendario.setDescripcion("Descripción del calendario");
        calendario.setColor("#3788d8");
        calendario.setPublico(false);
        calendario.setPropietario(usuario);
        calendario.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    @DisplayName("Debe guardar calendario con relación a usuario")
    void testSaveCalendarioConUsuario() {
        // Act
        Calendario saved = calendarioRepository.save(calendario);
        entityManager.flush();
        entityManager.clear();

        // Assert
        assertNotNull(saved.getId());
        assertEquals(usuario.getId(), saved.getPropietario().getId());
    }

    @Test
    @DisplayName("Debe encontrar calendarios por propietario ID")
    void testFindByPropietarioId() {
        // Arrange
        calendarioRepository.save(calendario);

        Calendario calendario2 = new Calendario();
        calendario2.setNombre("Segundo Calendario");
        calendario2.setDescripcion("Otro calendario");
        calendario2.setColor("#FF5733");
        calendario2.setPublico(true);
        calendario2.setPropietario(usuario);
        calendario2.setFechaCreacion(LocalDateTime.now());
        calendarioRepository.save(calendario2);
        entityManager.flush();

        // Act
        List<Calendario> calendarios = calendarioRepository.findByPropietarioId(usuario.getId());

        // Assert
        assertEquals(2, calendarios.size());
        assertTrue(calendarios.stream().allMatch(c -> c.getPropietario().getId().equals(usuario.getId())));
    }

    @Test
    @DisplayName("Debe encontrar calendarios por propietario")
    void testFindByPropietario() {
        // Arrange
        calendarioRepository.save(calendario);
        entityManager.flush();

        // Act
        List<Calendario> calendarios = calendarioRepository.findByPropietario(usuario);

        // Assert
        assertEquals(1, calendarios.size());
        assertEquals(calendario.getNombre(), calendarios.get(0).getNombre());
    }

    @Test
    @DisplayName("Debe encontrar calendarios públicos")
    void testFindCalendariosPublicos() {
        // Arrange
        calendario.setPublico(true);
        calendarioRepository.save(calendario);

        Calendario calendarioPrivado = new Calendario();
        calendarioPrivado.setNombre("Calendario Privado");
        calendarioPrivado.setDescripcion("Privado");
        calendarioPrivado.setColor("#000000");
        calendarioPrivado.setPublico(false);
        calendarioPrivado.setPropietario(usuario);
        calendarioPrivado.setFechaCreacion(LocalDateTime.now());
        calendarioRepository.save(calendarioPrivado);
        entityManager.flush();

        // Act
        List<Calendario> publicos = calendarioRepository.findByPublicoTrue();

        // Assert
        assertEquals(1, publicos.size());
        assertTrue(publicos.get(0).getPublico());
    }

    @Test
    @DisplayName("Debe eliminar calendario en cascada con eventos")
    void testDeleteCalendarioCascadeEventos() {
        // Arrange
        Calendario saved = calendarioRepository.save(calendario);

        EventoCalendario evento = new EventoCalendario();
        evento.setTitulo("Evento Test");
        evento.setDescripcion("Descripción");
        evento.setFechaInicio(LocalDateTime.now().plusDays(1));
        evento.setFechaFin(LocalDateTime.now().plusDays(1).plusHours(2));
        evento.setCalendario(saved);
        evento.setTodoElDia(false);
        evento.setTipo(EventoCalendario.TipoEvento.EVENTO_PERSONAL);
        eventoRepository.save(evento);
        entityManager.flush();

        Long calendarioId = saved.getId();
        Long eventoId = evento.getId();

        // Act
        calendarioRepository.deleteById(calendarioId);
        entityManager.flush();

        // Assert
        assertFalse(calendarioRepository.findById(calendarioId).isPresent());
        // Los eventos deben eliminarse en cascada
        assertFalse(eventoRepository.findById(eventoId).isPresent());
    }

    @Test
    @DisplayName("Debe actualizar calendario correctamente")
    void testUpdateCalendario() {
        // Arrange
        Calendario saved = calendarioRepository.save(calendario);
        entityManager.flush();
        entityManager.clear();

        // Act
        saved.setNombre("Nombre Actualizado");
        saved.setColor("#FFFFFF");
        Calendario updated = calendarioRepository.save(saved);
        entityManager.flush();

        // Assert
        Calendario found = calendarioRepository.findById(updated.getId()).orElseThrow();
        assertEquals("Nombre Actualizado", found.getNombre());
        assertEquals("#FFFFFF", found.getColor());
    }

    @Test
    @DisplayName("Debe contar calendarios por usuario")
    void testContarCalendariosPorUsuario() {
        // Arrange
        calendarioRepository.save(calendario);

        Calendario calendario2 = new Calendario();
        calendario2.setNombre("Segundo");
        calendario2.setDescripcion("Desc");
        calendario2.setColor("#123456");
        calendario2.setPublico(false);
        calendario2.setPropietario(usuario);
        calendario2.setFechaCreacion(LocalDateTime.now());
        calendarioRepository.save(calendario2);
        entityManager.flush();

        // Act
        List<Calendario> calendarios = calendarioRepository.findByPropietarioId(usuario.getId());
        long count = calendarios.size();

        // Assert
        assertEquals(2, count);
    }

    @Test
    @DisplayName("Debe encontrar todos los calendarios")
    void testFindAll() {
        // Arrange
        calendarioRepository.save(calendario);

        Calendario calendario2 = new Calendario();
        calendario2.setNombre("Segundo Calendario");
        calendario2.setDescripcion("Otro calendario");
        calendario2.setColor("#FF5733");
        calendario2.setPublico(true);
        calendario2.setPropietario(usuario);
        calendario2.setFechaCreacion(LocalDateTime.now());
        calendarioRepository.save(calendario2);
        entityManager.flush();

        // Act
        List<Calendario> todos = calendarioRepository.findAll();

        // Assert
        assertTrue(todos.size() >= 2);
    }

    @Test
    @DisplayName("Debe persistir fechas automáticamente")
    void testFechasAutomaticas() {
        // Act
        Calendario saved = calendarioRepository.save(calendario);
        entityManager.flush();

        // Assert
        assertNotNull(saved.getFechaCreacion());
        assertNotNull(saved.getFechaModificacion());
    }
}

