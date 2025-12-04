package org.example.contract.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.junit.jupiter.api.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de contrato para Auth API usando WireMock
 *
 * Objetivo:
 * - Verificar que el contrato del API se mantenga estable
 * - Simular respuestas del servidor
 * - Validar request/response schemas
 * - Probar escenarios de error
 */
@DisplayName("Pruebas de Contrato - Auth API")
class AuthApiContractTest {

    private static WireMockServer wireMockServer;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;
    private String baseUrl;

    @BeforeAll
    static void setupServer() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    static void teardownServer() {
        wireMockServer.stop();
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
        baseUrl = "http://localhost:8089";
    }

    @Test
    @DisplayName("Contrato POST /auth/login - Login exitoso")
    void testLoginContractSuccess() throws Exception {
        // Arrange - Definir el contrato esperado
        AuthResponse expectedResponse = new AuthResponse();
        expectedResponse.setToken("eyJhbGciOiJIUzUxMiJ9.mock-token");
        expectedResponse.setTipo("Bearer");
        expectedResponse.setUsuarioId(1L);
        expectedResponse.setUsername("testuser");
        expectedResponse.setEmail("test@email.com");

        stubFor(post(urlEqualTo("/auth/login"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(matchingJsonPath("$.username"))
                .withRequestBody(matchingJsonPath("$.password"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedResponse))));

        // Act
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                baseUrl + "/auth/login",
                entity,
                AuthResponse.class
        );

        // Assert - Verificar el contrato
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Bearer", response.getBody().getTipo());
        assertNotNull(response.getBody().getToken());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("test@email.com", response.getBody().getEmail());

        // Verificar que el request cumple con el contrato
        verify(postRequestedFor(urlEqualTo("/auth/login"))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(matchingJsonPath("$.username", equalTo("testuser")))
                .withRequestBody(matchingJsonPath("$.password", equalTo("password123"))));
    }

    @Test
    @DisplayName("Contrato POST /auth/login - Credenciales inválidas (401)")
    void testLoginContractUnauthorized() {
        // Arrange
        stubFor(post(urlEqualTo("/auth/login"))
                .withRequestBody(matchingJsonPath("$.username", equalTo("wronguser")))
                .willReturn(aResponse()
                        .withStatus(401)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Credenciales invalidas\"}")));

        // Act
        LoginRequest request = new LoginRequest();
        request.setUsername("wronguser");
        request.setPassword("wrongpassword");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/auth/login",
                    entity,
                    String.class
            );
            fail("Should have thrown exception");
        } catch (Exception e) {
            // Expected
        }

        // Assert
        verify(postRequestedFor(urlEqualTo("/auth/login"))
                .withRequestBody(matchingJsonPath("$.username", equalTo("wronguser"))));
    }

    @Test
    @DisplayName("Contrato POST /auth/register - Registro exitoso")
    void testRegisterContractSuccess() throws Exception {
        // Arrange
        AuthResponse expectedResponse = new AuthResponse();
        expectedResponse.setToken("new-user-token");
        expectedResponse.setTipo("Bearer");
        expectedResponse.setUsuarioId(2L);
        expectedResponse.setUsername("newuser");
        expectedResponse.setEmail("newuser@email.com");

        stubFor(post(urlEqualTo("/auth/register"))
                .withRequestBody(matchingJsonPath("$.username"))
                .withRequestBody(matchingJsonPath("$.password"))
                .withRequestBody(matchingJsonPath("$.email"))
                .withRequestBody(matchingJsonPath("$.nombre"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(objectMapper.writeValueAsString(expectedResponse))));

        // Act
        String requestBody = """
                {
                    "username": "newuser",
                    "password": "Password123!",
                    "email": "newuser@email.com",
                    "nombre": "New User"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                baseUrl + "/auth/register",
                entity,
                AuthResponse.class
        );

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newuser", response.getBody().getUsername());
        assertEquals("newuser@email.com", response.getBody().getEmail());

        // Verificar estructura del request
        verify(postRequestedFor(urlEqualTo("/auth/register"))
                .withRequestBody(matchingJsonPath("$.username"))
                .withRequestBody(matchingJsonPath("$.password"))
                .withRequestBody(matchingJsonPath("$.email"))
                .withRequestBody(matchingJsonPath("$.nombre")));
    }

    @Test
    @DisplayName("Contrato POST /auth/register - Usuario ya existe (400)")
    void testRegisterContractUserExists() {
        // Arrange
        stubFor(post(urlEqualTo("/auth/register"))
                .withRequestBody(matchingJsonPath("$.username", equalTo("existinguser")))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"message\": \"Usuario ya existe\"}")));

        // Act & Assert
        String requestBody = """
                {
                    "username": "existinguser",
                    "password": "Password123!",
                    "email": "existing@email.com",
                    "nombre": "Existing User"
                }
                """;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/auth/register",
                    entity,
                    String.class
            );
            fail("Should have thrown exception");
        } catch (Exception e) {
            // Expected
        }

        verify(postRequestedFor(urlEqualTo("/auth/register")));
    }

    @Test
    @DisplayName("Contrato - Validar campos obligatorios en login")
    void testLoginContractMissingFields() {
        // Arrange
        stubFor(post(urlEqualTo("/auth/login"))
                .willReturn(aResponse()
                        .withStatus(400)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"errors\": [\"username is required\", \"password is required\"]}")));

        // Act
        String emptyRequest = "{}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>(emptyRequest, headers);

        try {
            restTemplate.postForEntity(
                    baseUrl + "/auth/login",
                    entity,
                    String.class
            );
            fail("Should have thrown exception");
        } catch (Exception e) {
            // Expected
        }

        // Assert
        verify(postRequestedFor(urlEqualTo("/auth/login")));
    }

    @Test
    @DisplayName("Contrato - Response debe incluir todos los campos esperados")
    void testLoginContractResponseSchema() throws Exception {
        // Arrange
        String responseBody = """
                {
                    "token": "eyJhbGciOiJIUzUxMiJ9.test-token",
                    "tipo": "Bearer",
                    "usuarioId": 1,
                    "username": "testuser",
                    "email": "test@email.com"
                }
                """;

        stubFor(post(urlEqualTo("/auth/login"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(responseBody)));

        // Act
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                baseUrl + "/auth/login",
                entity,
                AuthResponse.class
        );

        // Assert - Verificar que todos los campos del contrato están presentes
        AuthResponse body = response.getBody();
        assertNotNull(body);
        assertNotNull(body.getToken(), "token es obligatorio en el contrato");
        assertNotNull(body.getTipo(), "tipo es obligatorio en el contrato");
        assertNotNull(body.getUsuarioId(), "usuarioId es obligatorio en el contrato");
        assertNotNull(body.getUsername(), "username es obligatorio en el contrato");
        assertNotNull(body.getEmail(), "email es obligatorio en el contrato");
    }

    @Test
    @DisplayName("Contrato - Content-Type debe ser application/json")
    void testLoginContractContentType() {
        // Arrange
        stubFor(post(urlEqualTo("/auth/login"))
                .withHeader("Content-Type", equalTo("application/json"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"token\": \"test\"}")));

        // Act
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password123");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<LoginRequest> entity = new HttpEntity<>(request, headers);

        restTemplate.postForEntity(baseUrl + "/auth/login", entity, String.class);

        // Assert
        verify(postRequestedFor(urlEqualTo("/auth/login"))
                .withHeader("Content-Type", equalTo("application/json")));
    }
}

