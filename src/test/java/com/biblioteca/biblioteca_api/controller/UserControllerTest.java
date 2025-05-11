package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.config.TestSecurityConfig;  // Importación de la configuración de seguridad
import com.biblioteca.biblioteca_api.dto.UserRegistrationDto;
import com.biblioteca.biblioteca_api.security.JwtTokenUtil;
import com.biblioteca.biblioteca_api.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@Import(TestSecurityConfig.class)  // Importación añadida para la configuración de seguridad
@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("POST /users/register - éxito (registro de usuario)")
    void registerUser_success() throws Exception {
        UserRegistrationDto newUserDto = new UserRegistrationDto(
            "newuser@example.com", 
            "password123", 
            "John Doe", 
            List.of("USER")
        );

        doNothing().when(userService).registerUser(any(UserRegistrationDto.class));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"newuser@example.com\", \"password\": \"password123\", \"name\": \"John Doe\", \"roles\": [\"USER\"]}"))
            .andExpect(status().isCreated()) 
            .andExpect(content().string("Usuario registrado con éxito.")); 
    }

    @Test
    @DisplayName("POST /users/register - fallo (correo ya en uso)")
    void registerUser_emailAlreadyInUse() throws Exception {
        UserRegistrationDto newUserDto = new UserRegistrationDto(
            "existinguser@example.com", 
            "password123", 
            "Jane Doe", 
            List.of("USER")
        );

        doThrow(new IllegalArgumentException("Email already in use")).when(userService).registerUser(any(UserRegistrationDto.class));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"existinguser@example.com\", \"password\": \"password123\", \"name\": \"Jane Doe\", \"roles\": [\"USER\"]}"))
            .andExpect(status().isConflict())  
            .andExpect(content().string("El correo electrónico ya está en uso.")); 
    }

    @Test
    @DisplayName("POST /users/register - fallo (datos inválidos)")
    void registerUser_invalidData() throws Exception {
        UserRegistrationDto newUserDto = new UserRegistrationDto(
            "", 
            "password123", 
            "", 
            List.of("USER")
        );

        doThrow(new IllegalArgumentException("Invalid user data")).when(userService).registerUser(any(UserRegistrationDto.class));

        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"\", \"password\": \"password123\", \"name\": \"\", \"roles\": [\"USER\"]}"))
            .andExpect(status().isBadRequest())  
            .andExpect(content().string("Invalid user data")); 
    }
}