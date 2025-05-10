package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.dto.UserRegistrationDto;
import com.biblioteca.biblioteca_api.model.Role;
import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.repository.RoleRepository;
import com.biblioteca.biblioteca_api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void registerUser(UserRegistrationDto newUserDto) {
        if (userRepository.findByEmail(newUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use.");
        }

        List<Role> userRoles = newUserDto.getRoles().stream()
        .map(roleName -> roleRepository.findByRole(roleName.toUpperCase())
            .orElseThrow(() -> new IllegalArgumentException("Invalid role: " + roleName)))
        .collect(Collectors.toList());

        User user = new User();
        user.setEmail(newUserDto.getEmail());
        user.setName(newUserDto.getName());
        user.setPassword(newUserDto.getPassword());
        user.setRoles(userRoles);

        userRepository.save(user);
        sendWelcomeEmail(user.getEmail(), user.getName());
    }

    private void sendWelcomeEmail(String to, String name) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("bibliotecaprueba184@gmail.com");
        message.setTo(to);
        message.setSubject("Welcome to the library");
        message.setText("Hello " + name + ",\n\nthanks for signing up to our library.");
        mailSender.send(message);
    }
}