package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.model.Book;
import com.biblioteca.biblioteca_api.service.BookService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookDetail(@PathVariable Long id) {
        Book book = bookService.getBookDetail(id);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PostMapping
    public ResponseEntity<?> createBook(
            @RequestBody Book book,
            @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionId,
            HttpSession session
            ) {
        if (sessionId.isEmpty() || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable Long id, @RequestBody Book updatedBook,
            @CookieValue(value = "JSESSIONID", defaultValue = "") String sessionId,
            HttpSession session
            ) {   
        if (sessionId.isEmpty() || session.getAttribute("user") == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login required");
        }
        Book bookUpdated = bookService.updateBook(id, updatedBook);
        if (bookUpdated == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(bookUpdated);
    }
}