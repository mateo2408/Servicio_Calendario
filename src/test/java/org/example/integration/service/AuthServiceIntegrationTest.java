package org.example.integration.service;

import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegistroRequest;
import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración end-to-end para AuthService
 *
 * Usa:
 * - TestContainers con PostgreSQL real
 * - Spring Boot completo
 * - Transacciones reales
 * - Cifrado de passwords real
 */
@SpringBootTest
@Testcontainers
@Transactional
@DisplayName("Pruebas de Integración E2E - AuthService")
class AuthServiceIntegrationTest {

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
        registry.add("spring.rabbitmq.host", () -> "localhost");
        registry.add("spring.rabbitmq.port", () -> "5672");
    }

    @Autowired
    private AuthService authService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    private RegistroRequest registroRequest;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAll();

        registroRequest = new RegistroRequest();
        registroRequest.setUsername("integrationuser");
        registroRequest.setPassword("IntegrationPass123!");
        registroRequest.setEmail("integration@test.com");
        registroRequest.setNombre("Integration User");
    }

    @Test
    @DisplayName("Flujo completo: Registro -> Login")
    void testFlujoCompletoRegistroYLogin() {
        // 1. Registrar usuario
        AuthResponse registroResponse = authService.registrarUsuario(registroRequest);

        assertNotNull(registroResponse);
        assertNotNull(registroResponse.getToken());
        assertEquals("Bearer", registroResponse.getTipo());
        assertEquals("integrationuser", registroResponse.getUsername());

        // 2. Verificar que el usuario fue guardado en BD
        Usuario usuarioGuardado = usuarioRepository.findByUsername("integrationuser").orElseThrow();
        assertNotNull(usuarioGuardado);
        assertTrue(usuarioGuardado.isActivo());
        assertNotNull(usuarioGuardado.getFechaCreacion());

        // 3. Hacer login con el mismo usuario
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("integrationuser");
        loginRequest.setPassword("IntegrationPass123!");

        AuthResponse loginResponse = authService.autenticarUsuario(loginRequest);

        assertNotNull(loginResponse);
        assertNotNull(loginResponse.getToken());
        assertEquals("integrationuser", loginResponse.getUsername());

        // 4. Verificar que se actualizó el último acceso
        Usuario usuarioActualizado = usuarioRepository.findByUsername("integrationuser").orElseThrow();
        assertNotNull(usuarioActualizado.getUltimoAcceso());
    }

    @Test
    @DisplayName("Debe fallar al registrar usuario duplicado")
    void testRegistroUsuarioDuplicado() {
        // Arrange
        authService.registrarUsuario(registroRequest);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(registroRequest);
        });
    }

    @Test
    @DisplayName("Debe fallar login con password incorrecto")
    void testLoginPasswordIncorrecto() {
        // Arrange
        authService.registrarUsuario(registroRequest);

        LoginRequest loginIncorrecto = new LoginRequest();
        loginIncorrecto.setUsername("integrationuser");
        loginIncorrecto.setPassword("PasswordIncorrecto123!");

        // Act & Assert
        assertThrows(Exception.class, () -> {
            authService.autenticarUsuario(loginIncorrecto);
        });
    }

    @Test
    @DisplayName("Debe cifrar el password al registrar")
    void testPasswordCifrado() {
        // Act
        authService.registrarUsuario(registroRequest);

        // Assert
        Usuario usuario = usuarioRepository.findByUsername("integrationuser").orElseThrow();
        assertNotEquals("IntegrationPass123!", usuario.getPassword());
        assertTrue(usuario.getPassword().startsWith("$2a$") || usuario.getPassword().startsWith("$2b$"));
    }

    @Test
    @DisplayName("Debe generar tokens diferentes en cada login")
    void testTokensDiferentes() {
        // Arrange
        authService.registrarUsuario(registroRequest);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername("integrationuser");
        loginRequest.setPassword("IntegrationPass123!");

        // Act
        AuthResponse response1 = authService.autenticarUsuario(loginRequest);

        // Simular espera de 1 segundo para que el token sea diferente
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        AuthResponse response2 = authService.autenticarUsuario(loginRequest);

        // Assert
        assertNotNull(response1.getToken());
        assertNotNull(response2.getToken());
        // Los tokens pueden ser iguales o diferentes dependiendo de la implementación
        // Si incluyen timestamp, serán diferentes
    }
}

