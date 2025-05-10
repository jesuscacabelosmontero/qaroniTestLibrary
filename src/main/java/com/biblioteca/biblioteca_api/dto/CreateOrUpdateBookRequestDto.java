package com.biblioteca.biblioteca_api.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrUpdateBookRequestDto {
    private String title;
    private List<Long> authorIds;
}