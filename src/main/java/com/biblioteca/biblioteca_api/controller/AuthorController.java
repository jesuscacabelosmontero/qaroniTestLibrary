package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.service.AuthorService;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorDetail(@PathVariable Long id) {
        Author author = authorService.getAuthorDetail(id);
        if (author == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(
        @RequestBody Author author,
        @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionId,
        HttpSession session
        ) {
        if (sessionId.isEmpty() || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(author));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(
        @PathVariable Long id, 
        @RequestBody Author updatedAuthor,
        @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionId,
        HttpSession session
        ) {
        if (sessionId.isEmpty() || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }
        Author authorUpdated = authorService.updateAuthor(id, updatedAuthor);
        if (authorUpdated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Author not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(authorUpdated);

    }
}

