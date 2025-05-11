package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.dto.UserRegistrationDto;
import com.biblioteca.biblioteca_api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@Tag(name = "UserController", description = "Endpoints relacionadas con los usuarios.")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Registrar un nuevo usuario", description = "Permite registrar un nuevo usuario proporcionando información necesaria como nombre, correo electrónico y roles y manda un correo de bienvenida.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado con éxito", content = @Content(mediaType = "text/plain;charset=UTF-8")),
        @ApiResponse(responseCode = "400", description = "Solicitud incorrecta - error de validación o datos no válidos", content = @Content(mediaType = "text/plain;charset=UTF-8")),
        @ApiResponse(responseCode = "409", description = "Conflicto - El correo electrónico ya está en uso", content = @Content(mediaType = "text/plain;charset=UTF-8"))
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto newUserDto) {
        try {
            userService.registerUser(newUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario registrado con éxito.");
        } catch (IllegalArgumentException e) {
            if (e.getMessage().contains("Email already in use")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("El correo electrónico ya está en uso.");
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}