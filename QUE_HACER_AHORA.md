# 🎉 ¡LOCALHOST FUNCIONANDO! - ¿QUÉ HACER AHORA?

## ✅ CONFIRMACIÓN

Si puedes acceder a:
- ✅ http://localhost:8080/swagger-ui.html
- ✅ http://localhost:8080/h2-console

**¡Felicidades! El sistema está funcionando correctamente** 🎉

---

## 🎯 OPCIONES PARA PROBAR EL SISTEMA

### OPCIÓN 1: Script Automático (MÁS RÁPIDO) ⚡

Este script creará automáticamente:
- 1 usuario
- 1 calendario
- 3 eventos
- 2 alertas

```bash
cd 
./prueba_sistema_completo.sh
```

**Duración**: 10 segundos

**Qué hace**:
1. Registra usuario "juan.perez"
2. Obtiene token JWT
3. Crea calendario personal
4. Agrega 3 eventos (Reunión, Tarea, Personal)
5. Configura 2 alertas (PUSH, EMAIL)
6. Muestra resumen completo

---

### OPCIÓN 2: Swagger UI (VISUAL E INTERACTIVO) 🌐

1. **Abrir Swagger UI**:
   ```
   http://localhost:8080/swagger-ui.html
   ```

2. **Registrar Usuario**:
   - Expandir: `Autenticacion` → `POST /auth/register`
   - Click: "Try it out"
   - Completar:
     ```json
     {
       "username": "miusuario",
       "password": "MiPassword123",
       "email": "usuario@email.com",
       "nombre": "Mi Nombre"
     }
     ```
   - Click: "Execute"
   - **Copiar el token** de la respuesta

3. **Autorizar en Swagger**:
   - Click: botón **"Authorize" 🔓** (arriba)
   - Pegar el token
   - Click: "Authorize" → "Close"

4. **Crear Calendario**:
   - Expandir: `Calendar` → `POST /calendar`
   - Click: "Try it out"
   - Completar:
     ```json
     {
       "nombre": "Mi Calendario",
       "descripcion": "Datos sensibles aquí",
       "color": "#3498db",
       "publico": false
     }
     ```
   - Click: "Execute"

5. **Agregar Evento**:
   - Expandir: `POST /calendar/events`
   - Click: "Try it out"
   - Completar con el calendarioId que obtuviste

6. **Configurar Alerta**:
   - Expandir: `POST /alerts`
   - Click: "Try it out"
   - Completar con el eventoId

---

### OPCIÓN 3: Archivo HTTP Requests (IntelliJ IDEA) 📝

1. **Abrir archivo**: `api-requests.http`

2. **Ejecutar requests**:
   - Click en **▶️ Run** junto a cada request
   - Los tokens se guardan automáticamente
   - Los IDs se propagan entre requests

3. **Orden recomendado**:
   1. Registro
   2. Login (si es necesario)
   3. Crear calendario
   4. Agregar eventos
   5. Configurar alertas

---

### OPCIÓN 4: cURL Manual (Terminal) 💻

#### 1. Registrar Usuario
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "usuario1",
    "password": "Pass123",
    "email": "user1@test.com",
    "nombre": "Usuario Uno"
  }'
```

#### 2. Copiar Token de la Respuesta

#### 3. Crear Calendario (reemplaza {TOKEN})
```bash
curl -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN}" \
  -d '{
    "nombre": "Mi Calendario",
    "descripcion": "Test",
    "color": "#FF5733",
    "publico": false
  }'
```

---

## 🔍 VERIFICAR QUE TODO FUNCIONA

### 1. Ver Base de Datos H2

1. **Abrir**: http://localhost:8080/h2-console

2. **Conectar**:
   - JDBC URL: `jdbc:h2:mem:calendardb`
   - Username: `sa`
   - Password: (dejar vacío)

3. **Ejecutar consulta**:
   ```sql
   SELECT * FROM usuarios;
   SELECT * FROM calendarios;
   SELECT * FROM eventos;
   SELECT * FROM alertas;
   ```

Deberías ver los datos cifrados en la BD.

---

### 2. Verificar Cifrado

**En H2 Console**, ejecuta:
```sql
SELECT nombre, datos_cifrados FROM calendarios;
```

Verás que `datos_cifrados` contiene texto encriptado.

**En Swagger**, usa:
```
GET /calendar/view
```

Verás los datos descifrados.

---

### 3. Probar Diferentes Usuarios

Puedes crear múltiples usuarios:
- Usuario 1: "juan.perez"
- Usuario 2: "maria.garcia"
- Usuario 3: "carlos.lopez"

Cada uno tendrá:
- Su propio token
- Sus propios calendarios
- Sus propios eventos
- Sus propias alertas

---

## 📊 ARQUITECTURA EN ACCIÓN

### Flujo Completo de Uso:

```
1. Usuario se registra
   ↓
2. Sistema genera token JWT (24h)
   ↓
3. Usuario crea calendario
   ↓
4. Sistema CIFRA datos sensibles
   ↓
5. Usuario agrega eventos
   ↓
6. Sistema CIFRA descripciones
   ↓
7. Usuario configura alertas
   ↓
8. Scheduler procesa alertas (cada 60s)
   ↓
9. Sistema envía notificaciones
   ↓
10. Usuario ve calendario
    ↓
11. Sistema DESCIFRA datos para mostrar
```

---

## 🎯 CARACTERÍSTICAS A PROBAR

### ✅ Autenticación JWT
- [x] Registro de usuarios
- [x] Login
- [x] Token válido por 24 horas
- [x] Endpoints protegidos

### ✅ Cifrado de Datos
- [x] Datos sensibles cifrados en BD
- [x] Descifrado automático al mostrar
- [x] Solo usuarios autorizados pueden descifrar

### ✅ Control de Acceso
- [x] Usuario solo ve sus calendarios
- [x] Usuario solo puede modificar sus eventos
- [x] Calendarios públicos visibles para todos

### ✅ Sistema de Notificaciones
- [x] Alertas PUSH, EMAIL, SMS
- [x] Scheduler procesa cada 60 segundos
- [x] Notificaciones con anticipación configurable

### ✅ Multiplataforma
- [x] API REST accesible desde cualquier cliente
- [x] Web (Swagger UI)
- [x] Mobile (API)
- [x] Servicios externos (API)

---

## 📚 DOCUMENTACIÓN DISPONIBLE

1. **README.md** - Documentación general
2. **GUIA_AUTENTICACION_JWT.md** - Guía de autenticación
3. **GUIA_VISUAL_JWT.md** - Guía visual paso a paso
4. **api-requests.http** - Ejemplos de requests HTTP
5. **openapi-spec.yaml** - Especificación OpenAPI
6. **RESTRICCIONES_ARQUITECTURA.md** - Restricciones del sistema

---

## 🆘 AYUDA

### Ver logs de la aplicación
En IntelliJ IDEA, ver el panel "Run" para ver:
- Peticiones HTTP
- Queries SQL
- Cifrado/descifrado
- Procesamiento de alertas

### Reiniciar BD
La BD H2 está en memoria (`jdbc:h2:mem:calendardb`).
Para limpiar datos: **Restart la aplicación**

### Probar diferentes escenarios
1. Usuario con múltiples calendarios
2. Calendario público vs privado
3. Eventos de diferentes tipos
4. Alertas con diferentes anticipaciones
5. Notificaciones de diferentes tipos

---

## 🎉 PRÓXIMOS PASOS RECOMENDADOS

### 1. Ejecuta el Script de Prueba
```bash
./prueba_sistema_completo.sh
```

### 2. Explora Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 3. Revisa la Base de Datos
```
http://localhost:8080/h2-console
```

### 4. Consulta SwaggerHub
```
https://app.swaggerhub.com/apis/udla-52c/api-sistema-de-calendario/1.0.0
```

---

## ✅ CHECKLIST FINAL

- [ ] Script de prueba ejecutado
- [ ] Usuario registrado en Swagger
- [ ] Token JWT obtenido
- [ ] Calendario creado
- [ ] Evento agregado
- [ ] Alerta configurada
- [ ] Datos vistos en H2 Console
- [ ] Cifrado verificado
- [ ] Vista descifrada obtenida

---

**¡EL SISTEMA ESTÁ 100% FUNCIONAL Y LISTO PARA USAR!** 🎉

**Ejecuta**: `./prueba_sistema_completo.sh` y verás todo en acción.

