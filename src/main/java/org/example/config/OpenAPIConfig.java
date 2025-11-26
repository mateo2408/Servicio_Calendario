package org.example.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuracion de OpenAPI/Swagger para documentacion de APIs
 * Accesible en: /swagger-ui.html
 */
@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "API Sistema de Calendario",
        version = "1.0",
        description = "API RESTful para gestion de calendarios con autenticacion JWT, " +
                      "cifrado de datos sensibles y sistema de notificaciones. " +
                      "Arquitectura en capas con API Gateway, servicios de aplicacion e infraestructura.",
        contact = @Contact(
            name = "Sistema de Calendario",
            email = "soporte@calendario.com"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor de Desarrollo"),
        @Server(url = "https://api.calendario.com", description = "Servidor de Produccion (HTTPS)")
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Autenticacion mediante token JWT. Formato: Bearer {token}"
)
public class OpenAPIConfig {
    // La configuracion se realiza mediante anotaciones
}


