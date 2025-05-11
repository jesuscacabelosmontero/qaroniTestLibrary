package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.dto.AuthorDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateAuthorRequestDto;
import com.biblioteca.biblioteca_api.exception.AuthorNotFoundException;
import com.biblioteca.biblioteca_api.exception.BookNotFoundException;
import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.model.Book;
import com.biblioteca.biblioteca_api.repository.AuthorRepository;
import com.biblioteca.biblioteca_api.repository.BookRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AuthorService authorService;

    @Test
    void testGetAllAuthors_returnsAuthorDtoList() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Gabriel García Márquez");
        author1.setBooks(List.of());

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Isabel Allende");
        author2.setBooks(List.of());

        List<Author> authors = List.of(author1, author2);

        when(authorRepository.findAllByOrderByIdAsc()).thenReturn(authors);

        List<AuthorDto> result = authorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Gabriel García Márquez", result.get(0).getName());
        assertEquals("Isabel Allende", result.get(1).getName());
        verify(authorRepository).findAllByOrderByIdAsc();
    }

    @Test
    void testGetAuthorDetail_whenAuthorExists_returnsAuthorDto() {
        Book book1 = new Book();
        book1.setId(10L);
        book1.setTitle("Cien años de soledad");

        Book book2 = new Book();
        book2.setId(11L);
        book2.setTitle("El amor en los tiempos del cólera");

        Author author = new Author();
        author.setId(1L);
        author.setName("Gabriel García Márquez");
        author.setBooks(List.of(book1, book2));

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));

        AuthorDto result = authorService.getAuthorDetail(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gabriel García Márquez", result.getName());
        assertNotNull(result.getBookTitles());
        assertEquals(2, result.getBookTitles().size());
        assertTrue(result.getBookTitles().contains("Cien años de soledad"));
        assertTrue(result.getBookTitles().contains("El amor en los tiempos del cólera"));

        verify(authorRepository).findById(1L);
    }

    @Test
    void testGetAuthorDetail_whenAuthorNotFound_throwsException() {
        when(authorRepository.findById(99L)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(
            AuthorNotFoundException.class,
            () -> authorService.getAuthorDetail(99L)
        );

        assertEquals("Author with id 99 not found", exception.getMessage());
        verify(authorRepository).findById(99L);
    }

    @Test
    void testCreateAuthor_whenValidRequest_savesAndReturnsAuthorDto() {
        CreateOrUpdateAuthorRequestDto requestDto = new CreateOrUpdateAuthorRequestDto();
        requestDto.setName("Julio Cortázar");
        requestDto.setBookIds(List.of(100L, 101L));

        Book book1 = new Book();
        book1.setId(100L);
        book1.setTitle("Rayuela");

        Book book2 = new Book();
        book2.setId(101L);
        book2.setTitle("Bestiario");

        Author savedAuthor = new Author();
        savedAuthor.setId(1L);
        savedAuthor.setName("Julio Cortázar");
        savedAuthor.setBooks(List.of(book1, book2));

        when(bookRepository.findAllById(List.of(100L, 101L)))
            .thenReturn(List.of(book1, book2));
        when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);

        AuthorDto result = authorService.createAuthor(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Julio Cortázar", result.getName());
        assertEquals(2, result.getBookTitles().size());
        assertTrue(result.getBookTitles().contains("Rayuela"));
        assertTrue(result.getBookTitles().contains("Bestiario"));
        verify(bookRepository).findAllById(List.of(100L, 101L));
        verify(authorRepository).save(any(Author.class));
    }

    @Test
    void testCreateAuthor_whenBookIdsMissing_throwsBookNotFoundException() {
        CreateOrUpdateAuthorRequestDto requestDto = new CreateOrUpdateAuthorRequestDto();
        requestDto.setName("Ernesto Sabato");
        requestDto.setBookIds(List.of(200L, 201L)); 

        Book existingBook = new Book();
        existingBook.setId(200L);
        existingBook.setTitle("El túnel");

        when(bookRepository.findAllById(List.of(200L, 201L)))
            .thenReturn(List.of(existingBook));

        BookNotFoundException exception = assertThrows(
            BookNotFoundException.class,
            () -> authorService.createAuthor(requestDto)
        );

        assertEquals("Books not found with IDs: [201]", exception.getMessage());
        verify(bookRepository).findAllById(List.of(200L, 201L));
        verify(authorRepository, never()).save(any());
    }

    @Test
    void testUpdateAuthor_whenAuthorExistsAndBooksValid_updatesAndReturnsAuthorDto() {
        Long authorId = 1L;
        CreateOrUpdateAuthorRequestDto requestDto = new CreateOrUpdateAuthorRequestDto();
        requestDto.setName("Mario Vargas Llosa");
        requestDto.setBookIds(List.of(300L));

        Author existingAuthor = new Author();
        existingAuthor.setId(authorId);
        existingAuthor.setName("Nombre Antiguo");
        existingAuthor.setBooks(List.of()); 

        Book newBook = new Book();
        newBook.setId(300L);
        newBook.setTitle("La ciudad y los perros");

        Author updatedAuthor = new Author();
        updatedAuthor.setId(authorId);
        updatedAuthor.setName("Mario Vargas Llosa");
        updatedAuthor.setBooks(List.of(newBook));

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.findAllById(List.of(300L))).thenReturn(List.of(newBook));
        when(authorRepository.save(existingAuthor)).thenReturn(updatedAuthor);

        AuthorDto result = authorService.updateAuthor(authorId, requestDto);

        assertNotNull(result);
        assertEquals(authorId, result.getId());
        assertEquals("Mario Vargas Llosa", result.getName());
        assertEquals(1, result.getBookTitles().size());
        assertTrue(result.getBookTitles().contains("La ciudad y los perros"));
        verify(authorRepository).findById(authorId);
        verify(bookRepository).findAllById(List.of(300L));
        verify(authorRepository).save(existingAuthor);
    }

    @Test
    void testUpdateAuthor_whenAuthorNotFound_throwsAuthorNotFoundException() {
        Long authorId = 99L;
        CreateOrUpdateAuthorRequestDto requestDto = new CreateOrUpdateAuthorRequestDto();
        requestDto.setName("Autor Fantasma");
        requestDto.setBookIds(List.of(1L));

        when(authorRepository.findById(authorId)).thenReturn(Optional.empty());

        AuthorNotFoundException exception = assertThrows(
            AuthorNotFoundException.class,
            () -> authorService.updateAuthor(authorId, requestDto)
        );

        assertEquals("Author with id 99 not found", exception.getMessage());
        verify(authorRepository).findById(authorId);
        verify(bookRepository, never()).findAllById(any());
        verify(authorRepository, never()).save(any());
    }

    @Test
    void testUpdateAuthor_whenSomeBooksMissing_throwsBookNotFoundException() {
        Long authorId = 1L;
        CreateOrUpdateAuthorRequestDto requestDto = new CreateOrUpdateAuthorRequestDto();
        requestDto.setName("Autor X");
        requestDto.setBookIds(List.of(400L, 401L));

        Author existingAuthor = new Author();
        existingAuthor.setId(authorId);
        existingAuthor.setName("Nombre Antiguo");
        existingAuthor.setBooks(List.of());

        Book onlyBook = new Book();
        onlyBook.setId(400L);
        onlyBook.setTitle("Libro válido");

        when(authorRepository.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(bookRepository.findAllById(List.of(400L, 401L)))
            .thenReturn(List.of(onlyBook));

        BookNotFoundException exception = assertThrows(
            BookNotFoundException.class,
            () -> authorService.updateAuthor(authorId, requestDto)
        );

        assertEquals("Books not found with IDs: [401]", exception.getMessage());
        verify(authorRepository).findById(authorId);
        verify(bookRepository).findAllById(List.of(400L, 401L));
        verify(authorRepository, never()).save(any());
    }
}