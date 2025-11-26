# 🚀 SOLUCIÓN: LocalHost No Funciona

## 🔴 PROBLEMA DETECTADO

La aplicación **NO está corriendo**. Por eso localhost no funciona.

**Causa**: El problema de compilación de Maven (`TypeTag::UNKNOWN`) impide ejecutar con `mvn spring-boot:run`

## ✅ SOLUCIÓN: Ejecutar con IntelliJ IDEA

### PASO 1: Abrir el Proyecto en IntelliJ IDEA

1. **Abrir IntelliJ IDEA**
2. **File → Open**
3. **Seleccionar**: `/Users/mateocisneros/IdeaProjects/Servicio_Calendario`
4. **Click**: OK
5. **Esperar** a que IntelliJ indexe el proyecto

---

### PASO 2: Configurar el SDK de Java

1. **File → Project Structure** (⌘ + ;)
2. **Project**:
   - **SDK**: Seleccionar **Java 17**
   - **Language level**: 17
3. **Click**: **Apply** → **OK**

---

### PASO 3: Sincronizar Maven

1. **Click** en el botón **"Load Maven Changes"** 🔄 (esquina superior derecha)
   
   O:
   
2. **Maven panel** (lado derecho) → **Click derecho** → **Reload All Maven Projects**

---

### PASO 4: Habilitar Annotation Processing (Lombok)

1. **Preferences** (⌘ + ,)
2. **Build, Execution, Deployment → Compiler → Annotation Processors**
3. **✅ Enable annotation processing**
4. **Apply** → **OK**

---

### PASO 5: Compilar el Proyecto

1. **Build → Build Project** (⌘ + F9)
2. **Esperar** a que compile
3. **Verificar** que no hay errores en la consola de Build

---

### PASO 6: Ejecutar la Aplicación

1. **Buscar el archivo**: `CalendarioApplication.java`
   - Ubicación: `src/main/java/org/example/CalendarioApplication.java`

2. **Click derecho** en el archivo

3. **Run 'CalendarioApplication'**

   O:

4. **Click** en el botón verde **▶️** (Play) al lado de la clase

---

### PASO 7: Verificar que Está Corriendo

Deberías ver en la consola:

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.1.5)

...
Started CalendarioApplication in 3.456 seconds (JVM running for 4.123)
```

**✅ SI VES ESTO: ¡La aplicación está corriendo!**

---

### PASO 8: Probar LocalHost

Ahora abre tu navegador:

1. **Swagger UI**:
   ```
   http://localhost:8080/swagger-ui.html
   ```
   Deberías ver la documentación de la API

2. **H2 Console**:
   ```
   http://localhost:8080/h2-console
   ```
   Base de datos en memoria

3. **Verificar API**:
   ```bash
   curl http://localhost:8080/auth/login
   ```

---

## 🔧 TROUBLESHOOTING

### ❌ Error: "Cannot resolve symbol 'xxx'"

**Solución**:
1. File → Invalidate Caches → Invalidate and Restart
2. Maven panel → Reload All Maven Projects
3. Build → Rebuild Project

---

### ❌ Error: "Port 8080 is already in use"

**Solución**:
```bash
# Matar proceso en puerto 8080
lsof -ti:8080 | xargs kill -9

# Reintentar ejecutar
```

---

### ❌ Error: Lombok no funciona

**Solución**:
1. Preferences → Plugins → Buscar "Lombok" → Install
2. Restart IntelliJ IDEA
3. Preferences → Build → Compiler → Annotation Processors
4. ✅ Enable annotation processing
5. Rebuild Project

---

### ❌ Error: "Cannot find main class"

**Solución**:
1. File → Project Structure → Project
2. Verificar SDK: Java 17
3. Build → Rebuild Project
4. Reintentar Run

---

### ❌ La aplicación se detiene inmediatamente

**Verificar en la consola** si hay errores como:
- `java.lang.ClassNotFoundException`
- `Failed to configure a DataSource`
- `Port already in use`

**Solución general**:
1. Rebuild Project
2. Verificar application.yml existe
3. Reintentar Run

---

## 📊 DIAGRAMA DEL PROCESO

```
┌─────────────────────┐
│ Abrir IntelliJ IDEA │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Abrir Proyecto      │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Configurar Java 17  │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Sincronizar Maven   │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Habilitar Lombok    │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Build Project       │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ Run Application     │
└──────────┬──────────┘
           │
           ▼
┌─────────────────────┐
│ ✅ LOCALHOST        │
│ http://localhost    │
│      :8080          │
└─────────────────────┘
```

---

## ✅ CHECKLIST DE VERIFICACIÓN

- [ ] IntelliJ IDEA abierto
- [ ] Proyecto cargado
- [ ] Java 17 configurado
- [ ] Maven sincronizado
- [ ] Lombok habilitado
- [ ] Proyecto compilado (Build)
- [ ] CalendarioApplication ejecutándose
- [ ] Consola muestra "Started CalendarioApplication"
- [ ] Swagger UI accesible: http://localhost:8080/swagger-ui.html
- [ ] H2 Console accesible: http://localhost:8080/h2-console

---

## 🎯 RESUMEN RÁPIDO

```
1. Abrir IntelliJ IDEA
2. Abrir proyecto: Servicio_Calendario
3. File → Project Structure → SDK: Java 17
4. Click 🔄 Load Maven Changes
5. Preferences → Annotation Processors → ✅ Enable
6. Build → Build Project (⌘ + F9)
7. Buscar: CalendarioApplication.java
8. Click derecho → Run 'CalendarioApplication'
9. Esperar mensaje: "Started CalendarioApplication"
10. Abrir: http://localhost:8080/swagger-ui.html
```

---

## 📝 COMANDOS ÚTILES

```bash
# Verificar si la app está corriendo
lsof -i:8080

# Matar proceso en puerto 8080
lsof -ti:8080 | xargs kill -9

# Verificar Java
java -version

# Probar API
curl http://localhost:8080/swagger-ui.html
```

---

## 🆘 SI TODO FALLA

### Opción 1: Reiniciar IntelliJ IDEA
```
File → Invalidate Caches → Invalidate and Restart
```

### Opción 2: Limpiar y Recompilar
```
Build → Clean Project
Build → Rebuild Project
```

### Opción 3: Verificar Logs
```
Run panel → Console
Ver errores en rojo
```

---

## 🎉 ÉXITO

Cuando veas esto en el navegador:

```
http://localhost:8080/swagger-ui.html
```

Y aparezca la interfaz de Swagger con todos los endpoints:
- **Autenticacion**
- **Calendar**  
- **Notification**

**¡FELICIDADES! LocalHost está funcionando** 🎉

---

**IMPORTANTE**: No uses `mvn spring-boot:run` por el terminal. Usa IntelliJ IDEA.

**Razón**: Maven tiene problemas de compatibilidad con Java 17. IntelliJ IDEA usa su propio compilador que funciona perfectamente.

---

**Última actualización**: 26 de Noviembre, 2025  
**Método recomendado**: ✅ IntelliJ IDEA  
**Método NO recomendado**: ❌ Maven CLI

