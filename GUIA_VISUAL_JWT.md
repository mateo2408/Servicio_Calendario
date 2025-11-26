# 🔑 GUÍA VISUAL - Cómo Obtener y Usar Token JWT

## 📱 MÉTODO 1: Swagger UI (MÁS FÁCIL) ⭐

### Paso 1: Abrir Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### Paso 2: Registrar Usuario
```
┌─────────────────────────────────────────────┐
│ POST /auth/register                         │
├─────────────────────────────────────────────┤
│ [Try it out]                                │
│                                             │
│ Request body:                               │
│ {                                           │
│   "username": "testuser",                   │
│   "password": "password123",                │
│   "email": "test@example.com",              │
│   "nombre": "Usuario Test"                  │
│ }                                           │
│                                             │
│ [Execute]                                   │
└─────────────────────────────────────────────┘

Respuesta:
┌─────────────────────────────────────────────┐
│ {                                           │
│   "token": "eyJhbGciOiJIUzUxMiJ9.eyJz..."  │  ← COPIAR ESTO
│   "tipo": "Bearer",                         │
│   "usuarioId": 1,                           │
│   "username": "testuser",                   │
│   "email": "test@example.com"               │
│ }                                           │
└─────────────────────────────────────────────┘
```

### Paso 3: Autorizar en Swagger
```
┌─────────────────────────────────────────────┐
│  [🔓 Authorize]  ← Click aquí (arriba)     │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│ Available authorizations                    │
├─────────────────────────────────────────────┤
│ bearerAuth (http, Bearer)                   │
│                                             │
│ Value: [                                  ] │
│        ↑ Pegar token aquí                   │
│                                             │
│        [Authorize] [Close]                  │
└─────────────────────────────────────────────┘
```

### Paso 4: Usar Endpoints
```
Ahora TODOS los endpoints funcionarán automáticamente:
✅ POST /calendar
✅ GET /calendar
✅ POST /calendar/events
✅ POST /alerts
```

---

## 💻 MÉTODO 2: cURL (Línea de Comandos)

### Paso 1: Obtener Token
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

**Respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX",
  "tipo": "Bearer",
  "usuarioId": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

### Paso 2: Copiar Token
```
Token = eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX
```

### Paso 3: Usar en Peticiones
```bash
# Formato:
curl -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {PEGAR_TOKEN_AQUÍ}" \
  -d '{ ... }'

# Ejemplo real:
curl -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX" \
  -d '{
    "nombre": "Mi Calendario",
    "descripcion": "Calendario personal",
    "color": "#FF5733",
    "publico": false
  }'
```

---

## 📮 MÉTODO 3: Postman

### Paso 1: Crear Petición
```
POST http://localhost:8080/auth/register

Body → raw → JSON

{
  "username": "testuser",
  "password": "password123",
  "email": "test@example.com",
  "nombre": "Usuario Test"
}

[Send]
```

### Paso 2: Copiar Token de la Respuesta
```
Response:
{
  "token": "eyJhbGciOiJIUzUxMiJ9..." ← Copiar esto
}
```

### Paso 3: Configurar Authorization
```
Para cada petición:

Authorization → Type: Bearer Token
Token: [Pegar token aquí]

O globalmente en la Collection:
Collection → Edit → Authorization → Bearer Token
```

---

## 🚀 MÉTODO 4: Script Automático

```bash
# Ejecutar script de prueba
cd /Users/mateocisneros/IdeaProjects/Servicio_Calendario
./test_jwt.sh
```

El script hará automáticamente:
1. ✅ Registrar usuario
2. ✅ Obtener token
3. ✅ Crear calendario
4. ✅ Mostrar resultados

---

## 🔐 FORMATO DEL TOKEN JWT

### Anatomía del Token
```
eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0dXNlciIsImlhdCI6MTcwMDAwMDAwMCwiZXhwIjoxNzAwMDg2NDAwfQ.XXXXX
│                     │                                                                        │
│   Header (Base64)   │               Payload (Base64)                                        │  Signature
│                     │                                                                        │
└─────────────────────┴────────────────────────────────────────────────────────────────────────┴──────────
```

### Header
```json
{
  "alg": "HS512",
  "typ": "JWT"
}
```

### Payload
```json
{
  "sub": "testuser",      // Username
  "iat": 1700000000,      // Issued At (timestamp)
  "exp": 1700086400       // Expiration (24 horas después)
}
```

### Signature
```
HMACSHA512(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret_key
)
```

---

## 📊 DIAGRAMA DE FLUJO

```
┌──────────────┐
│   CLIENTE    │
└──────┬───────┘
       │
       │ 1. POST /auth/register o /auth/login
       │    { username, password }
       ▼
┌──────────────┐
│   SERVIDOR   │
└──────┬───────┘
       │
       │ 2. Valida credenciales
       │
       ▼
┌──────────────┐
│  GENERA JWT  │
│  HS512       │
│  Exp: 24h    │
└──────┬───────┘
       │
       │ 3. Retorna token
       │    { token: "eyJ..." }
       ▼
┌──────────────┐
│   CLIENTE    │
│ Guarda token │
└──────┬───────┘
       │
       │ 4. Peticiones con token
       │    Authorization: Bearer {token}
       ▼
┌──────────────┐
│   SERVIDOR   │
│ Valida JWT   │
└──────┬───────┘
       │
       ├─ Token válido → ✅ Permite acceso
       │
       └─ Token inválido → ❌ 401 Unauthorized
```

---

## ⚡ INICIO RÁPIDO (30 segundos)

### 1. Abre Swagger UI
```
http://localhost:8080/swagger-ui.html
```

### 2. Register
```
POST /auth/register → Try it out → Execute
```

### 3. Copia token de la respuesta

### 4. Authorize
```
Click [🔓 Authorize] → Pegar token → Authorize
```

### 5. ¡Listo! 
```
Ahora puedes usar todos los endpoints
```

---

## ✅ CHECKLIST

- [ ] Aplicación corriendo
- [ ] Swagger UI accesible
- [ ] Usuario registrado
- [ ] Token copiado
- [ ] Token autorizado en Swagger
- [ ] Endpoints funcionando

---

## 🎯 EJEMPLO COMPLETO

```bash
# 1. Obtener token
TOKEN=$(curl -s -X POST http://localhost:8080/auth/register \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"pass123","email":"user@test.com","nombre":"User"}' \
  | jq -r '.token')

# 2. Usar token
curl -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"nombre":"Mi Cal","descripcion":"Test","color":"#FF0000","publico":false}'
```

---

## 📝 ARCHIVO DE REQUESTS

Usa el archivo `api-requests.http` en IntelliJ IDEA:

1. Abrir: `api-requests.http`
2. Click en "▶️ Run" junto a cada request
3. Los tokens se guardan automáticamente

---

**¡Todo listo para usar la autenticación JWT!** 🎉

