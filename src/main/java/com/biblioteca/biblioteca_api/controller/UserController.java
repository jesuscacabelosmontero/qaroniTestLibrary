package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.dto.UserRegistrationDto;
import com.biblioteca.biblioteca_api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto newUserDto) {
        try {
            userService.registerUser(newUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }
}