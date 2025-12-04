# Pruebas de Contrato - WireMock
- [REST Assured](https://rest-assured.io/)
- [Consumer-Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html)
- [WireMock Documentation](http://wiremock.org/docs/)

## 📚 Recursos

| **E2E** | Sistema completo | 🐌 Lenta | ✅ Todo |
| **Contrato** | Cumplimiento de API | ⚡ Rápida | ❌ Mocks |
| **Integración** | Componentes juntos | 🐢 Moderada | ✅ BD, servicios |
| **Unitarias** | Componente aislado | ⚡ Muy rápida | ❌ Ninguna |
|------|----------|-----------|--------------|
| Tipo | Objetivo | Velocidad | Dependencias |

## 🔍 Diferencias con Otros Tipos de Prueba

6. **Documentar cambios**: Actualizar contratos cuando cambia el API
5. **Validar todos los campos**: No solo happy path
4. **Escenarios realistas**: Simular casos del mundo real
3. **Pruebas independientes**: No depender de orden de ejecución
2. **Versionar contratos**: Mantener compatibilidad con versiones anteriores
1. **Definir contratos explícitos**: Documentar estructura esperada

## 🎓 Mejores Prácticas

- **OpenAPI**: Generación de contratos desde spec
- **Spring Cloud Contract**: Integración con Spring
- **Pact**: Framework específico para CDC
### Herramientas Complementarias

4. **CI/CD** ejecuta tests en cada cambio
3. **Tests** verifican que Provider cumple contrato
2. **Provider** implementa según contrato
1. **Consumer** define contrato esperado
### Workflow

## 🔗 Consumer-Driven Contract Testing

- ✅ Detectan breaking changes
- ✅ Deterministas (sin flakiness)
- ✅ Ejecución rápida (< 10 segundos)
- ✅ No requieren servicios externos
Las pruebas de contrato son ideales para CI/CD:

## 📈 Integración con CI/CD

```
                .withTransformers("response-template")));
        .willReturn(aResponse()
stubFor(post(urlMatching("/.*"))
```java
### Respuestas Dinámicas

```
                .withFault(Fault.CONNECTION_RESET_BY_PEER)));
        .willReturn(aResponse()
stubFor(get(urlEqualTo("/error"))
```java
### Errores de Red

```
                .withFixedDelay(5000)));
        .willReturn(aResponse()
stubFor(get(urlEqualTo("/slow"))
```java
### Timeout

## 🔧 Configuración Avanzada

| **Verificación** | Confirmar requests realizados |
| **Escenarios complejos** | Simular errores, timeouts, etc. |
| **Determinístico** | Siempre produce los mismos resultados |
| **Rápido** | No hay latencia de red real |
| **Sin dependencias** | No requiere servicios externos |
|---------|-------------|
| Ventaja | Descripción |

## 📊 Ventajas de WireMock

Frontend y Backend pueden trabajar con el contrato antes de implementar.
### 4. Desarrollo Paralelo

Simular errores 4xx, 5xx sin afectar sistemas reales.
### 3. Pruebas de Escenarios de Error

Probar integraciones sin depender de servicios reales (Push, Email, SMS).
### 2. Simulación de Servicios Externos

Verificar que el API cumple con las expectativas de los consumidores.
### 1. Consumer-Driven Contracts

## 🎯 Casos de Uso

```
.withRequestBody(matchingJsonPath("$.username", equalTo("testuser")))
```java
Validación de partes del request:
### Matching

```
        .withHeader("Authorization", equalTo("Bearer token")));
verify(getRequestedFor(urlEqualTo("/calendar"))
```java
Confirma que se hizo el request esperado:
### Verificación

```
                .withBody(responseJson)));
                .withStatus(200)
        .willReturn(aResponse()
        .withHeader("Authorization", equalTo("Bearer token"))
stubFor(get(urlEqualTo("/calendar"))
```java
Define cómo debe responder WireMock a un request:
### Stub (Simulación)

## 💡 Conceptos Clave

```
}
            .withRequestBody(matchingJsonPath("$.password")));
            .withRequestBody(matchingJsonPath("$.username"))
    verify(postRequestedFor(urlEqualTo("/auth/login"))
    // Verificar que el request cumple con el contrato
    
    assertNotNull(response.getBody().getToken());
    assertEquals(HttpStatus.OK, response.getStatusCode());
    // Assert - Verificar el contrato

    );
            AuthResponse.class
            request,
            baseUrl + "/auth/login",
    ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
    // Act

                    .withBody(expectedResponse)));
                    .withHeader("Content-Type", "application/json")
                    .withStatus(200)
            .willReturn(aResponse()
            .withRequestBody(matchingJsonPath("$.password"))
            .withRequestBody(matchingJsonPath("$.username"))
            .withHeader("Content-Type", equalTo("application/json"))
    stubFor(post(urlEqualTo("/auth/login"))
    // Arrange - Definir el contrato esperado
void testLoginContractSuccess() {
@DisplayName("Contrato POST /auth/login - Login exitoso")
@Test
```java

## 🔍 Ejemplo de Prueba

- ✅ Validación de contratos externos
- ✅ Simulación de proveedores externos
- ✅ Validación de tipos de notificación (PUSH, EMAIL, SMS)
- ✅ POST /notifications/shown - Confirmar entrega (200)
- ✅ GET /alerts - Obtener alertas (200)
- ✅ POST /alerts - Crear alerta (200)
### NotificationApiContractTest

- ✅ Validación de campos obligatorios
- ✅ Validación de token JWT
- ✅ GET /calendar/view - Vista con descifrado (200)
- ✅ POST /calendar/events - Crear evento (200)
- ✅ POST /calendar - Crear calendario (200)
- ✅ GET /calendar - Sin autenticación (401)
- ✅ GET /calendar - Obtener calendarios (200)
### CalendarApiContractTest

- ✅ Validación de schema de response
- ✅ Validación de Content-Type
- ✅ Validación de campos obligatorios
- ✅ POST /auth/register - Usuario ya existe (400)
- ✅ POST /auth/register - Registro exitoso (200)
- ✅ POST /auth/login - Credenciales inválidas (401)
- ✅ POST /auth/login - Login exitoso (200)
### AuthApiContractTest

## 📝 Cobertura de Contratos

```
mvn test -Dtest="NotificationApiContractTest"
```bash
### Solo contratos de Notifications

```
mvn test -Dtest="CalendarApiContractTest"
```bash
### Solo contratos de Calendar

```
mvn test -Dtest="AuthApiContractTest"
```bash
### Solo contratos de Auth

```
mvn test -Dtest="org.example.contract.**"
```bash
### Todas las pruebas de contrato

## 🚀 Ejecutar Pruebas

```
}
    wireMockServer.start();
    wireMockServer = new WireMockServer(8089);
static void setupServer() {
@BeforeAll
```java

WireMock levanta un servidor HTTP en el puerto 8089:

## 🌐 WireMock Server

- **JUnit 5**: Framework de pruebas
- **ObjectMapper**: Serialización/deserialización JSON
- **RestTemplate**: Cliente HTTP de Spring
- **WireMock**: Servidor HTTP mock para simular APIs

## 🛠️ Tecnologías

4. **Los consumidores del API pueden confiar en la estructura de datos**
3. **Los cambios no rompen el contrato establecido**
2. **Las integraciones con servicios externos funcionan correctamente**
1. **El API cumple con el contrato especificado** (request/response schemas)
Las pruebas de contrato verifican que:

## 🎯 Objetivo

```
    └── NotificationApiContractTest.java  # Contratos de notificaciones
└── notifications/
│   └── CalendarApiContractTest.java      # Contratos de calendario
├── calendar/
│   └── AuthApiContractTest.java          # Contratos de autenticación
├── auth/
contract/
```

## 📁 Estructura

Esta carpeta contiene las pruebas de contrato del sistema utilizando **WireMock** para simular APIs externas.


