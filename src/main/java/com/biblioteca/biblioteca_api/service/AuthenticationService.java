package com.biblioteca.biblioteca_api.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.biblioteca.biblioteca_api.dto.LoginRequest;
import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class AuthenticationService {
    
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public User authenticate(LoginRequest request) {
        Optional<User> userOptional = userRepository.findByEmail(request.getEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                return user;
            }
        }
        return null;
    }

}
