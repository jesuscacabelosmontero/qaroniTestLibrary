package com.biblioteca.biblioteca_api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa a un autor con los títulos de sus libros.")
public class AuthorDto {
    @Schema(description = "ID único del autor", example = "1")
    private Long id;
    @Schema(description = "Nombre del autor", example = "Gabriel García Márquez")
    private String name;
    @Schema(description = "Lista de títulos de los libros del autor", example = "[\"Cien años de soledad\", \"Teo va de excursión\"]")
    private List<String> bookTitles; 
}