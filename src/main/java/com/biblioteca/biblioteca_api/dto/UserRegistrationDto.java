package com.biblioteca.biblioteca_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO utilizado para el registro de un nuevo usuario en el sistema. Contiene los datos necesarios como correo, contraseña, nombre y roles asignados al usuario.")
public class UserRegistrationDto {
    @Schema(description = "Correo electrónico del usuario. Debe ser único.", example = "ejemplo@dominio.com", required = true)
    private String email;
    @Schema(description = "Contraseña del usuario. Debe tener al menos 8 caracteres y contener una combinación de letras y números.", example = "password123", required = true)
    private String password;
    @Schema(description = "Nombre completo del usuario.", example = "Juan Pérez", required = true)
    private String name;
    @Schema(description = "Lista de roles asignados al usuario. Los roles deben ser válidos y predefinidos en el sistema.", example = "[\"USER\", \"ADMIN\"]", required = true)
    private List<String> roles;
}