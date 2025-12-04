# 🧪 Guía Completa de Pruebas - Sistema de Calendario

Esta carpeta contiene todas las pruebas automatizadas del sistema, organizadas por tipo y propósito.

## 📁 Estructura General

```
test/
├── java/org/example/
│   ├── unit/                    # Pruebas unitarias (JUnit + Mockito)
│   │   ├── service/
│   │   │   ├── AuthServiceTest.java
│   │   │   ├── CalendarServiceTest.java
│   │   │   └── EncryptionServiceTest.java
│   │   ├── controller/
│   │   │   └── AuthControllerTest.java
│   │   └── README.md
│   │
│   ├── integration/             # Pruebas de integración (TestContainers + BD)
│   │   ├── repository/
│   │   │   ├── UsuarioRepositoryIntegrationTest.java
│   │   │   └── CalendarioRepositoryIntegrationTest.java
│   │   ├── service/
│   │   │   └── AuthServiceIntegrationTest.java
│   │   └── README.md
│   │
│   └── contract/                # Pruebas de contrato (WireMock)
│       ├── auth/
│       │   └── AuthApiContractTest.java
│       ├── calendar/
│       │   └── CalendarApiContractTest.java
│       ├── notifications/
│       │   └── NotificationApiContractTest.java
│       └── README.md
│
└── resources/
    ├── application-test.yml     # Configuración para pruebas
    └── wiremock/                # Stubs de WireMock (opcional)
```

## 🎯 Tipos de Pruebas

### 1. Pruebas Unitarias (unit/)
**Objetivo**: Verificar componentes individuales de forma aislada

**Tecnologías**:
- JUnit 5
- Mockito
- MockMvc

**Características**:
- ⚡ Muy rápidas (< 5 segundos)
- ❌ Sin dependencias externas
- 🎯 Cobertura > 80%

**Cuándo ejecutar**: En cada cambio de código, antes de commit

**Comando**:
```bash
mvn test -Dtest="org.example.unit.**"
```

---

### 2. Pruebas de Integración (integration/)
**Objetivo**: Verificar que múltiples componentes funcionen juntos

**Tecnologías**:
- TestContainers
- PostgreSQL
- Spring Boot Test

**Características**:
- 🐢 Moderadas (10-30 segundos)
- ✅ Base de datos real
- 🔒 Transacciones reales

**Cuándo ejecutar**: Antes de merge a main, en CI/CD

**Comando**:
```bash
mvn test -Dtest="org.example.integration.**"
```

**Prerrequisito**: Docker debe estar corriendo

---

### 3. Pruebas de Contrato (contract/)
**Objetivo**: Verificar contratos de API y simular servicios externos

**Tecnologías**:
- WireMock
- RestTemplate
- Consumer-Driven Contracts

**Características**:
- ⚡ Rápidas (< 10 segundos)
- 🌐 Simula APIs externas
- 📝 Valida contratos

**Cuándo ejecutar**: Antes de cambios en API, en CI/CD

**Comando**:
```bash
mvn test -Dtest="org.example.contract.**"
```

---

## 🚀 Comandos Rápidos

### Ejecutar todas las pruebas
```bash
mvn clean test
```

### Solo un tipo de prueba
```bash
# Unitarias
mvn test -Dtest="org.example.unit.**"

# Integración
mvn test -Dtest="org.example.integration.**"

# Contratos
mvn test -Dtest="org.example.contract.**"
```

### Una clase específica
```bash
mvn test -Dtest="AuthServiceTest"
```

### Un método específico
```bash
mvn test -Dtest="AuthServiceTest#testAutenticarUsuario_Success"
```

### Con reporte de cobertura
```bash
mvn clean test jacoco:report
```
El reporte estará en: `target/site/jacoco/index.html`

### Ejecutar en paralelo (más rápido)
```bash
mvn test -T 4
```

---

## 📊 Pirámide de Pruebas

```
        /\
       /  \        E2E (Manual/Opcional)
      /____\
     /      \      
    / Contract\     Pruebas de Contrato (30 tests)
   /___________\   
  /             \
 /  Integration  \  Pruebas de Integración (20 tests)
/________________\
/                 \
/     Unit Tests   \ Pruebas Unitarias (50+ tests)
/___________________\
```

**Distribución recomendada**:
- 70% Unitarias (rápidas, muchas)
- 20% Integración (moderadas, importantes)
- 10% Contrato (rápidas, críticas para API)

---

## 📈 Métricas y Objetivos

| Métrica | Objetivo | Actual |
|---------|----------|--------|
| Cobertura de líneas | > 80% | - |
| Cobertura de ramas | > 70% | - |
| Tiempo total | < 1 min | - |
| Tests que pasan | 100% | - |

---

## 🔧 Configuración

### Prerrequisitos

1. **Java 17+**
```bash
java -version
```

2. **Maven 3.6+**
```bash
mvn -version
```

3. **Docker** (solo para pruebas de integración)
```bash
docker --version
docker ps
```

### Dependencias Principales

Todas las dependencias ya están en `pom.xml`:

```xml
<!-- JUnit 5 + Mockito -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- TestContainers -->
<dependency>
    <groupId>org.testcontainers</groupId>
    <artifactId>postgresql</artifactId>
    <version>1.19.3</version>
    <scope>test</scope>
</dependency>

<!-- WireMock -->
<dependency>
    <groupId>org.wiremock</groupId>
    <artifactId>wiremock-standalone</artifactId>
    <version>3.3.1</version>
    <scope>test</scope>
</dependency>
```

---

## 🎓 Guía de Uso

### Para Desarrolladores

**Al desarrollar nueva funcionalidad**:
1. Escribir pruebas unitarias primero (TDD)
2. Implementar la funcionalidad
3. Agregar pruebas de integración si aplica
4. Actualizar pruebas de contrato si cambió API
5. Ejecutar todas las pruebas localmente
6. Commit + Push

### Para Code Review

**Verificar**:
- ✅ Todas las pruebas pasan
- ✅ Cobertura no disminuye
- ✅ Pruebas nuevas para código nuevo
- ✅ Nombres descriptivos
- ✅ Sin tests comentados o ignorados

### Para CI/CD

**Pipeline recomendado**:
```yaml
stages:
  - test:unit        # Rápidas, siempre
  - test:integration # Moderadas, en PR
  - test:contract    # Rápidas, antes de deploy
  - build
  - deploy
```

---

## 🐛 Troubleshooting

### Tests fallan localmente

1. **Limpiar y recompilar**
```bash
mvn clean install -DskipTests
mvn test
```

2. **Verificar versión de Java**
```bash
java -version  # Debe ser 17+
```

3. **Verificar Docker (para integración)**
```bash
docker ps
# Si no está corriendo: open -a Docker (macOS)
```

### Tests lentos

1. **Ejecutar solo unitarias mientras desarrollas**
```bash
mvn test -Dtest="org.example.unit.**"
```

2. **Usar ejecución paralela**
```bash
mvn test -T 4
```

### TestContainers falla

1. **Verificar Docker**
```bash
docker info
```

2. **Limpiar contenedores**
```bash
docker ps -a | grep testcontainers | awk '{print $1}' | xargs docker rm -f
```

3. **Verificar puertos disponibles**
```bash
lsof -i :5432
```

---

## 📚 Recursos y Documentación

### Frameworks
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [TestContainers](https://www.testcontainers.org/)
- [WireMock](http://wiremock.org/docs/)

### Buenas Prácticas
- [Test Driven Development (TDD)](https://martinfowler.com/bliki/TestDrivenDevelopment.html)
- [Consumer-Driven Contracts](https://martinfowler.com/articles/consumerDrivenContracts.html)
- [Testing Pyramid](https://martinfowler.com/bliki/TestPyramid.html)

### Spring Testing
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [Spring Security Testing](https://docs.spring.io/spring-security/reference/servlet/test/index.html)

---

## 🎯 Checklist de Calidad

Antes de hacer commit:

- [ ] Todas las pruebas pasan localmente
- [ ] Cobertura de código > 80%
- [ ] Sin tests ignorados (@Disabled)
- [ ] Sin System.out.println() en tests
- [ ] Nombres descriptivos (@DisplayName)
- [ ] Sin sleeps o waits hardcodeados
- [ ] Sin dependencias de orden de ejecución
- [ ] Tests independientes entre sí

---

## 🤝 Contribuir

Al agregar nuevas pruebas:

1. **Ubicación correcta**: unit/ o integration/ o contract/
2. **Nombrado**: `*Test.java` para JUnit
3. **Package**: Mismo que la clase que prueba
4. **Documentación**: Agregar @DisplayName descriptivo
5. **README**: Actualizar este archivo si es necesario

---

## 📞 Soporte

¿Preguntas sobre las pruebas?
- Revisar README de cada carpeta (unit/, integration/, contract/)
- Ver ejemplos en las clases de test existentes
- Consultar documentación oficial de frameworks

---

**¡Feliz Testing! 🎉**

