# 🎯 Guía Rápida de Comandos - Ejecución de Tests

## 🚀 Script Automatizado (Recomendado)

```bash
# Dar permisos de ejecución (solo una vez)
chmod +x run_tests.sh

# Pruebas unitarias (más rápidas)
./run_tests.sh unit

# Pruebas de integración (requiere Docker)
./run_tests.sh integration

# Pruebas de contrato
./run_tests.sh contract

# Todos los tests
./run_tests.sh all

# Con reporte de cobertura
./run_tests.sh coverage

# Limpiar y solucionar problema Lombok
./run_tests.sh clean
```

---

## 📝 Comandos Maven Directos

### Pruebas Unitarias

```bash
# Todas las pruebas unitarias
mvn test -Dtest="org.example.unit.**"

# Solo AuthService
mvn test -Dtest="AuthServiceTest"

# Solo CalendarService
mvn test -Dtest="CalendarServiceTest"

# Solo EncryptionService
mvn test -Dtest="EncryptionServiceTest"

# Solo AuthController
mvn test -Dtest="AuthControllerTest"

# Un método específico
mvn test -Dtest="AuthServiceTest#testAutenticarUsuario_Success"
```

### Pruebas de Integración

```bash
# Verificar que Docker está corriendo primero
docker ps

# Todas las pruebas de integración
mvn test -Dtest="org.example.integration.**"

# Solo repositories
mvn test -Dtest="org.example.integration.repository.**"

# Solo UsuarioRepository
mvn test -Dtest="UsuarioRepositoryIntegrationTest"

# Solo CalendarioRepository
mvn test -Dtest="CalendarioRepositoryIntegrationTest"

# Solo AuthService integración
mvn test -Dtest="AuthServiceIntegrationTest"
```

### Pruebas de Contrato

```bash
# Todas las pruebas de contrato
mvn test -Dtest="org.example.contract.**"

# Solo contratos de Auth
mvn test -Dtest="AuthApiContractTest"

# Solo contratos de Calendar
mvn test -Dtest="CalendarApiContractTest"

# Solo contratos de Notifications
mvn test -Dtest="NotificationApiContractTest"
```

### Combinaciones

```bash
# Unitarias + Contrato (sin Docker)
mvn test -Dtest="org.example.unit.**,org.example.contract.**"

# Solo servicios (unit + integration)
mvn test -Dtest="*ServiceTest,*ServiceIntegrationTest"

# Todos los tests de Auth
mvn test -Dtest="*Auth*Test"
```

---

## 🔧 Solución de Problemas

### Error de Compilación (Lombok)

```bash
# Opción 1: Limpiar completamente
rm -rf target/
rm -rf ~/.m2/repository/org/projectlombok/
mvn clean
mvn install -DskipTests

# Opción 2: Usar el script
./run_tests.sh clean

# Opción 3: Compilar sin annotation processing
mvn clean compile -Dmaven.compiler.proc=none
```

### Docker no está corriendo

```bash
# Verificar estado
docker ps

# macOS: Iniciar Docker Desktop
open -a Docker

# Linux: Iniciar servicio
sudo systemctl start docker

# Verificar nuevamente
docker ps
```

### Tests fallan por caché

```bash
# Limpiar todo y recompilar
mvn clean install -DskipTests
mvn test -Dtest="org.example.unit.**"
```

---

## 📊 Reporte de Cobertura

```bash
# Generar reporte de cobertura
mvn clean test jacoco:report

# Ver reporte (macOS)
open target/site/jacoco/index.html

# Ver reporte (Linux)
xdg-open target/site/jacoco/index.html

# O con el script
./run_tests.sh coverage
```

---

## 🎯 Ejecución por Prioridad

### Desarrollo Rápido (< 10 seg)
```bash
# Solo unitarias mientras desarrollas
mvn test -Dtest="org.example.unit.**"
```

### Antes de Commit (< 30 seg)
```bash
# Unitarias + Contratos (sin Docker)
mvn test -Dtest="org.example.unit.**,org.example.contract.**"
```

### Antes de Push (< 60 seg)
```bash
# Todos los tests
./run_tests.sh all
```

### Antes de Release
```bash
# Todos + Cobertura
./run_tests.sh coverage
```

---

## 🔍 Depuración de Tests

### Ver output detallado

```bash
# Maven verbose
mvn test -Dtest="AuthServiceTest" -X

# Con logs de Spring
mvn test -Dtest="AuthServiceTest" -Dlogging.level.org.example=DEBUG
```

### Ejecutar un solo test con debug

```bash
# En IntelliJ IDEA:
# 1. Abrir clase de test
# 2. Click derecho en el método de test
# 3. "Debug 'testMetodo()'"

# En Maven con debug remoto:
mvn test -Dtest="AuthServiceTest" -Dmaven.surefire.debug
# Conectar debugger en puerto 5005
```

### Ver por qué falla un test

```bash
# Maven con stack traces completos
mvn test -Dtest="AuthServiceTest" -Dsurefire.printSummary=true

# Ver logs de Surefire
cat target/surefire-reports/*.txt
```

---

## 📈 Monitoreo en Tiempo Real

### Ver tests ejecutándose

```bash
# En otra terminal
watch -n 1 'ls -lh target/surefire-reports/*.txt 2>/dev/null | tail -10'
```

### Contar tests ejecutados

```bash
# Después de ejecutar
grep -r "Tests run:" target/surefire-reports/*.txt

# Resumen
grep -r "Tests run:" target/surefire-reports/*.txt | \
  awk '{sum+=$4} END {print "Total tests:", sum}'
```

---

## ⚙️ Configuración Personalizada

### Ejecutar tests en paralelo

```bash
# 4 threads en paralelo
mvn test -T 4

# Un thread por core
mvn test -T 1C
```

### Cambiar profile de test

```bash
# Usar profile específico
mvn test -Ptest-profile

# Con propiedades custom
mvn test -Dspring.profiles.active=test
```

### Skip de ciertos tests

```bash
# Skip integration tests temporalmente
mvn test -Dtest="org.example.unit.**"

# Skip todos los tests
mvn install -DskipTests

# Compilar sin tests
mvn clean compile -DskipTests
```

---

## 📚 Recursos Adicionales

### Documentación
- `RESUMEN_EJECUTIVO_TESTS.md` - Resumen completo
- `RESUMEN_TESTS_CREADOS.md` - Detalles técnicos
- `SOLUCION_ERROR_LOMBOK.md` - Troubleshooting
- `src/test/java/org/example/README.md` - Guía principal
- `src/test/java/org/example/unit/README.md` - Tests unitarios
- `src/test/java/org/example/integration/README.md` - Tests integración
- `src/test/java/org/example/contract/README.md` - Tests contrato

### Ayuda
```bash
# Ayuda del script
./run_tests.sh help

# Ayuda de Maven
mvn help:describe -Dplugin=surefire

# Ver effective pom
mvn help:effective-pom
```

---

## ✅ Checklist Antes de Ejecutar

- [ ] Java 17+ instalado (`java -version`)
- [ ] Maven 3.6+ instalado (`mvn -version`)
- [ ] Docker corriendo (solo para integración) (`docker ps`)
- [ ] Proyecto compilado (`mvn clean install -DskipTests`)
- [ ] Lombok configurado (ver `SOLUCION_ERROR_LOMBOK.md`)

---

## 🎯 Comandos más Usados

```bash
# Top 5 comandos que usarás:

# 1. Desarrollo rápido
mvn test -Dtest="org.example.unit.**"

# 2. Antes de commit
./run_tests.sh unit

# 3. Antes de push
./run_tests.sh all

# 4. Verificar cobertura
./run_tests.sh coverage

# 5. Solucionar problemas
./run_tests.sh clean
```

---

**¡Feliz Testing! 🚀**

