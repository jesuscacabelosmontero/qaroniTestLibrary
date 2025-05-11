package com.biblioteca.biblioteca_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que contiene el token JWT generado después de un inicio de sesión exitoso.")
public class LoginResponse {

    @Schema(description = "Token JWT generado para el usuario", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

}