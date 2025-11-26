# ✅ ERROR CORREGIDO - SecurityConfig RequestMatchers

## ❌ ERROR ORIGINAL

```
Caused by: java.lang.IllegalArgumentException: This method cannot decide whether these patterns are Spring MVC patterns or not. If this endpoint is a Spring MVC endpoint, please use requestMatchers(MvcRequestMatcher); otherwise, please use requestMatchers(AntPathRequestMatcher).
This is because there is more than one mappable servlet in your servlet context: {org.h2.server.web.JakartaWebServlet=[/h2-console/*], org.springframework.web.servlet.DispatcherServlet=[/]}
```

## 🔍 CAUSA DEL PROBLEMA

En **Spring Security 6.x**, cuando hay múltiples servlets en el contexto (H2 Console + DispatcherServlet), el método `requestMatchers(String...)` no puede determinar automáticamente qué tipo de matcher usar.

**Conflicto**:
- `org.h2.server.web.JakartaWebServlet` en `/h2-console/*`
- `org.springframework.web.servlet.DispatcherServlet` en `/`

## ✅ SOLUCIÓN APLICADA

### Cambio en SecurityConfig.java

**ANTES** (incorrecto):
```java
.authorizeHttpRequests(auth -> auth
    .requestMatchers("/auth/**", "/h2-console/**", "/swagger-ui/**",
                     "/api-docs/**", "/v3/api-docs/**").permitAll()
    .anyRequest().authenticated()
)
```

**DESPUÉS** (correcto):
```java
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

.authorizeHttpRequests(auth -> auth
    .requestMatchers(
        new AntPathRequestMatcher("/auth/**"),
        new AntPathRequestMatcher("/h2-console/**"),
        new AntPathRequestMatcher("/swagger-ui/**"),
        new AntPathRequestMatcher("/swagger-ui.html"),
        new AntPathRequestMatcher("/api-docs/**"),
        new AntPathRequestMatcher("/v3/api-docs/**")
    ).permitAll()
    .anyRequest().authenticated()
)
```

## 📝 CAMBIOS REALIZADOS

1. ✅ **Agregado import**: `AntPathRequestMatcher`
2. ✅ **Actualizado método**: Uso explícito de `AntPathRequestMatcher` para cada patrón
3. ✅ **Añadido patrón**: `/swagger-ui.html` para acceso directo

## 🎯 RESULTADO

Ahora Spring Security sabe exactamente qué tipo de matcher usar para cada patrón, resolviendo la ambigüedad.

## 🚀 SIGUIENTE PASO

**Ejecutar la aplicación nuevamente**:

1. En IntelliJ IDEA:
   - Click derecho en `CalendarioApplication.java`
   - **Run 'CalendarioApplication'**

2. Esperar mensaje:
   ```
   Started CalendarioApplication in X seconds
   ```

3. Verificar:
   - http://localhost:8080/swagger-ui.html ✅
   - http://localhost:8080/h2-console ✅

## 📚 REFERENCIA

**Spring Security 6.x Documentation**:
- Cuando hay múltiples servlets, siempre usar matchers explícitos
- `AntPathRequestMatcher` para rutas estándar
- `MvcRequestMatcher` para endpoints Spring MVC específicos

## ✅ VERIFICACIÓN

### Endpoints Públicos (sin autenticación):
- `/auth/**` - Login y registro
- `/h2-console/**` - Consola de base de datos
- `/swagger-ui/**` - Documentación Swagger
- `/api-docs/**` - OpenAPI docs

### Endpoints Protegidos (requieren JWT):
- `/calendar/**` - Gestión de calendarios
- `/alerts/**` - Gestión de alertas
- Todos los demás endpoints

---

**Fecha**: 26 de Noviembre, 2025  
**Error**: `IllegalArgumentException` en SecurityConfig  
**Solución**: Uso explícito de `AntPathRequestMatcher`  
**Estado**: ✅ **RESUELTO**

