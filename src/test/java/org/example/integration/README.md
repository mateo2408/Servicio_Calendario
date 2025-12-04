# Pruebas de Integración - TestContainers + PostgreSQL

Esta carpeta contiene las pruebas de integración del sistema utilizando **TestContainers** con **PostgreSQL real**.

## 📁 Estructura

```
integration/
├── repository/
│   ├── UsuarioRepositoryIntegrationTest.java
│   └── CalendarioRepositoryIntegrationTest.java
└── service/
    └── AuthServiceIntegrationTest.java
```

## 🎯 Objetivo

Las pruebas de integración verifican que **múltiples componentes funcionen correctamente juntos**, usando una base de datos real en un contenedor Docker.

## 🛠️ Tecnologías

- **TestContainers**: Contenedores Docker para pruebas
- **PostgreSQL 15**: Base de datos real
- **@DataJpaTest**: Configuración automática de JPA
- **@SpringBootTest**: Contexto completo de Spring
- **TestEntityManager**: Manipulación de entidades en pruebas
- **@Transactional**: Rollback automático después de cada prueba

## 🐳 TestContainers

TestContainers inicia automáticamente un contenedor PostgreSQL para las pruebas:

```java
@Container
static PostgreSQLContainer<?> postgres = 
    new PostgreSQLContainer<>("postgres:15-alpine")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test");
```

## 🚀 Ejecutar Pruebas

### Prerrequisito: Docker
```bash
# Verificar que Docker está corriendo
docker --version
docker ps
```

### Todas las pruebas de integración
```bash
mvn test -Dtest="org.example.integration.**"
```

### Solo repositorios
```bash
mvn test -Dtest="org.example.integration.repository.**"
```

### Solo servicios
```bash
mvn test -Dtest="org.example.integration.service.**"
```

### Una clase específica
```bash
mvn test -Dtest="UsuarioRepositoryIntegrationTest"
```

## 📝 Cobertura de Pruebas

### UsuarioRepositoryIntegrationTest
- ✅ CRUD completo de usuarios
- ✅ Búsqueda por username y email
- ✅ Validación de constraints únicos
- ✅ Verificación de existencia
- ✅ Actualización y eliminación
- ✅ Integridad referencial

### CalendarioRepositoryIntegrationTest
- ✅ Creación con relación a usuario
- ✅ Búsqueda por usuario ID
- ✅ Filtrado de calendarios públicos
- ✅ Cascada de eliminación con eventos
- ✅ Actualización de propiedades
- ✅ Conteo y búsqueda por nombre

### AuthServiceIntegrationTest
- ✅ Flujo completo: Registro → Login
- ✅ Persistencia en BD real
- ✅ Cifrado de passwords
- ✅ Validación de duplicados
- ✅ Actualización de último acceso
- ✅ Generación de tokens diferentes

## 🔍 Ejemplo de Prueba

```java
@Test
@DisplayName("Flujo completo: Registro -> Login")
void testFlujoCompletoRegistroYLogin() {
    // 1. Registrar usuario
    AuthResponse registroResponse = authService.registrarUsuario(request);
    assertNotNull(registroResponse.getToken());

    // 2. Verificar persistencia en BD
    Usuario usuario = usuarioRepository.findByUsername("user").orElseThrow();
    assertNotNull(usuario);

    // 3. Login
    AuthResponse loginResponse = authService.autenticarUsuario(loginRequest);
    assertNotNull(loginResponse.getToken());

    // 4. Verificar actualización de último acceso
    Usuario actualizado = usuarioRepository.findByUsername("user").orElseThrow();
    assertNotNull(actualizado.getUltimoAcceso());
}
```

## 💡 Ventajas de TestContainers

1. **Base de datos real**: PostgreSQL real, no H2
2. **Aislamiento**: Cada prueba usa una BD limpia
3. **Portabilidad**: Funciona en cualquier máquina con Docker
4. **CI/CD**: Se puede ejecutar en pipelines
5. **Sin setup manual**: No requiere instalar PostgreSQL

## ⚙️ Configuración Dinámica

Las propiedades se configuran dinámicamente:

```java
@DynamicPropertySource
static void configureProperties(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", postgres::getJdbcUrl);
    registry.add("spring.datasource.username", postgres::getUsername);
    registry.add("spring.datasource.password", postgres::getPassword);
}
```

## 📊 Métricas Esperadas

- **Cobertura de integración**: > 70%
- **Tiempo de ejecución**: 10-30 segundos (incluye inicio de contenedor)
- **Confiabilidad**: 100% (con Docker disponible)

## 🔧 Troubleshooting

### Docker no está corriendo
```bash
# macOS
open -a Docker

# Linux
sudo systemctl start docker
```

### Error de permisos de Docker
```bash
sudo usermod -aG docker $USER
newgrp docker
```

### Puerto ocupado
TestContainers usa puertos aleatorios, no debería haber conflictos.

## 🎯 Diferencias con Pruebas Unitarias

| Aspecto | Unitarias | Integración |
|---------|-----------|-------------|
| BD Real | ❌ | ✅ |
| Mocks | ✅ Muchos | ❌ Mínimos |
| Velocidad | Muy rápida | Moderada |
| Alcance | Un componente | Múltiples |
| Aislamiento | Total | Parcial |

