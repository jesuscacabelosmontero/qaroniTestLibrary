package com.biblioteca.biblioteca_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para solicitar el inicio de sesión. Contiene las credenciales del usuario.")
public class LoginRequest {

    @Schema(description = "Correo electrónico del usuario", example = "user@example.com")
    private String email;

    @Schema(description = "Contraseña del usuario", example = "password123")
    private String password;
}