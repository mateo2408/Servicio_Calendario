# 🔐 GUÍA COMPLETA - Autenticación JWT

## 📋 Requisitos Previos

1. **La aplicación debe estar corriendo**
2. **Puerto 8080 debe estar disponible**

## 🚀 PASO 1: Iniciar la Aplicación

### Opción A: Con IntelliJ IDEA (Recomendado)
```
1. Abrir IntelliJ IDEA
2. Buscar: CalendarioApplication.java
3. Click derecho → Run 'CalendarioApplication'
4. Esperar mensaje: "Started CalendarioApplication in X seconds"
```

### Opción B: Con Maven (si funciona)
```bash
cd 
mvn spring-boot:run
```

## 🔑 PASO 2: Obtener Token JWT

### 2.1 Registrar un Nuevo Usuario

**Endpoint**: `POST /auth/register`

**cURL**:
```bash
curl -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "nombre": "Usuario de Prueba"
  }'
```

**Respuesta Esperada**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX",
  "tipo": "Bearer",
  "usuarioId": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

### 2.2 O Iniciar Sesión con Usuario Existente

**Endpoint**: `POST /auth/login`

**cURL**:
```bash
curl -X POST http://localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

**Respuesta Esperada**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX",
  "tipo": "Bearer",
  "usuarioId": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

**⚠️ IMPORTANTE**: Copia el valor del campo `token` - Lo necesitarás para los siguientes pasos.

## 🔓 PASO 3: Usar el Token en Peticiones

### 3.1 Formato del Header de Autorización

```
Authorization: Bearer {TU_TOKEN_AQUI}
```

**Ejemplo Completo**:
```
Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX
```

### 3.2 Ejemplo: Crear un Calendario

**cURL con Token**:
```bash
# Reemplaza {TU_TOKEN} con el token real
curl -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TU_TOKEN}" \
  -d '{
    "nombre": "Mi Calendario Personal",
    "descripcion": "Calendario con eventos importantes",
    "color": "#FF5733",
    "publico": false
  }'
```

### 3.3 Ejemplo: Obtener Calendarios

```bash
curl -X GET http://localhost:8080/calendar \
  -H "Authorization: Bearer {TU_TOKEN}"
```

### 3.4 Ejemplo: Agregar Evento a Calendario

```bash
curl -X POST http://localhost:8080/calendar/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TU_TOKEN}" \
  -d '{
    "titulo": "Reunión Importante",
    "descripcion": "Reunión con el equipo",
    "fechaInicio": "2025-12-01T10:00:00",
    "fechaFin": "2025-12-01T11:30:00",
    "ubicacion": "Sala de Juntas",
    "todoElDia": false,
    "tipo": "EVENTO_LABORAL",
    "calendarioId": 1
  }'
```

### 3.5 Ejemplo: Configurar Alerta

```bash
curl -X POST http://localhost:8080/alerts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TU_TOKEN}" \
  -d '{
    "eventoId": 1,
    "fechaAlerta": "2025-12-01T09:45:00",
    "tipoNotificacion": "PUSH",
    "minutosAnticipacion": 15,
    "mensaje": "Recordatorio: Reunión en 15 minutos"
  }'
```

## 🌐 PASO 4: Usar Swagger UI (Más Fácil)

### 4.1 Acceder a Swagger UI

Abrir en navegador:
```
http://localhost:8080/swagger-ui.html
```

### 4.2 Obtener Token en Swagger

1. **Expandir**: `Autenticacion` → `POST /auth/register` (o `/auth/login`)
2. **Click**: "Try it out"
3. **Completar** el JSON con tus datos:
   ```json
   {
     "username": "testuser",
     "password": "password123",
     "email": "test@example.com",
     "nombre": "Usuario Test"
   }
   ```
4. **Click**: "Execute"
5. **Copiar** el `token` de la respuesta

### 4.3 Autorizar en Swagger

1. **Click** en el botón **"Authorize" 🔓** (arriba a la derecha)
2. **Pegar** el token en el campo "Value"
   ```
   eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX
   ```
3. **Click**: "Authorize"
4. **Click**: "Close"

### 4.4 Probar Endpoints Protegidos

Ahora puedes probar cualquier endpoint:
- POST /calendar
- GET /calendar
- POST /calendar/events
- POST /alerts
- etc.

Todos incluirán automáticamente el token en el header.

## 📱 PASO 5: Usar Postman

### 5.1 Importar Colección

1. **Abrir Postman**
2. **Import** → **Link**
3. **Pegar**: `http://localhost:8080/api-docs`
4. **Import**

### 5.2 Configurar Autorización

1. **Collection** → **Edit**
2. **Authorization** → **Type**: Bearer Token
3. **Token**: Pegar tu token JWT
4. **Save**

### 5.3 Usar Variables de Entorno

Crear variable para el token:

```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX"
}
```

Luego en Authorization:
```
{{token}}
```

## ⏱️ Expiración del Token

- **Duración**: 24 horas
- **Después de 24 horas**: Debes hacer login nuevamente
- **Error si expira**: `401 Unauthorized`

## 🔄 Renovar Token

Cuando el token expire, simplemente:

1. **Login nuevamente**:
   ```bash
   curl -X POST http://localhost:8080/auth/login \
     -H "Content-Type: application/json" \
     -d '{"username": "testuser", "password": "password123"}'
   ```

2. **Copiar el nuevo token**

3. **Actualizar** en tus peticiones

## 🛠️ Troubleshooting

### Error: "401 Unauthorized"
**Causa**: Token inválido o expirado
**Solución**: 
1. Verificar que copiaste el token completo
2. Verificar formato: `Authorization: Bearer {token}`
3. Obtener nuevo token con `/auth/login`

### Error: "Token malformed"
**Causa**: Token incompleto o con espacios
**Solución**:
1. Copiar token completo (comienza con `eyJ`)
2. No incluir espacios antes/después
3. No incluir "Bearer" en el valor del token (solo en el header completo)

### Error: "Cannot connect to server"
**Causa**: Aplicación no está corriendo
**Solución**:
1. Iniciar aplicación en IntelliJ IDEA
2. Verificar puerto 8080 está libre
3. Esperar mensaje "Started CalendarioApplication"

## 📝 Script Rápido de Prueba

Guarda esto en `test_auth.sh`:

```bash
#!/bin/bash

# Colores
GREEN='\033[0;32m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

echo -e "${BLUE}=== PRUEBA DE AUTENTICACIÓN JWT ===${NC}"

# 1. Registrar usuario
echo -e "\n${GREEN}1. Registrando usuario...${NC}"
RESPONSE=$(curl -s -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123",
    "email": "test@example.com",
    "nombre": "Usuario Test"
  }')

echo "$RESPONSE" | jq .

# 2. Extraer token
TOKEN=$(echo "$RESPONSE" | jq -r '.token')

echo -e "\n${GREEN}2. Token obtenido:${NC}"
echo "$TOKEN"

# 3. Crear calendario con token
echo -e "\n${GREEN}3. Creando calendario con token...${NC}"
curl -s -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{
    "nombre": "Mi Calendario",
    "descripcion": "Calendario de prueba",
    "color": "#FF5733",
    "publico": false
  }' | jq .

echo -e "\n${BLUE}=== PRUEBA COMPLETADA ===${NC}"
```

Ejecutar:
```bash
chmod +x test_auth.sh
./test_auth.sh
```

## 📚 Ejemplos Completos

### Flujo Completo: Registro → Calendario → Evento → Alerta

```bash
# 1. Registro
RESPONSE=$(curl -s -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123","email":"user1@test.com","nombre":"User One"}')

TOKEN=$(echo "$RESPONSE" | jq -r '.token')

# 2. Crear Calendario
CAL_RESPONSE=$(curl -s -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nombre":"Cal 1","descripcion":"Test","color":"#FF0000","publico":false}')

CAL_ID=$(echo "$CAL_RESPONSE" | jq -r '.id')

# 3. Crear Evento
EVENT_RESPONSE=$(curl -s -X POST http://localhost:8080/calendar/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"titulo\":\"Evento 1\",\"descripcion\":\"Test\",\"fechaInicio\":\"2025-12-01T10:00:00\",\"fechaFin\":\"2025-12-01T11:00:00\",\"ubicacion\":\"Office\",\"todoElDia\":false,\"tipo\":\"EVENTO_LABORAL\",\"calendarioId\":$CAL_ID}")

EVENT_ID=$(echo "$EVENT_RESPONSE" | jq -r '.id')

# 4. Crear Alerta
curl -s -X POST http://localhost:8080/alerts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d "{\"eventoId\":$EVENT_ID,\"fechaAlerta\":\"2025-12-01T09:45:00\",\"tipoNotificacion\":\"PUSH\",\"minutosAnticipacion\":15,\"mensaje\":\"Recordatorio\"}" | jq .
```

## ✅ Checklist de Verificación

- [ ] Aplicación corriendo en puerto 8080
- [ ] Swagger UI accesible: http://localhost:8080/swagger-ui.html
- [ ] Usuario registrado con `/auth/register`
- [ ] Token obtenido (comienza con `eyJ`)
- [ ] Token copiado completamente
- [ ] Header de autorización correcto: `Authorization: Bearer {token}`
- [ ] Endpoints protegidos respondiendo correctamente

---

**¡Listo para usar la API con autenticación JWT!** 🎉

**Recuerda**: El token dura 24 horas. Después debes hacer login nuevamente.

