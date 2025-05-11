package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.config.TestSecurityConfig;
import com.biblioteca.biblioteca_api.dto.LoginRequest;
import com.biblioteca.biblioteca_api.model.Role;
import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.repository.UserRepository;
import com.biblioteca.biblioteca_api.security.JwtTokenUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(LoginController.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Test
    @DisplayName("POST /auth/login - correcto")
    void login_success() throws Exception {
        Role mockRole = new Role(1L, "USER", null); 
        User mockUser = new User(1L, "test@example.com", "password123", "John Doe", List.of(mockRole));
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password123");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));
        when(jwtTokenUtil.generateToken(any(User.class))).thenReturn("mock-jwt-token");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"password123\"}"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value("mock-jwt-token"));
    }

    @Test
    @DisplayName("POST /auth/login - error, email incorrecto")
    void login_failure_invalidCredentials() throws Exception {
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"wrongpassword\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid credentials")); 
    }

    @Test
    @DisplayName("POST /auth/login - error contraseña incorrecta")
    void login_failure_incorrectPassword() throws Exception {
        Role mockRole = new Role(1L, "USER", null); 
        User mockUser = new User(1L, "test@example.com", "password123", "John Doe", List.of(mockRole));
        LoginRequest loginRequest = new LoginRequest("test@example.com", "wrongpassword");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(mockUser));

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@example.com\", \"password\": \"wrongpassword\"}"))
            .andExpect(status().isUnauthorized())
            .andExpect(content().string("Invalid credentials"));  
    }
}