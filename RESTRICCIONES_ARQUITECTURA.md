# RESTRICCIONES DE LA ARQUITECTURA DEL SISTEMA DE CALENDARIO

## 1. RESTRICCIONES GENERALES

### 1.1 Estilo Arquitectónico
- **Estilo Principal**: Arquitectura en Capas (Layered Architecture)
- **Estilo Complementario**: Arquitectura Orientada a Servicios (SOA)
- **Patrón de Comunicación**: API Gateway con arquitectura REST

### 1.2 Restricción: Separación de Capas
**Descripción**: Las capas deben estar claramente separadas y la comunicación debe ser unidireccional (de arriba hacia abajo).

**Capas Definidas**:
1. **Capa de Presentación**: Interfaces de usuario (Web, Móvil, Tablet, Aplicaciones de terceros)
2. **Capa de Integración**: API Gateway HTTPS
3. **Capa de Servicios de Aplicación**: Lógica de negocio
4. **Capa de Infraestructura**: Persistencia, mensajería y servicios externos

**Implicaciones**:
- La capa de presentación NO puede acceder directamente a la capa de infraestructura
- Todos los accesos deben pasar por el API Gateway
- Los servicios de aplicación son los únicos que pueden acceder a la capa de infraestructura

---

## 2. RESTRICCIONES DE SEGURIDAD

### 2.1 Restricción: Autenticación Obligatoria
**Descripción**: Todo acceso al sistema requiere autenticación mediante credenciales (username + password).

**Implementación**:
- JWT (JSON Web Tokens) para autenticación stateless
- Tokens con expiración de 24 horas
- Encabezado Authorization con formato: `Bearer {token}`

**Endpoints Públicos** (sin autenticación):
- POST /auth/login
- POST /auth/register

**Endpoints Protegidos** (requieren token JWT):
- Todos los endpoints de /calendar/*
- Todos los endpoints de /alerts/*
- Todos los endpoints de /notifications/*

### 2.2 Restricción: Cifrado de Datos Sensibles
**Descripción**: Toda información sensible almacenada en calendarios y eventos DEBE ser cifrada en reposo.

**Implementación**:
- Algoritmo: AES (Advanced Encryption Standard)
- Los calendarios se almacenan cifrados en BD_Calendarios
- Los eventos con información sensible se cifran antes de almacenar
- El descifrado SOLO ocurre cuando un usuario autenticado solicita ver sus datos

**Flujo de Cifrado**:
1. Usuario ingresa datos → ServicioCifrado.cifrar() → Almacenamiento cifrado
2. Usuario solicita datos → Verificación de permisos → ServicioCifrado.descifrar() → Vista preparada

### 2.3 Restricción: Transporte Seguro
**Descripción**: Todas las comunicaciones DEBEN realizarse sobre HTTPS.

**Implicaciones**:
- API Gateway solo acepta conexiones HTTPS en producción
- Certificados SSL/TLS obligatorios
- HTTP redirige automáticamente a HTTPS

---

## 3. RESTRICCIONES DE PERMISOS

### 3.1 Restricción: Control de Acceso a Calendarios
**Descripción**: Los usuarios solo pueden acceder a calendarios para los cuales tienen permisos.

**Reglas**:
- Un usuario puede acceder a SUS PROPIOS calendarios
- Un usuario puede acceder a calendarios marcados como PÚBLICOS
- Cualquier intento de acceder a calendarios privados de otros usuarios resulta en error 403

**Validación**:
- Verificación en ServicioCalendario antes de cualquier operación
- Validación del propietario vs. usuario autenticado

### 3.2 Restricción: Integridad de Alertas
**Descripción**: Las alertas solo pueden ser configuradas por el propietario del calendario.

**Reglas**:
- Solo el propietario del calendario puede crear alertas para eventos
- Las alertas están vinculadas a un usuario específico
- Un usuario no puede ver alertas de otros usuarios

---

## 4. RESTRICCIONES DE INFRAESTRUCTURA

### 4.1 Restricción: Bases de Datos Separadas
**Descripción**: Los datos de usuarios y calendarios están en bases de datos conceptualmente separadas.

**Bases de Datos**:
- **BaseDatosUsuarios**: Información de autenticación (usuarios, credenciales, roles)
- **BD_Calendarios**: Calendarios y eventos (CIFRADOS)

**Ventajas**:
- Separación de responsabilidades
- Diferente nivel de seguridad por base de datos
- Posibilidad de escalar independientemente

### 4.2 Restricción: Mensajería Asíncrona
**Descripción**: Las notificaciones se procesan de forma asíncrona mediante un broker de mensajes.

**Implementación**:
- Broker: RabbitMQ (AMQP protocol)
- Las alertas se publican en el bus de eventos
- Los proveedores de notificaciones consumen del bus de forma asíncrona

**Colas**:
- `alertas.queue`: Nuevas alertas configuradas
- `notificaciones.push.queue`: Notificaciones Push pendientes
- `notificaciones.email.queue`: Notificaciones Email pendientes
- `notificaciones.sms.queue`: Notificaciones SMS pendientes
- `confirmaciones.queue`: Confirmaciones de entrega

---

## 5. RESTRICCIONES DE ESCALABILIDAD

### 5.1 Restricción: Stateless
**Descripción**: Los servicios deben ser stateless para permitir escalamiento horizontal.

**Implementación**:
- JWT permite autenticación sin estado de sesión
- No hay sesiones almacenadas en memoria del servidor
- Cada request contiene toda la información necesaria (token)

### 5.2 Restricción: Desacoplamiento de Servicios
**Descripción**: Los servicios están desacoplados mediante interfaces bien definidas.

**Servicios Independientes**:
- ServicioAutenticacion
- ServicioCalendario
- ServicioNotificaciones
- ServicioCifrado

**Ventajas**:
- Posibilidad de desplegar servicios independientemente
- Facilita testing unitario
- Permite microservicios en el futuro

---

## 6. RESTRICCIONES DE DISPONIBILIDAD

### 6.1 Restricción: Múltiples Canales de Notificación
**Descripción**: El sistema debe soportar múltiples proveedores de notificaciones.

**Proveedores**:
- PUSH: Notificaciones push a aplicaciones móviles
- EMAIL: Correo electrónico
- SMS: Mensajes de texto

**Implicaciones**:
- El sistema no depende de un único proveedor
- Failover automático si un proveedor falla
- Usuario puede elegir su canal preferido

### 6.2 Restricción: Confirmación de Entrega
**Descripción**: Las notificaciones requieren confirmación de entrega.

**Flujo**:
1. Notificación enviada al proveedor
2. Proveedor entrega al usuario
3. Aplicación cliente confirma mediante POST /notifications/shown
4. Sistema marca la alerta como "mostrada"

---

## 7. RESTRICCIONES DE INTEROPERABILIDAD

### 7.1 Restricción: API RESTful Estándar
**Descripción**: Todas las APIs deben seguir principios REST.

**Principios**:
- Verbos HTTP correctos (GET, POST, PUT, DELETE)
- Recursos bien definidos (/calendar, /alerts, etc.)
- Códigos de estado HTTP apropiados (200, 201, 400, 401, 403, 500)
- JSON como formato de intercambio

### 7.2 Restricción: Documentación OpenAPI
**Descripción**: Todas las APIs deben estar documentadas con OpenAPI/Swagger.

**Acceso**:
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

---

## 8. RESTRICCIONES DE PLATAFORMA

### 8.1 Restricción: Multiplataforma
**Descripción**: El sistema debe ser accesible desde múltiples plataformas.

**Plataformas Soportadas**:
- Navegador Web (Aplicación Web)
- Aplicación Móvil (iOS, Android)
- Aplicación Tablet
- Servicios Externos (APIs de terceros)

**Implicaciones**:
- API agnóstica a la plataforma
- Respuestas JSON consistentes
- CORS habilitado para clientes web

---

## 9. RESTRICCIONES TÉCNICAS

### 9.1 Stack Tecnológico
- **Backend**: Java 17 + Spring Boot 3.x
- **Seguridad**: Spring Security + JWT
- **Persistencia**: JPA/Hibernate + H2 (desarrollo) / PostgreSQL (producción)
- **Mensajería**: RabbitMQ (AMQP)
- **Documentación**: SpringDoc OpenAPI 3

### 9.2 Restricción: Transaccionalidad
**Descripción**: Operaciones críticas deben ser transaccionales.

**Operaciones Transaccionales**:
- Creación de usuarios
- Creación de calendarios
- Creación de eventos
- Configuración de alertas

---

## 10. LIMITACIONES CONOCIDAS

### 10.1 Limitación: Cifrado Simétrico
**Descripción**: Se utiliza AES con clave simétrica compartida.

**Limitación**: Si la clave se ve comprometida, todos los datos cifrados están en riesgo.

**Mitigación Recomendada**: 
- Implementar cifrado asimétrico (RSA + AES)
- Rotar claves periódicamente
- Usar Key Management Service (KMS) en producción

### 10.2 Limitación: Sincronía de RabbitMQ
**Descripción**: El sistema depende de RabbitMQ para notificaciones.

**Limitación**: Si RabbitMQ no está disponible, las notificaciones no se procesan.

**Mitigación Implementada**:
- Mensajes persistentes en colas durables
- Reintentos automáticos
- Logs de errores

### 10.3 Limitación: Base de Datos en Memoria (H2)
**Descripción**: En desarrollo se usa H2 (en memoria).

**Limitación**: Los datos se pierden al reiniciar.

**Solución**: En producción usar PostgreSQL o MySQL con persistencia.

---

## RESUMEN DE RESTRICCIONES CRÍTICAS

1. ✅ **Autenticación JWT obligatoria** para todos los endpoints protegidos
2. ✅ **Cifrado AES** de datos sensibles en reposo
3. ✅ **HTTPS** en todas las comunicaciones (producción)
4. ✅ **Control de acceso** basado en propietario de calendario
5. ✅ **Arquitectura en capas** estricta con separación de responsabilidades
6. ✅ **Mensajería asíncrona** para notificaciones mediante RabbitMQ
7. ✅ **Stateless** para escalabilidad horizontal
8. ✅ **API RESTful** documentada con OpenAPI/Swagger
9. ✅ **Multiplataforma** con soporte para web, móvil y terceros
10. ✅ **Transaccionalidad** en operaciones críticas

