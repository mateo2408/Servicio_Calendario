# 🚀 GUÍA RÁPIDA - Cómo Compilar y Ejecutar el Proyecto

## ⚠️ PROBLEMA ACTUAL

El proyecto tiene un **problema de incompatibilidad entre Maven 3.9.11 y Java 17** al compilar en línea de comandos:

```
Fatal error compiling: java.lang.ExceptionInInitializerError: 
com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

## ✅ SOLUCIONES DISPONIBLES

### 🎯 SOLUCIÓN 1: USAR INTELLIJ IDEA (RECOMENDADO ✨)

Esta es la forma **MÁS FÁCIL Y RÁPIDA** de compilar y ejecutar el proyecto:

#### Pasos:

1. **Abrir el proyecto en IntelliJ IDEA**:
   ```
   File → Open → Seleccionar carpeta: Servicio_Calendario
   ```

2. **Configurar SDK**:
   ```
   File → Project Structure (⌘ + ;)
   → Project → SDK: Seleccionar Java 17
   → Apply → OK
   ```

3. **Sincronizar Maven**:
   ```
   Botón "Load Maven Changes" (icono de recarga) en la esquina superior derecha
   O: Maven panel (derecha) → Reload All Maven Projects
   ```

4. **Compilar el proyecto**:
   ```
   Build → Build Project (⌘ + F9)
   ```

5. **Ejecutar la aplicación**:
   ```
   Buscar: CalendarioApplication.java
   Click derecho → Run 'CalendarioApplication'
   
   O usar el botón verde de Play ▶️
   ```

6. **Verificar que funciona**:
   ```
   Abrir navegador:
   - Swagger UI: http://localhost:8080/swagger-ui.html
   - H2 Console: http://localhost:8080/h2-console
   ```

**✅ VENTAJAS**:
- No requiere configuración adicional
- IntelliJ maneja Lombok automáticamente
- Mejor experiencia de desarrollo
- Debugging integrado

---

### 🔧 SOLUCIÓN 2: ACTUALIZAR MAVEN

Si prefieres usar Maven en línea de comandos:

```bash
# 1. Actualizar Maven
brew update
brew upgrade maven

# 2. Verificar versión
mvn --version

# 3. Limpiar y compilar
cd /Users/mateocisneros/IdeaProjects/Servicio_Calendario
rm -rf target
mvn clean compile

# 4. Si compila, ejecutar:
mvn spring-boot:run
```

---

### 🐳 SOLUCIÓN 3: USAR DOCKER (Ambiente Controlado)

Si las soluciones anteriores fallan, Docker garantiza un ambiente consistente:

#### Crear `Dockerfile`:

```dockerfile
FROM maven:3.8-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### Compilar y ejecutar:

```bash
# Construir imagen
docker build -t servicio-calendario .

# Ejecutar
docker run -p 8080:8080 servicio-calendario

# Acceder a:
# http://localhost:8080/swagger-ui.html
```

---

### 🛠️ SOLUCIÓN 4: CONFIGURACIÓN ALTERNATIVA DE MAVEN

Modificar `pom.xml` con versión específica del compilador:

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.10.1</version>
    <configuration>
        <release>17</release>
        <fork>true</fork>
        <annotationProcessorPaths>
            <path>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
                <version>1.18.30</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

Luego:
```bash
mvn clean compile
```

---

## 📊 COMPARACIÓN DE SOLUCIONES

| Solución | Dificultad | Tiempo | Recomendado |
|----------|-----------|--------|-------------|
| **IntelliJ IDEA** | ⭐ Fácil | 2 min | ✅ SÍ |
| **Actualizar Maven** | ⭐⭐ Media | 5 min | ⚠️ Puede funcionar |
| **Docker** | ⭐⭐⭐ Avanzada | 10 min | 🔄 Si todo falla |
| **Configurar Maven** | ⭐⭐⭐ Avanzada | 15 min | ❌ Último recurso |

---

## 🎯 RECOMENDACIÓN FINAL

### Para Desarrollo:
**👉 USA INTELLIJ IDEA** - Es la forma más confiable y productiva.

### Para Producción:
**👉 USA DOCKER** - Garantiza consistencia en cualquier ambiente.

---

## 📝 VERIFICACIÓN POST-EJECUCIÓN

Una vez que la aplicación esté corriendo, verifica:

### 1. Consola debe mostrar:
```
Started CalendarioApplication in X seconds
```

### 2. Swagger UI debe funcionar:
```
http://localhost:8080/swagger-ui.html
```

Deberías ver:
- 11 endpoints documentados
- POST /auth/login
- POST /auth/register
- POST /calendar
- etc.

### 3. H2 Console debe funcionar:
```
http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:calendardb
Username: sa
Password: (vacío)
```

### 4. Probar registro de usuario:

```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "nombre": "Usuario Test"
  }'
```

Deberías recibir un token JWT.

---

## 🆘 TROUBLESHOOTING

### Error: "Port 8080 is already in use"
```bash
# Encontrar y matar proceso
lsof -ti:8080 | xargs kill -9

# O cambiar puerto en application.yml
server:
  port: 8081
```

### Error: "Cannot find symbol" en IntelliJ
```
1. File → Invalidate Caches → Invalidate and Restart
2. Maven panel → Reload All Maven Projects
3. Build → Rebuild Project
```

### Error: Lombok no funciona
```
1. Preferences → Plugins → Buscar "Lombok" → Install
2. Preferences → Build → Compiler → Annotation Processors
3. ✅ Enable annotation processing
4. Restart IntelliJ IDEA
```

---

## 📚 DOCUMENTACIÓN ADICIONAL

- `README.md` - Documentación completa del proyecto
- `SOLUCION_ERROR_MAVEN_TYPETAG.md` - Detalles del error de Maven
- `ERRORES_CORREGIDOS_OPENAPI_JWTUTIL.md` - Historial de correcciones
- `INSTRUCCIONES_COMPLETAS.md` - Guía detallada del sistema

---

## 🎉 ÉXITO

Si ves esto en la consola:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.5)

Started CalendarioApplication in 3.456 seconds
```

**¡FELICIDADES! 🎉 El proyecto está funcionando correctamente.**

---

**Última actualización**: 26 de Noviembre, 2025  
**Recomendación**: 👉 **Usar IntelliJ IDEA para compilar y ejecutar**

