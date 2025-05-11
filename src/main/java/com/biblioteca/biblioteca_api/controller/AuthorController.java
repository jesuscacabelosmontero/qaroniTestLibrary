package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.dto.AuthorDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateAuthorRequestDto;
import com.biblioteca.biblioteca_api.service.AuthorService;

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

@Tag(name = "AuthorController", description = "Endpoints relacionadas con autores")
@RestController
@RequestMapping("/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(summary = "Obtener todos los autores", description = "Devuelve una lista de todos los autores registrados.")
    @ApiResponse(responseCode = "200", description = "Lista de autores",
        content = @Content(array = @ArraySchema(schema = @Schema(implementation = AuthorDto.class))))
    @GetMapping
    public ResponseEntity<List<AuthorDto>> getAllAuthors() {
        return ResponseEntity.status(HttpStatus.OK).body(authorService.getAllAuthors());
    }

    @Operation(summary = "Obtener un autor por ID", description = "Devuelve los detalles de un autor específico por su ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor encontrado",
            content = @Content(schema = @Schema(implementation = AuthorDto.class))),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content(mediaType = "text/plain;charset=UTF-8"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorDto> getAuthorDetail(@PathVariable Long id) {
        AuthorDto author = authorService.getAuthorDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @Operation(summary = "Crear un nuevo autor", description = "Crea un nuevo autor con una lista de libros asociados. Requiere autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Autor creado exitosamente",
            content = @Content(schema = @Schema(implementation = AuthorDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos inválidos (libros no encontrados)", content = @Content(mediaType = "text/plain;charset=UTF-8")),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<AuthorDto> createAuthor(
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos del autor a crear", required = true
        )
        @RequestBody CreateOrUpdateAuthorRequestDto author
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authorService.createAuthor(author));
    }

    @Operation(summary = "Editar un autor existente", description = "Edita la información de un autor y sus libros asociados. Requiere autenticación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Autor actualizado exitosamente",
            content = @Content(schema = @Schema(implementation = AuthorDto.class))),
        @ApiResponse(responseCode = "404", description = "Autor o libro no encontrado", content = @Content(mediaType = "text/plain;charset=UTF-8")),
        @ApiResponse(responseCode = "401", description = "No autenticado", content = @Content(mediaType = "application/json"))
    })
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDto> updateAuthor(
        @PathVariable Long id,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos para editar el autor", required = true
        )
        @RequestBody CreateOrUpdateAuthorRequestDto updatedAuthor
    ) {
        AuthorDto authorUpdated = authorService.updateAuthor(id, updatedAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(authorUpdated);
    }
}