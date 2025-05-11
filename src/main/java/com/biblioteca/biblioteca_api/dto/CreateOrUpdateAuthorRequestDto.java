package com.biblioteca.biblioteca_api.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "DTO para crear o editar un autor, incluyendo los IDs de sus libros en caso de que esten creados.")
public class CreateOrUpdateAuthorRequestDto {
    @Schema(description = "Nombre del autor", example = "Isabel Allende")
    private String name;
    @Schema(description = "Lista de IDs de libros asociados al autor", example = "[101, 102]")
    private List<Long> bookIds;
}