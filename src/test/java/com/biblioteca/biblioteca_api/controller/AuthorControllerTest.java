package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.config.TestSecurityConfig;
import com.biblioteca.biblioteca_api.dto.AuthorDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateAuthorRequestDto;
import com.biblioteca.biblioteca_api.exception.AuthorNotFoundException;
import com.biblioteca.biblioteca_api.security.JwtTokenUtil;
import com.biblioteca.biblioteca_api.service.AuthorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthorController.class)
@Import(TestSecurityConfig.class)
public class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;



@Test
    @DisplayName("GET /authors - debe retornar lista de autores")
    void getAllAuthors_returnsList() throws Exception {
        List<AuthorDto> mockAuthors = Arrays.asList(
            new AuthorDto(1L, "George Orwell", Arrays.asList("1984", "Animal Farm")),
            new AuthorDto(2L, "Gabriel García Márquez", Arrays.asList("Cien años de soledad", "El otoño del patriarca"))
        );

        when(authorService.getAllAuthors()).thenReturn(mockAuthors);

        mockMvc.perform(get("/authors"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].name").value("George Orwell"))
            .andExpect(jsonPath("$[0].bookTitles[0]").value("1984"))
            .andExpect(jsonPath("$[1].name").value("Gabriel García Márquez"))
            .andExpect(jsonPath("$[1].bookTitles[0]").value("Cien años de soledad"));
    }

    @Test
    @DisplayName("GET /authors/{id} - autor encontrado")
    void getAuthorDetail_returnsAuthor() throws Exception {
        AuthorDto mockAuthor = new AuthorDto(1L, "George Orwell", Arrays.asList("1984", "Animal Farm"));

        when(authorService.getAuthorDetail(1L)).thenReturn(mockAuthor);

        mockMvc.perform(get("/authors/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("George Orwell"))
            .andExpect(jsonPath("$.bookTitles[0]").value("1984"))
            .andExpect(jsonPath("$.bookTitles[1]").value("Animal Farm"));
    }

    @Test
    @DisplayName("POST /authors - crear autor exitoso")
    void createAuthor_returnsCreated() throws Exception {
        CreateOrUpdateAuthorRequestDto authorDto = new CreateOrUpdateAuthorRequestDto("Nuevo Autor", Arrays.asList(1L, 2L));

        AuthorDto mockResponse = new AuthorDto(1L, "Nuevo Autor", Arrays.asList("Libro 1", "Libro 2"));

        when(authorService.createAuthor(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Nuevo Autor"))
            .andExpect(jsonPath("$.bookTitles[0]").value("Libro 1"))
            .andExpect(jsonPath("$.bookTitles[1]").value("Libro 2"));
    }


    @Test
    @DisplayName("PUT /authors/{id} - actualizar autor exitoso")
    void updateAuthor_returnsUpdated() throws Exception {
        CreateOrUpdateAuthorRequestDto authorDto = new CreateOrUpdateAuthorRequestDto("Autor Actualizado", Arrays.asList(1L, 2L));

        AuthorDto mockResponse = new AuthorDto(1L, "Autor Actualizado", Arrays.asList("Nuevo Libro"));

        when(authorService.updateAuthor(Mockito.eq(1L), any())).thenReturn(mockResponse);

        mockMvc.perform(put("/authors/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Autor Actualizado"))
            .andExpect(jsonPath("$.bookTitles[0]").value("Nuevo Libro"));
    }

    @Test
    @DisplayName("PUT /authors/{id} - autor no encontrado")
    void updateAuthor_authorNotFound_returnsNotFound() throws Exception {
        CreateOrUpdateAuthorRequestDto authorDto = new CreateOrUpdateAuthorRequestDto("Autor Inexistente", Arrays.asList(1L, 2L));

        when(authorService.updateAuthor(Mockito.eq(999L), any()))
            .thenThrow(new AuthorNotFoundException("Author with id 999 not found"));

        mockMvc.perform(put("/authors/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(authorDto)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Author with id 999 not found"));
    }
}