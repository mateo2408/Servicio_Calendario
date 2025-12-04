package org.example.unit.service;

import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegistroRequest;
import org.example.model.Usuario;
import org.example.repository.UsuarioRepository;
import org.example.security.JwtUtil;
import org.example.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para AuthService usando JUnit 5 + Mockito
 *
 * Cobertura:
 * - Autenticación de usuarios
 * - Registro de usuarios
 * - Validación de tokens
 * - Manejo de errores
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas Unitarias - AuthService")
class AuthServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthService authService;

    private Usuario usuarioMock;
    private LoginRequest loginRequest;
    private RegistroRequest registroRequest;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        // Configurar datos de prueba
        usuarioMock = new Usuario();
        usuarioMock.setId(1L);
        usuarioMock.setUsername("testuser");
        usuarioMock.setPassword("encodedPassword");
        usuarioMock.setEmail("test@email.com");
        usuarioMock.setNombre("Test User");
        usuarioMock.setActivo(true);
        usuarioMock.setRol(Usuario.RolUsuario.USUARIO);
        usuarioMock.setFechaCreacion(LocalDateTime.now());

        loginRequest = new LoginRequest();
        loginRequest.setUsername("testuser");
        loginRequest.setPassword("password123");

        registroRequest = new RegistroRequest();
        registroRequest.setUsername("newuser");
        registroRequest.setPassword("Password123!");
        registroRequest.setEmail("newuser@email.com");
        registroRequest.setNombre("New User");

        userDetails = User.builder()
                .username("testuser")
                .password("encodedPassword")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Debe autenticar usuario correctamente")
    void testAutenticarUsuario_Success() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("mock-jwt-token");
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.of(usuarioMock));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);

        // Act
        AuthResponse response = authService.autenticarUsuario(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTipo());
        assertEquals(usuarioMock.getId(), response.getUsuarioId());
        assertEquals(usuarioMock.getUsername(), response.getUsername());
        assertEquals(usuarioMock.getEmail(), response.getEmail());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtUtil).generateToken(any(UserDetails.class));
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el usuario no existe")
    void testAutenticarUsuario_UsuarioNoEncontrado() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("mock-jwt-token");
        when(usuarioRepository.findByUsername(anyString())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.autenticarUsuario(loginRequest);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe registrar nuevo usuario correctamente")
    void testRegistrarUsuario_Success() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioMock);
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("mock-jwt-token");

        // Act
        AuthResponse response = authService.registrarUsuario(registroRequest);

        // Assert
        assertNotNull(response);
        assertEquals("mock-jwt-token", response.getToken());
        assertEquals("Bearer", response.getTipo());

        verify(usuarioRepository).existsByUsername(registroRequest.getUsername());
        verify(usuarioRepository).existsByEmail(registroRequest.getEmail());
        verify(passwordEncoder).encode(registroRequest.getPassword());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el username ya existe")
    void testRegistrarUsuario_UsernameYaExiste() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(registroRequest);
        });

        assertTrue(exception.getMessage().contains("username ya esta en uso"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe lanzar excepción cuando el email ya existe")
    void testRegistrarUsuario_EmailYaExiste() {
        // Arrange
        when(usuarioRepository.existsByUsername(anyString())).thenReturn(false);
        when(usuarioRepository.existsByEmail(anyString())).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            authService.registrarUsuario(registroRequest);
        });

        assertTrue(exception.getMessage().contains("email ya esta en uso"));
        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    @DisplayName("Debe validar usuario existente")
    void testValidarUsuario_Success() {
        // Arrange
        when(usuarioRepository.findByUsername("testuser")).thenReturn(Optional.of(usuarioMock));

        // Act
        Usuario usuario = authService.validarUsuario("testuser");

        // Assert
        assertNotNull(usuario);
        assertEquals("testuser", usuario.getUsername());
        verify(usuarioRepository).findByUsername("testuser");
    }

    @Test
    @DisplayName("Debe lanzar excepción al validar usuario inexistente")
    void testValidarUsuario_NoEncontrado() {
        // Arrange
        when(usuarioRepository.findByUsername("noexiste")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            authService.validarUsuario("noexiste");
        });
    }
}

