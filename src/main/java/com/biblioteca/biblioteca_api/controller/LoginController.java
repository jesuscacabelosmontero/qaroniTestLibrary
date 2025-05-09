package com.biblioteca.biblioteca_api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.biblioteca.biblioteca_api.dto.LoginRequest;
import com.biblioteca.biblioteca_api.model.User;
import com.biblioteca.biblioteca_api.service.AuthenticationService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class LoginController {

    @Autowired
    private AuthenticationService authenticationService;
    

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session, HttpServletResponse response) {
        User user = authenticationService.authenticate(request);
        if (user == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Wrong credentials");
        }
        session.setAttribute("user", user);
        Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(3600);
        sessionCookie.setPath("/"); 
        response.addCookie(sessionCookie);
        return ResponseEntity.status(HttpStatus.OK).body("Logged correctly");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session, HttpServletResponse response) {
        session.invalidate();
        Cookie cookie = new Cookie("JSESSIONID", null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);  
        cookie.setPath("/"); 
        response.addCookie(cookie);
        return ResponseEntity.ok("Logged out successfully.");
    }
}
