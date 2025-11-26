package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para respuesta de autenticación con token JWT
 * Endpoint: /auth/login (response)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;
    private String tipo = "Bearer";
    private Long usuarioId;
    private String username;
    private String email;

    public AuthResponse(String token, Long usuarioId, String username, String email) {
        this.token = token;
        this.usuarioId = usuarioId;
        this.username = username;
        this.email = email;
    }
}

