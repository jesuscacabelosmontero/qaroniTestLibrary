package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.dto.BookDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateBookRequestDto;
import com.biblioteca.biblioteca_api.service.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "BookController", description = "Endpoints relacionados con los libros")
@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(summary = "Obtener todos los libros", description = "Devuelve una lista de todos los libros registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de libros",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookDto.class))))
    @GetMapping
    public ResponseEntity<List<BookDto>> getAllBooks() {
        return ResponseEntity.status(HttpStatus.OK).body(bookService.getAllBooks());
    }

    @Operation(summary = "Obtener un libro por ID", description = "Devuelve los detalles de un libro específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro encontrado",
            content = @Content(schema = @Schema(implementation = BookDto.class))),
        @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content(mediaType = "text/plain;charset=UTF-8"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookDetail(@PathVariable Long id) {
        BookDto book = bookService.getBookDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Crear un nuevo libro", description = "Crea un nuevo libro con una lista de autores asociados. Requiere autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Libro creado exitosamente",
            content = @Content(schema = @Schema(implementation = BookDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (autores no encontrados)", content = @Content(mediaType = "text/plain;charset=UTF-8")),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public ResponseEntity<?> createBook(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del libro a crear", required = true
        )
        @RequestBody CreateOrUpdateBookRequestDto bookRequest
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookService.createBook(bookRequest));
    }

    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "Editar un libro existente", description = "Edita la información de un libro y sus autores asociados. Requiere autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Libro actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = BookDto.class))),
        @ApiResponse(responseCode = "404", description = "Libro o autor no encontrado", content = @Content(mediaType = "text/plain;charset=UTF-8")),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
        @PathVariable Long id, 
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para editar el libro", required = true
        )
        @RequestBody CreateOrUpdateBookRequestDto updatedBook
    ) {   
        BookDto bookUpdated = bookService.updateBook(id, updatedBook);
        return ResponseEntity.status(HttpStatus.OK).body(bookUpdated);
    }
}