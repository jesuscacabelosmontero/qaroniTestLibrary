package com.biblioteca.biblioteca_api.controller;

import com.biblioteca.biblioteca_api.config.TestSecurityConfig;
import com.biblioteca.biblioteca_api.dto.BookDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateBookRequestDto;
import com.biblioteca.biblioteca_api.exception.AuthorNotFoundException;
import com.biblioteca.biblioteca_api.exception.BookNotFoundException;
import com.biblioteca.biblioteca_api.security.JwtTokenUtil;
import com.biblioteca.biblioteca_api.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;

import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestSecurityConfig.class)
@WebMvcTest(BookController.class)
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("GET /books - debe retornar lista de libros")
    void getAllBooks_returnsList() throws Exception {
        List<BookDto> mockBooks = Arrays.asList(
            new BookDto(1L, "1984", List.of("George Orwell")),
            new BookDto(2L, "Cien años de soledad", List.of("Gabriel García Márquez"))
        );

        Mockito.when(bookService.getAllBooks()).thenReturn(mockBooks);

        mockMvc.perform(get("/books"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.size()").value(2))
            .andExpect(jsonPath("$[0].title").value("1984"))
            .andExpect(jsonPath("$[1].authorNames[0]").value("Gabriel García Márquez"));
    }

    @Test
    @DisplayName("GET /books/{id} - libro encontrado")
    void getBookDetail_returnsBook() throws Exception {
        BookDto mockBook = new BookDto(1L, "1984", List.of("George Orwell"));
        Mockito.when(bookService.getBookDetail(1L)).thenReturn(mockBook);

        mockMvc.perform(get("/books/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("1984"));
    }

    @Test
    @DisplayName("GET /books/{id} - libro no encontrado")
    void getBookDetail_notFound() throws Exception {
        when(bookService.getBookDetail(99L))
            .thenThrow(new BookNotFoundException("Book with id 99 not found"));

        mockMvc.perform(get("/books/99"))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Book with id 99 not found"));
    }

    @Test
    @DisplayName("POST /books - crear libro autenticado")
    @WithMockUser // simula autenticación
    void createBook_authenticated_success() throws Exception {
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto("Nuevo Libro", List.of(1L, 2L));
        BookDto mockResponse = new BookDto(1L, "Nuevo Libro", List.of("Autor 1", "Autor 2"));

        Mockito.when(bookService.createBook(any())).thenReturn(mockResponse);

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.title").value("Nuevo Libro"));
    }

    @Test
    @WithMockUser
    @DisplayName("POST /books - autores no encontrados (400)")
    void createBook_authorsNotFound() throws Exception {
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto("Nuevo", List.of(999L));

        when(bookService.createBook(any()))
            .thenThrow(new AuthorNotFoundException("Authors not found with IDs: [999]"));

        mockMvc.perform(post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Authors not found with IDs: [999]"));
    }

    @Test
    @DisplayName("PUT /books/{id} - actualizar libro autenticado")
    @WithMockUser
    void updateBook_authenticated_success() throws Exception {
        CreateOrUpdateBookRequestDto updateDto = new CreateOrUpdateBookRequestDto("Título actualizado", List.of(1L));
        BookDto updatedBook = new BookDto(1L, "Título actualizado", List.of("Autor 1"));

        Mockito.when(bookService.updateBook(Mockito.eq(1L), any())).thenReturn(updatedBook);

        mockMvc.perform(put("/books/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.title").value("Título actualizado"));
    }

    @Test
    @WithMockUser
    @DisplayName("PUT /books/{id} - libro no encontrado")
    void updateBook_notFound() throws Exception {
        CreateOrUpdateBookRequestDto dto = new CreateOrUpdateBookRequestDto("Título", List.of(1L));

        when(bookService.updateBook(eq(999L), any()))
            .thenThrow(new BookNotFoundException("Book with id 999 not found"));

        mockMvc.perform(put("/books/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Book with id 999 not found"));
    }
}