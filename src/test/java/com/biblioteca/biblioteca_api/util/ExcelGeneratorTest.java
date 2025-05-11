package com.biblioteca.biblioteca_api.util;

import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.model.Book;
import com.biblioteca.biblioteca_api.repository.AuthorRepository;
import com.biblioteca.biblioteca_api.repository.BookRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcelGeneratorTest {

    @InjectMocks
    private ExcelGenerator excelGenerator;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateExcel_returnsNonEmptyByteArray() {
        Author author = new Author();
        author.setName("Gabriel García Márquez");
        Book book = new Book();
        author.setBooks(List.of(book));

        when(bookRepository.count()).thenReturn(10L);
        when(authorRepository.count()).thenReturn(1L);
        when(authorRepository.findAll()).thenReturn(List.of(author));

        byte[] result = excelGenerator.generateExcel();

        assertNotNull(result);
        assertTrue(result.length > 0);
        verify(bookRepository).count();
        verify(authorRepository).count();
        verify(authorRepository).findAll();
    }


}
