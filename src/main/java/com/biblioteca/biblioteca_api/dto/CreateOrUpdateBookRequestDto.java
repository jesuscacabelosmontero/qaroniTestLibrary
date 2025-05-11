package com.biblioteca.biblioteca_api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para crear o actualizar un libro con su título y lista de IDs de autores.")
public class CreateOrUpdateBookRequestDto {
    @Schema(description = "Título del libro", example = "Don Quijote de la Mancha")
    private String title;
    @Schema(description = "Lista de IDs de los autores asociados al libro", example = "[1, 2]")
    private List<Long> authorIds;
}