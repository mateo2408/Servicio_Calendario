# SISTEMA DE CALENDARIO - PROYECTO COMPLETADO

## ✅ ESTADO DEL PROYECTO

El proyecto ha sido implementado completamente con la arquitectura solicitada. Todos los archivos están creados y listos para usar.

## 📁 ARCHIVOS CREADOS (Total: 40+ archivos)

### 1. Configuración
- `pom.xml` - Dependencias Maven (Spring Boot, Security, JWT, JPA, RabbitMQ, Swagger)
- `application.yml` - Configuración de la aplicación

### 2. Modelos de Datos (Entidades JPA)
- `Usuario.java` - Entidad para BaseDatosUsuarios
- `Calendario.java` - Entidad para BD_Calendarios (cifrado)
- `EventoCalendario.java` - Eventos de calendario
- `Alerta.java` - Alertas de notificaciones

### 3. Repositorios (Acceso a Datos)
- `UsuarioRepository.java`
- `CalendarioRepository.java`
- `EventoRepository.java`
- `AlertaRepository.java`

### 4. DTOs (Transferencia de Datos)
- `LoginRequest.java` / `AuthResponse.java`
- `RegistroRequest.java`
- `CalendarioRequest.java` / `CalendarioResponse.java`
- `EventoRequest.java` / `EventoResponse.java`
- `AlertaRequest.java` / `AlertaResponse.java`

### 5. Servicios (Lógica de Negocio)
- `AuthService.java` - Autenticación y JWT
- `CalendarService.java` - Gestión de calendarios
- `NotificationService.java` - Sistema de alertas
- `EncryptionService.java` - Cifrado AES
- `MessageBrokerService.java` - RabbitMQ

### 6. Seguridad
- `SecurityConfig.java` - Configuración Spring Security
- `JwtUtil.java` - Utilidades JWT
- `JwtAuthenticationFilter.java` - Filtro de autenticación
- `CustomUserDetailsService.java` - Carga de usuarios

### 7. Controladores (API Gateway)
- `AuthController.java` - Endpoints de autenticación
- `CalendarController.java` - Endpoints de calendario
- `NotificationController.java` - Endpoints de alertas

### 8. Configuración Adicional
- `OpenAPIConfig.java` - Configuración Swagger
- `AlertScheduler.java` - Scheduler de alertas
- `AppConfig.java` - Config general

### 9. Documentación
- `README.md` - Guía completa del proyecto
- `RESTRICCIONES_ARQUITECTURA.md` - Documento de restricciones
- `openapi-spec.yaml` - Especificación OpenAPI/Swagger
- `diagrama.xml` - Diagrama de arquitectura XML

### 10. Aplicación Principal
- `CalendarioApplication.java` - Main class
- `Main.java` - (original, puedes eliminar)

## 🚀 CÓMO EJECUTAR EL PROYECTO

### Opción 1: Sin RabbitMQ (Más simple)

```bash
cd /Users/mateocisneros/IdeaProjects/Servicio_Calendario

# Compilar
mvn clean install

# Ejecutar
mvn spring-boot:run
```

La aplicación funcionará perfectamente sin RabbitMQ, usando simulación de mensajes.

### Opción 2: Con RabbitMQ (Completo)

```bash
# 1. Instalar RabbitMQ
brew install rabbitmq

# 2. Iniciar RabbitMQ
brew services start rabbitmq

# 3. Compilar y ejecutar
cd /Users/mateocisneros/IdeaProjects/Servicio_Calendario
mvn clean install
mvn spring-boot:run
```

## 📊 ACCESO A LA APLICACIÓN

Una vez ejecutado, accede a:

1. **Swagger UI (Documentación interactiva)**:
   - http://localhost:8080/swagger-ui.html

2. **H2 Console (Base de datos)**:
   - http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:calendardb`
   - Username: `sa`
   - Password: (dejar vacío)

3. **RabbitMQ Management (si está instalado)**:
   - http://localhost:15672
   - Username: `guest`
   - Password: `guest`

## 🧪 PROBAR LA API

### 1. Registrar un Usuario

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

**Respuesta**:
```json
{
  "token": "eyJhbGciOiJIUzUxMiJ9...",
  "tipo": "Bearer",
  "usuarioId": 1,
  "username": "testuser",
  "email": "test@example.com"
}
```

### 2. Crear un Calendario

```bash
curl -X POST http://localhost:8080/calendar \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN_AQUI}" \
  -d '{
    "nombre": "Mi Calendario",
    "descripcion": "Información sensible que se cifrará",
    "color": "#FF5733",
    "publico": false
  }'
```

### 3. Agregar un Evento

```bash
curl -X POST http://localhost:8080/calendar/events \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN_AQUI}" \
  -d '{
    "titulo": "Reunión Importante",
    "descripcion": "Datos confidenciales",
    "fechaInicio": "2024-12-01T10:00:00",
    "fechaFin": "2024-12-01T11:30:00",
    "ubicacion": "Sala de Juntas",
    "todoElDia": false,
    "tipo": "EVENTO_LABORAL",
    "calendarioId": 1
  }'
```

### 4. Configurar una Alerta

```bash
curl -X POST http://localhost:8080/alerts \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {TOKEN_AQUI}" \
  -d '{
    "eventoId": 1,
    "fechaAlerta": "2024-12-01T09:45:00",
    "tipoNotificacion": "PUSH",
    "minutosAnticipacion": 15,
    "mensaje": "Recordatorio: Reunión en 15 minutos"
  }'
```

## 📚 RESPUESTAS A LAS PREGUNTAS DEL EJERCICIO

### a) Modelado de Arquitectura ✅

**Archivo**: `diagrama.xml`

**Estilo Arquitectónico Implementado**:
- **Principal**: Arquitectura en Capas (Layered Architecture)
- **Complementario**: Arquitectura Orientada a Servicios (SOA)
- **Patrón**: API Gateway

**4 Capas Definidas**:
1. **Capa de Presentación**: Web, Móvil, Tablet, Servicios Externos
2. **Capa de Integración**: API Gateway HTTPS (Controllers)
3. **Capa de Servicios**: AuthService, CalendarService, NotificationService, EncryptionService
4. **Capa de Infraestructura**: BD Usuarios, BD Calendarios (cifrados), Broker Mensajes

### b) Restricciones de la Solución ✅

**Archivo**: `RESTRICCIONES_ARQUITECTURA.md`

**10 Categorías de Restricciones Documentadas**:
1. Restricciones Generales (separación de capas)
2. Restricciones de Seguridad (JWT, cifrado AES, HTTPS)
3. Restricciones de Permisos (control de acceso)
4. Restricciones de Infraestructura (BD separadas)
5. Restricciones de Escalabilidad (stateless)
6. Restricciones de Disponibilidad (múltiples canales)
7. Restricciones de Interoperabilidad (API REST)
8. Restricciones de Plataforma (multiplataforma)
9. Restricciones Técnicas (stack tecnológico)
10. Limitaciones Conocidas (conocidas y documentadas)

**Restricciones Principales**:
- ✅ Autenticación JWT obligatoria
- ✅ Cifrado AES de datos sensibles
- ✅ HTTPS en comunicaciones
- ✅ Control de acceso por propietario
- ✅ Mensajería asíncrona con RabbitMQ
- ✅ Arquitectura stateless

### c) Diseño de APIs en SwaggerHub ✅

**Archivo**: `openapi-spec.yaml`

**Contenido**:
- Especificación OpenAPI 3.0 completa
- 10 endpoints documentados
- Schemas de request/response
- Ejemplos de uso
- Autenticación JWT configurada
- Listo para importar en SwaggerHub

**Endpoints Implementados**:
1. POST /auth/login - Iniciar sesión
2. POST /auth/register - Registrar usuario
3. POST /auth/token - Validar token
4. GET /calendar - Obtener calendarios
5. GET /calendar/view - Vista preparada (descifrado)
6. GET /calendar/{id} - Calendario por ID
7. POST /calendar - Crear calendario
8. POST /calendar/events - Agregar evento
9. GET /alerts - Obtener alertas
10. POST /alerts - Configurar alerta
11. POST /notifications/shown - Confirmar entrega

## 🎯 CARACTERÍSTICAS IMPLEMENTADAS

### Seguridad
- ✅ Autenticación JWT (24h de expiración)
- ✅ Cifrado AES de calendarios y eventos
- ✅ BCrypt para contraseñas
- ✅ Spring Security configurado
- ✅ Control de acceso por permisos

### Funcionalidades
- ✅ Registro y login de usuarios
- ✅ Gestión de calendarios (público/privado)
- ✅ Eventos con fechas y ubicación
- ✅ Alertas programables (PUSH, EMAIL, SMS)
- ✅ Scheduler automático (cada 60s)
- ✅ Descifrado automático en vistas

### Arquitectura
- ✅ 4 capas bien definidas
- ✅ Separación de responsabilidades
- ✅ Stateless (escalable)
- ✅ RESTful APIs
- ✅ Message Broker (RabbitMQ)

### Documentación
- ✅ Swagger/OpenAPI integrado
- ✅ README completo
- ✅ Restricciones documentadas
- ✅ Ejemplos de uso
- ✅ Diagrama de arquitectura XML

## 📋 PRÓXIMOS PASOS

### Para SwaggerHub:
✅ **YA ESTÁ PUBLICADO**: [https://app.swaggerhub.com/apis/udla-52c/api-sistema-de-calendario/1.0.0](https://app.swaggerhub.com/apis/udla-52c/api-sistema-de-calendario/1.0.0)

La API está completamente documentada y disponible en SwaggerHub. Puedes:
- Ver la documentación interactiva
- Probar los endpoints
- Generar código cliente
- Compartir con tu equipo

### Para Producción:
1. Cambiar de H2 a PostgreSQL/MySQL
2. Configurar certificados SSL/TLS
3. Usar variables de entorno para secrets
4. Configurar RabbitMQ en cluster
5. Implementar proveedores reales (Push, Email, SMS)

### Mejoras Opcionales:
1. Implementar permisos compartidos en calendarios
2. Agregar calendario público global
3. Implementar exportación a iCal
4. Agregar sincronización con Google Calendar
5. Implementar WebSockets para notificaciones en tiempo real

## 🐛 TROUBLESHOOTING

### Si hay errores de compilación:
```bash
mvn clean install -DskipTests
```

### Si no arranca RabbitMQ:
El sistema funciona sin RabbitMQ, simula el envío con logs

### Si faltan dependencias:
```bash
mvn dependency:resolve
mvn dependency:tree
```

### Si necesitas regenerar el proyecto:
```bash
mvn clean install -U
```

## 📞 HERRAMIENTAS EXTERNAS NECESARIAS (OPCIONALES)

### 1. RabbitMQ (Para notificaciones asíncronas)
```bash
# macOS
brew install rabbitmq
brew services start rabbitmq

# URL: http://localhost:15672
# User/Pass: guest/guest
```

**Nota**: El sistema funciona SIN RabbitMQ, solo simula con logs.

### 2. PostgreSQL (Para producción)
```bash
# macOS
brew install postgresql
brew services start postgresql

# Crear base de datos
createdb calendario_db
```

### 3. Postman (Para testing de APIs)
- Importar desde http://localhost:8080/api-docs
- O usar Swagger UI directamente

### 4. SwaggerHub (Para diseño de APIs)
- https://app.swaggerhub.com
- Importar `openapi-spec.yaml`

## ✨ RESUMEN

**PROYECTO COMPLETO Y FUNCIONAL**

- ✅ 40+ archivos creados
- ✅ Arquitectura en capas implementada
- ✅ Seguridad JWT + Cifrado AES
- ✅ API RESTful documentada
- ✅ Sistema de notificaciones asíncrono
- ✅ Swagger UI integrado
- ✅ Documentación completa
- ✅ Restricciones documentadas
- ✅ OpenAPI spec para SwaggerHub

**TODO ESTÁ LISTO PARA USAR** 🎉

Ejecuta: `mvn spring-boot:run` y accede a http://localhost:8080/swagger-ui.html

