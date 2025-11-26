package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.AuthResponse;
import org.example.dto.LoginRequest;
import org.example.dto.RegistroRequest;
import org.example.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticacion", description = "Endpoints para autenticacion y registro de usuarios")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesion",
               description = "Autentica un usuario y retorna un token JWT")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            AuthResponse response = authService.autenticarUsuario(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401)
                    .body(new AuthResponse(null, null, null, "Credenciales invalidas"));
        }
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario",
               description = "Registra un nuevo usuario en el sistema")
    public ResponseEntity<AuthResponse> registrar(@Valid @RequestBody RegistroRequest request) {
        try {
            AuthResponse response = authService.registrarUsuario(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(new AuthResponse(null, null, null, e.getMessage()));
        }
    }

    @PostMapping("/token")
    @Operation(summary = "Validar token",
               description = "Valida un token JWT existente")
    public ResponseEntity<String> validarToken(@RequestHeader("Authorization") String token) {
        try {
            return ResponseEntity.ok("Token valido");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token invalido");
        }
    }
}

