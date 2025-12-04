# Pruebas Unitarias - JUnit + Mockito
Solo usan **mocks en memoria**, lo que las hace muy rápidas.

- ❌ TestContainers
- ❌ Servicios externos
- ❌ Servidor web
- ❌ Base de datos
Las pruebas unitarias **NO** requieren:

## 🔧 Configuración

- **Tiempo de ejecución**: < 5 segundos
- **Cobertura de ramas**: > 70%
- **Cobertura de líneas**: > 80%

## 📊 Métricas Esperadas

6. **DisplayName**: Descripciones legibles de cada prueba
5. **Verificaciones**: Usar `verify()` para confirmar interacciones
4. **Mocks limpios**: Reset de mocks entre pruebas con @BeforeEach
3. **Un concepto por prueba**: Cada test valida una sola cosa
2. **Nombres descriptivos**: `testMetodo_Escenario_ResultadoEsperado`
1. **AAA Pattern**: Arrange, Act, Assert

## 💡 Buenas Prácticas

```
}
    verify(jwtUtil).generateToken(any());
    assertEquals("mock-jwt-token", response.getToken());
    assertNotNull(response);
    // Assert

    AuthResponse response = authService.autenticarUsuario(loginRequest);
    // Act

            .thenReturn("mock-jwt-token");
    when(jwtUtil.generateToken(any()))
            .thenReturn(userDetails);
    when(userDetailsService.loadUserByUsername(anyString()))
            .thenReturn(mock(Authentication.class));
    when(authenticationManager.authenticate(any()))
    // Arrange
void testAutenticarUsuario_Success() {
@DisplayName("Debe autenticar usuario correctamente")
@Test
```java

## 🔍 Ejemplo de Prueba

- ✅ Manejo de errores
- ✅ Códigos de estado HTTP
- ✅ Validación de requests
- ✅ Endpoints de login y registro
### AuthControllerTest

- ✅ Textos largos
- ✅ Caracteres especiales y Unicode
- ✅ Validación de entrada nula/vacía
- ✅ Manejo de diferentes tipos de texto
- ✅ Cifrado y descifrado de datos
### EncryptionServiceTest

- ✅ Manejo de errores
- ✅ Validación de permisos
- ✅ Cifrado automático de datos sensibles
- ✅ Creación de eventos
- ✅ Obtención de calendarios del usuario
- ✅ Creación de calendarios
### CalendarServiceTest

- ✅ Validación de tokens
- ✅ Generación de tokens JWT
- ✅ Validación de usuarios/emails duplicados
- ✅ Registro de nuevos usuarios
- ✅ Manejo de credenciales inválidas
- ✅ Autenticación exitosa de usuarios
### AuthServiceTest

## 📝 Cobertura de Pruebas

```
mvn test -Dtest="AuthServiceTest"
```bash
### Una clase específica

```
mvn test -Dtest="org.example.unit.controller.**"
```bash
### Solo pruebas de controladores

```
mvn test -Dtest="org.example.unit.service.**"
```bash
### Solo pruebas de servicios

```
mvn test -Dtest="org.example.unit.**"
```bash
### Todas las pruebas unitarias

## 🚀 Ejecutar Pruebas

- **MockMvc**: Para pruebas de controladores
- **@InjectMocks**: Inyectar mocks en la clase bajo prueba
- **@Mock**: Crear mocks de dependencias
- **@ExtendWith(MockitoExtension.class)**: Integración JUnit + Mockito
- **Mockito**: Framework de mocking
- **JUnit 5**: Framework de pruebas

## 🛠️ Tecnologías

Las pruebas unitarias verifican el comportamiento de **componentes individuales** de forma aislada, usando **mocks** para las dependencias.

## 🎯 Objetivo

```
    └── AuthControllerTest.java        # Pruebas del controlador de autenticación
└── controller/
│   └── EncryptionServiceTest.java     # Pruebas del servicio de cifrado
│   ├── CalendarServiceTest.java       # Pruebas del servicio de calendario
│   ├── AuthServiceTest.java           # Pruebas del servicio de autenticación
├── service/
unit/
```

## 📁 Estructura

Esta carpeta contiene las pruebas unitarias del sistema utilizando **JUnit 5** y **Mockito**.


