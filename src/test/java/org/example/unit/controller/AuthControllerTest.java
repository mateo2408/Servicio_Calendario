package org.example.unit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.controller.AuthController;
import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegistroRequest;
import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas unitarias para AuthController usando MockMvc
 *
 * Cobertura:
 * - Endpoints de autenticación
 * - Validación de requests
 * - Manejo de respuestas
 * - Códigos de estado HTTP
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Desactivar filtros de seguridad para tests
@DisplayName("Pruebas Unitarias - AuthController")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private LoginRequest loginRequest;
    private RegistroRequest registroRequest;
    private AuthResponse authResponse;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        registroRequest = new RegistroRequest();
        registroRequest.setUsername("newuser");
        registroRequest.setPassword("Password123!");
        registroRequest.setEmail("newuser@email.com");
        registroRequest.setNombre("New User");

        authResponse = new AuthResponse();
        authResponse.setToken("mock-jwt-token");
        authResponse.setTipo("Bearer");
        authResponse.setUsuarioId(1L);
        authResponse.setUsername("testuser");
        authResponse.setEmail("test@email.com");
    }

    @Test
    @DisplayName("POST /auth/login - Debe autenticar correctamente")
    void testLogin_Success() throws Exception {
        // Arrange
        when(authService.autenticarUsuario(any(LoginRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.tipo").value("Bearer"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@email.com"));
    }

    @Test
    @DisplayName("POST /auth/login - Debe retornar 401 con credenciales inválidas")
    void testLogin_CredencialesInvalidas() throws Exception {
        // Arrange
        when(authService.autenticarUsuario(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Credenciales inválidas"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/register - Debe registrar usuario correctamente")
    void testRegister_Success() throws Exception {
        // Arrange
        authResponse.setUsername("newuser");
        authResponse.setEmail("newuser@email.com");
        when(authService.registrarUsuario(any(RegistroRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mock-jwt-token"))
                .andExpect(jsonPath("$.username").value("newuser"))
                .andExpect(jsonPath("$.email").value("newuser@email.com"));
    }

    @Test
    @DisplayName("POST /auth/register - Debe retornar 400 cuando usuario ya existe")
    void testRegister_UsuarioYaExiste() throws Exception {
        // Arrange
        when(authService.registrarUsuario(any(RegistroRequest.class)))
                .thenThrow(new RuntimeException("El username ya esta en uso"));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /auth/login - Debe aceptar JSON válido")
    void testLogin_JsonValido() throws Exception {
        // Arrange
        when(authService.autenticarUsuario(any(LoginRequest.class))).thenReturn(authResponse);

        String jsonRequest = """
                {
                    "username": "testuser",
                    "password": "password123"
                }
                """;

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @DisplayName("POST /auth/register - Debe validar formato de request")
    void testRegister_RequestValido() throws Exception {
        // Arrange
        when(authService.registrarUsuario(any(RegistroRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.tipo").exists())
                .andExpect(jsonPath("$.usuarioId").exists())
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.email").exists());
    }

    @Test
    @DisplayName("POST /auth/login - Debe manejar errores inesperados")
    void testLogin_ErrorInesperado() throws Exception {
        // Arrange
        when(authService.autenticarUsuario(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Error interno del servidor"));

        // Act & Assert
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("POST /auth/register - Debe retornar estructura de response correcta")
    void testRegister_EstructuraResponse() throws Exception {
        // Arrange
        when(authService.registrarUsuario(any(RegistroRequest.class))).thenReturn(authResponse);

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isString())
                .andExpect(jsonPath("$.tipo").isString())
                .andExpect(jsonPath("$.usuarioId").isNumber())
                .andExpect(jsonPath("$.username").isString())
                .andExpect(jsonPath("$.email").isString());
    }

    @Test
    @DisplayName("POST /auth/register - Debe manejar email duplicado")
    void testRegister_EmailDuplicado() throws Exception {
        // Arrange
        when(authService.registrarUsuario(any(RegistroRequest.class)))
                .thenThrow(new RuntimeException("El email ya esta en uso"));

        // Act & Assert
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registroRequest)))
                .andExpect(status().isBadRequest());
    }
}

