package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.dto.BookDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateBookRequestDto;
import com.biblioteca.biblioteca_api.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookDetail(@PathVariable Long id) {
        BookDto book = bookService.getBookDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<?> createBook(
        @RequestBody CreateOrUpdateBookRequestDto bookRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
        @PathVariable Long id, 
        @RequestBody CreateOrUpdateBookRequestDto updatedBook
    ) {   
        BookDto bookUpdated = bookService.updateBook(id, updatedBook);
        return ResponseEntity.status(HttpStatus.OK).body(bookUpdated);
    }
}