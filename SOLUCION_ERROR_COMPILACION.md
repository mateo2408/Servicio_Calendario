# 🔧 SOLUCIÓN AL ERROR DE COMPILACIÓN

## ❌ Error Original

```
[ERROR] Failed to execute goal org.apache.maven.plugins:maven-compiler-plugin:3.11.0:compile
[ERROR] invalid source release 17 with --enable-preview
[ERROR] (preview language features are only supported for release 25)
```

## 🔍 Análisis del Problema

El error se debía a una **configuración incorrecta en el `pom.xml`**:

1. **Properties configuradas con Java 25**:
   ```xml
   <maven.compiler.source>25</maven.compiler.source>
   <maven.compiler.target>25</maven.compiler.target>
   ```

2. **Plugin del compilador configurado con Java 25 y preview**:
   ```xml
   <plugin>
       <groupId>org.apache.maven.plugins</groupId>
       <artifactId>maven-compiler-plugin</artifactId>
       <configuration>
           <source>25</source>
           <target>25</target>
           <compilerArgs>--enable-preview</compilerArgs>
       </configuration>
   </plugin>
   ```

3. **Conflicto**: El proyecto usa Java 17, pero el compilador intentaba usar características de Java 25 con preview habilitado.

## ✅ Solución Aplicada

### 1. Corrección de Properties en pom.xml

**ANTES**:
```xml
<properties>
    <maven.compiler.source>25</maven.compiler.source>
    <maven.compiler.target>25</maven.compiler.target>
    ...
</properties>
```

**DESPUÉS**:
```xml
<properties>
    <maven.compiler.source>17</maven.compiler.source>
    <maven.compiler.target>17</maven.compiler.target>
    ...
</properties>
```

### 2. Eliminación del Plugin maven-compiler-plugin

**ANTES**:
```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>25</source>
        <target>25</target>
        <compilerArgs>--enable-preview</compilerArgs>
    </configuration>
</plugin>
```

**DESPUÉS**:
```xml
<!-- Plugin eliminado - Spring Boot parent ya configura Java 17 -->
```

**Razón**: Spring Boot Starter Parent (3.1.5) ya configura correctamente el compilador para Java 17. No es necesario sobrescribir esa configuración.

### 3. Recreación de Archivos Java Corruptos

Algunos archivos se habían corrompido durante ediciones anteriores. Se recrearon:

- ✅ `CalendarioApplication.java`
- ✅ `Usuario.java`
- ✅ `AuthService.java`
- ✅ `JwtUtil.java`
- ✅ `AuthController.java`

## 📋 Pasos Realizados

### Paso 1: Identificar el problema
```bash
cd 
mvn clean compile
# ERROR: invalid source release 17 with --enable-preview
```

### Paso 2: Revisar pom.xml
```bash
cat pom.xml | grep -A 3 "properties"
# Encontrado: Java 25 configurado
```

### Paso 3: Corregir properties
```xml
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
```

### Paso 4: Eliminar plugin conflictivo
```xml
<!-- Eliminado maven-compiler-plugin con --enable-preview -->
```

### Paso 5: Recrear archivos corruptos
```bash
rm -f archivos_corruptos.java
# Crear nuevos archivos limpios
```

### Paso 6: Compilar
```bash
mvn clean compile
# ✅ EXITOSO
```

## 🎯 Resultado Final

### Estado Actual del pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>org.example</groupId>
    <artifactId>Servicio_Calendario</artifactId>
    <version>1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.source>17</maven.compiler.source>  ✅ CORREGIDO
        <maven.compiler.target>17</maven.compiler.target>  ✅ CORREGIDO
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <spring.boot.version>3.1.5</spring.boot.version>
    </properties>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>3.1.5</version>
    </parent>

    <dependencies>
        <!-- Spring Boot dependencies -->
        ...
    </dependencies>

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
            <!-- ✅ maven-compiler-plugin ELIMINADO -->
        </plugins>
    </build>
</project>
```

### Archivos Corregidos

| Archivo | Estado | Descripción |
|---------|--------|-------------|
| `pom.xml` | ✅ | Java 17 configurado, preview eliminado |
| `CalendarioApplication.java` | ✅ | Recreado limpiamente |
| `Usuario.java` | ✅ | Recreado limpiamente |
| `AuthService.java` | ✅ | Recreado limpiamente |
| `JwtUtil.java` | ✅ | Recreado limpiamente |
| `AuthController.java` | ✅ | Recreado limpiamente |

## 🚀 Cómo Compilar y Ejecutar

### Compilar
```bash
cd 

# Limpiar y compilar
mvn clean compile

# Debería mostrar: BUILD SUCCESS
```

### Empaquetar
```bash
# Crear JAR
mvn clean package -DskipTests

# JAR generado en: target/Servicio_Calendario-1.0-SNAPSHOT.jar
```

### Ejecutar
```bash
# Opción 1: Con Maven
mvn spring-boot:run

# Opción 2: Con Java
java -jar target/Servicio_Calendario-1.0-SNAPSHOT.jar
```

### Verificar
```bash
# Una vez ejecutado, acceder a:
# - Swagger UI: http://localhost:8080/swagger-ui.html
# - H2 Console: http://localhost:8080/h2-console
# - API Docs: http://localhost:8080/api-docs
```

## 📝 Lecciones Aprendidas

### 1. Versión de Java
- ✅ Usar la versión de Java instalada (17 en este caso)
- ❌ No usar versiones futuras no disponibles (25)

### 2. Preview Features
- ✅ Solo usar `--enable-preview` en desarrollo experimental
- ❌ No usar preview features en proyectos de producción

### 3. Spring Boot Parent
- ✅ Confiar en la configuración del parent POM
- ❌ No sobrescribir configuraciones innecesariamente

### 4. Maven Compiler Plugin
- ✅ Dejar que Spring Boot lo configure
- ❌ No agregar configuración personalizada a menos que sea necesario

## 🔍 Verificación Final

### Checklist de Verificación

- ✅ Java 17 configurado en properties
- ✅ Preview features eliminadas
- ✅ maven-compiler-plugin eliminado
- ✅ Archivos Java recreados sin errores
- ✅ pom.xml válido
- ✅ Compilación exitosa

### Comando de Verificación

```bash
# Verificar versión de Java
java -version

# Verificar configuración Maven
mvn -version

# Verificar que compila
mvn clean compile

# Si todo está bien, deberías ver:
# [INFO] BUILD SUCCESS
```

## 🎉 Conclusión

El error **"invalid source release 17 with --enable-preview"** fue resuelto exitosamente mediante:

1. ✅ Corrección de la versión de Java en properties (25 → 17)
2. ✅ Eliminación de la configuración del maven-compiler-plugin
3. ✅ Recreación de archivos Java corruptos

**El proyecto ahora compila correctamente con Java 17 y está listo para ejecutar.**

---

**Fecha de corrección**: 26 de Noviembre, 2025
**Estado**: ✅ **RESUELTO**

