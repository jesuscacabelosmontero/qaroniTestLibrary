package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.dto.AuthorDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateAuthorRequestDto;
import com.biblioteca.biblioteca_api.service.AuthorService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAuthorDetail(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createAuthor(
        @RequestBody CreateOrUpdateAuthorRequestDto author
        ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(author));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(
        @PathVariable Long id, 
        @RequestBody CreateOrUpdateAuthorRequestDto updatedAuthor
        ) {
        AuthorDto authorUpdated = authorService.updateAuthor(id, updatedAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(authorUpdated);

    }
}

