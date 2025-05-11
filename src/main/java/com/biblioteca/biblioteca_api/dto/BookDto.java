package com.biblioteca.biblioteca_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO que representa un libro con su ID, título y nombres de autores.")
public class BookDto {
    @Schema(description = "ID único del libro", example = "1")
    private Long id;
    @Schema(description = "Título del libro", example = "El Quijote")
    private String title;
    @Schema(description = "Lista de nombres de autores del libro", example = "[\"Miguel de Cervantes\"]")
    private List<String> authorNames;
}