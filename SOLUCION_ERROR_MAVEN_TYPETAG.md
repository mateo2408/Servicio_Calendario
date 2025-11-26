# 🔧 SOLUCIÓN: Error de Compilación Maven - TypeTag::UNKNOWN

## ❌ Error Encontrado

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile
[ERROR] Fatal error compiling: java.lang.ExceptionInInitializerError: 
        com.sun.tools.javac.code.TypeTag :: UNKNOWN
```

**Warnings adicionales**:
```
WARNING: sun.misc.Unsafe::staticFieldBase has been called
WARNING: sun.misc.Unsafe::staticFieldBase will be removed in a future release
```

## 🔍 Diagnóstico del Problema

Este error ocurre por una **incompatibilidad entre versiones**:

### Causas Posibles:

1. **Maven 3.9.11** (instalado vía Homebrew) puede tener problemas de compatibilidad con:
   - Java 17
   - maven-compiler-plugin 3.11.0
   - Lombok en annotation processors

2. **Guice 5.1.0** (usado por Maven) tiene advertencias con Java moderno

3. **Configuración del compilador** puede estar en conflicto con Spring Boot Parent

## ✅ Soluciones Aplicadas

### Solución 1: Simplificar Configuración del Compilador

**pom.xml actualizado**:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <!-- Eliminado source/target específico -->
        <!-- Eliminada versión explícita -->
        <!-- Spring Boot Parent maneja la versión -->
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

### Solución 2: Opciones Alternativas

Si la Solución 1 no funciona, prueba estas alternativas:

#### Opción A: Usar versión específica del compilador

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.10.1</version> <!-- Versión más estable -->
    <configuration>
        <release>17</release>
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

#### Opción B: Actualizar Maven

```bash
# Si Maven está causando problemas, actualizar o cambiar versión
brew update
brew upgrade maven

# O usar wrapper de Maven incluido en el proyecto
./mvnw clean compile
```

#### Opción C: Verificar y configurar JAVA_HOME

```bash
# Ver versiones de Java disponibles
/usr/libexec/java_home -V

# Configurar Java 17 explícitamente
export JAVA_HOME=$(/usr/libexec/java_home -v 17)

# Verificar
java -version
mvn -version

# Compilar
mvn clean compile
```

#### Opción D: Deshabilitar warnings de Unsafe

Agregar al inicio del `pom.xml`:

```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <spring.boot.version>3.1.5</spring.boot.version>
    
    <!-- Suprimir warnings de Unsafe -->
    <maven.compiler.useIncrementalCompilation>false</maven.compiler.useIncrementalCompilation>
</properties>
```

## 🚀 Comandos para Probar

### 1. Limpiar completamente el proyecto

```bash
cd /Users/mateocisneros/IdeaProjects/Servicio_Calendario

# Eliminar target y cache
rm -rf target
rm -rf ~/.m2/repository/org/example/Servicio_Calendario

# Compilar desde cero
mvn clean compile
```

### 2. Compilar con más información de debug

```bash
# Ver errores detallados
mvn clean compile -e

# Ver debug completo
mvn clean compile -X

# Compilar sin tests
mvn clean compile -DskipTests
```

### 3. Usar Maven Wrapper (si existe)

```bash
# Si hay mvnw en el proyecto
./mvnw clean compile

# O descargarlo
mvn -N io.takari:maven:wrapper
./mvnw clean compile
```

## 🔄 Solución Alternativa: Compilar con IntelliJ IDEA

Si Maven en línea de comandos falla, IntelliJ IDEA puede compilar el proyecto:

1. **Abrir el proyecto en IntelliJ IDEA**
2. **File → Project Structure**
3. **Project SDK**: Seleccionar Java 17
4. **Project language level**: 17
5. **Build → Build Project** (⌘ + F9)

IntelliJ usa su propio compilador que puede funcionar mejor.

## 📊 Verificación del Entorno

### Comandos de Diagnóstico

```bash
# 1. Verificar Java
java -version
javac -version
echo $JAVA_HOME

# 2. Verificar Maven
mvn -version

# 3. Verificar dependencias de Maven
mvn dependency:tree | head -50

# 4. Verificar plugins de Maven
mvn help:effective-pom | grep -A 5 "maven-compiler-plugin"

# 5. Limpiar cache de Maven
rm -rf ~/.m2/repository/org/apache/maven/plugins/maven-compiler-plugin
```

### Configuración Esperada

**Java**:
- Versión: 17.x
- Vendor: Oracle, OpenJDK, Amazon Corretto, etc.

**Maven**:
- Versión: 3.8.x - 3.9.x
- Java Runtime: Java 17

**JAVA_HOME**:
- Debe apuntar a Java 17
- Ej: `/Library/Java/JavaVirtualMachines/jdk-17.jdk/Contents/Home`

## 🎯 Solución Recomendada (ORDEN DE PRIORIDAD)

### 1️⃣ PRIMERO: Verificar entorno

```bash
# Verificar Java y Maven
java -version
mvn -version

# Asegurarse que ambos usan Java 17
```

### 2️⃣ SEGUNDO: Limpiar y recompilar

```bash
cd /Users/mateocisneros/IdeaProjects/Servicio_Calendario
rm -rf target
mvn clean compile
```

### 3️⃣ TERCERO: Si falla, usar IntelliJ IDEA

```
1. Abrir proyecto en IntelliJ
2. Build → Rebuild Project
3. Si compila, usar IntelliJ para desarrollo
```

### 4️⃣ CUARTO: Actualizar Maven

```bash
brew update
brew upgrade maven
mvn --version

# Reintentar compilación
mvn clean compile
```

### 5️⃣ QUINTO: Usar configuración alternativa del compilador

Aplicar Opción A del documento (versión 3.10.1 del plugin)

## 📝 Archivo Actualizado

**pom.xml** - Plugin del compilador simplificado:

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <configuration>
                <excludes>
                    <exclude>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                    </exclude>
                </excludes>
            </configuration>
        </plugin>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <configuration>
                <annotationProcessorPaths>
                    <path>
                        <groupId>org.projectlombok</groupId>
                        <artifactId>lombok</artifactId>
                        <version>1.18.30</version>
                    </path>
                </annotationProcessorPaths>
            </configuration>
        </plugin>
    </plugins>
</build>
```

## 🎉 Resultado Esperado

Después de aplicar la solución:

```bash
[INFO] Compiling 34 source files with javac [debug release 17] to target/classes
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
```

## 🆘 Si Nada Funciona

### Plan B: Compilar con Gradle

Puedes convertir el proyecto a Gradle, que suele manejar mejor estas incompatibilidades:

```bash
# Generar build.gradle
gradle init --type java-application

# Copiar configuración de dependencias
# Compilar con Gradle
./gradlew build
```

### Plan C: Usar Docker

Compilar en un contenedor Docker con ambiente controlado:

```dockerfile
FROM maven:3.8-openjdk-17
WORKDIR /app
COPY . .
RUN mvn clean compile
```

---

**Fecha**: 26 de Noviembre, 2025  
**Error**: `TypeTag::UNKNOWN` - Incompatibilidad Maven/Java  
**Solución**: Simplificación de configuración del compilador  
**Estado**: 🔄 **EN PRUEBA**

