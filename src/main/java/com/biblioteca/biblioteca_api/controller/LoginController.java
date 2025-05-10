package com.biblioteca.biblioteca_api.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.biblioteca.biblioteca_api.dto.LoginRequest;
import com.biblioteca.biblioteca_api.dto.LoginResponse;
import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.repository.UserRepository;
import com.biblioteca.biblioteca_api.security.JwtTokenUtil;


@RestController
@RequestMapping("/auth")
public class LoginController {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LoginRequest loginRequest
    ) {
        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());

        if (userOpt.isEmpty() || !userOpt.get().getPassword().equals(passwordEncoder.encode(loginRequest.getPassword()))) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        User user = userOpt.get();
        String token = jwtTokenUtil.generateToken(user);

        return ResponseEntity.ok(new LoginResponse(token));
    }
}
