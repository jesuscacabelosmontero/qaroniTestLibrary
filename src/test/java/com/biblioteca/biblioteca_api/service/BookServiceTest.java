package com.biblioteca.biblioteca_api.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.biblioteca.biblioteca_api.dto.BookDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateBookRequestDto;
import com.biblioteca.biblioteca_api.exception.AuthorNotFoundException;
import com.biblioteca.biblioteca_api.exception.BookNotFoundException;
import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.model.Book;
import com.biblioteca.biblioteca_api.repository.AuthorRepository;
import com.biblioteca.biblioteca_api.repository.BookRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;
    
    
    @Test
    void testGetAllBooks_returnsBookDtoList() {
        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Gabriel García Márquez");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Miguel de Cervantes");
    

        Book book1 = new Book();
        book1.setId(1L);
        book1.setTitle("El Quijote");
        book1.setAuthors(Collections.singletonList(author2));

        Book book2 = new Book();
        book2.setId(2L);
        book2.setTitle("Cien años de soledad");
        book2.setAuthors(Collections.singletonList(author1));

        List<Book> books = List.of(book1, book2);

        when(bookRepository.findAllByOrderByIdAsc()).thenReturn(books);

        List<BookDto> result = bookService.getAllBooks();

        assertNotNull(result);
        assertEquals(2, result.size());
        BookDto resultBook1 = result.get(0);
        assertEquals(1L, resultBook1.getId());
        assertEquals("El Quijote", resultBook1.getTitle());
        assertEquals(1, resultBook1.getAuthorNames().size());
        assertEquals("Miguel de Cervantes", resultBook1.getAuthorNames().get(0));
        BookDto resultBook2 = result.get(1);
        assertEquals(2L, resultBook2.getId());
        assertEquals("Cien años de soledad", resultBook2.getTitle());
        assertEquals(1, resultBook2.getAuthorNames().size());
        assertEquals("Gabriel García Márquez", resultBook2.getAuthorNames().get(0));
        verify(bookRepository).findAllByOrderByIdAsc();
    }

    @Test
    void testGetBookDetail_whenBookExists_returnsBookDto() {
        Long bookId = 1L;
        Book book = new Book();
        book.setId(bookId);
        book.setTitle("Don Quijote de la Mancha");

        Author author = new Author();
        author.setName("Miguel de Cervantes");
        book.setAuthors(List.of(author));

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        BookDto result = bookService.getBookDetail(bookId);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("Don Quijote de la Mancha", result.getTitle());
        assertEquals(1, result.getAuthorNames().size());
        assertEquals("Miguel de Cervantes", result.getAuthorNames().get(0));
        verify(bookRepository).findById(bookId);
    }

    @Test
    void testGetBookDetail_whenBookNotFound_throwsBookNotFoundException() {
        // Arrange
        Long bookId = 99L;
        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        // Act & Assert
        BookNotFoundException exception = assertThrows(
            BookNotFoundException.class,
            () -> bookService.getBookDetail(bookId)
        );
        assertEquals("Book with id 99 not found", exception.getMessage());
        verify(bookRepository).findById(bookId);
    }

    @Test
    void testCreateBook_whenValidBookRequest_createsBookAndReturnsBookDto() {
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto();
        requestDto.setTitle("El amor en los tiempos del cólera");
        requestDto.setAuthorIds(List.of(1L, 2L));

        Author author1 = new Author();
        author1.setId(1L);
        author1.setName("Gabriel García Márquez");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Otro Autor");

        List<Author> authors = List.of(author1, author2);

        Book newBook = new Book();
        newBook.setTitle(requestDto.getTitle());
        newBook.setAuthors(authors);

        when(authorRepository.findAllById(requestDto.getAuthorIds())).thenReturn(authors);
        when(bookRepository.save(any(Book.class))).thenReturn(newBook);

        BookDto result = bookService.createBook(requestDto);

        assertNotNull(result);
        assertEquals("El amor en los tiempos del cólera", result.getTitle());
        assertEquals(2, result.getAuthorNames().size());
        assertTrue(result.getAuthorNames().contains("Gabriel García Márquez"));
        assertTrue(result.getAuthorNames().contains("Otro Autor"));
        verify(authorRepository).findAllById(requestDto.getAuthorIds());
        verify(bookRepository).save(any());
    }

    @Test
    void testCreateBook_whenAuthorsNotFound_throwsAuthorNotFoundException() {
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto();
        requestDto.setTitle("El otoño del patriarca");
        requestDto.setAuthorIds(List.of(1L, 99L));

        Author author = new Author();
        author.setId(1L);
        author.setName("Gabriel García Márquez");

        when(authorRepository.findAllById(requestDto.getAuthorIds())).thenReturn(List.of(author));

        AuthorNotFoundException exception = assertThrows(
            AuthorNotFoundException.class,
            () -> bookService.createBook(requestDto)
        );
        assertEquals("Authors not found with IDs: [99]", exception.getMessage());
        verify(authorRepository).findAllById(requestDto.getAuthorIds());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testUpdateBook_whenValidRequest_updatesBookAndReturnsBookDto() {
        Long bookId = 1L;
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto();
        requestDto.setTitle("La hojarasca");
        requestDto.setAuthorIds(List.of(2L, 3L));

        Book bookToUpdate = new Book();
        bookToUpdate.setId(bookId);
        bookToUpdate.setTitle("Nombre viejo");

        Author author2 = new Author();
        author2.setId(2L);
        author2.setName("Autor 2");

        Author author3 = new Author();
        author3.setId(3L);
        author3.setName("Autor 3");

        List<Author> authors = List.of(author2, author3);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(bookToUpdate));
        when(authorRepository.findAllById(requestDto.getAuthorIds())).thenReturn(authors);
        when(bookRepository.save(bookToUpdate)).thenReturn(bookToUpdate);

        BookDto result = bookService.updateBook(bookId, requestDto);

        assertNotNull(result);
        assertEquals(bookId, result.getId());
        assertEquals("La hojarasca", result.getTitle());
        assertEquals(2, result.getAuthorNames().size());
        assertTrue(result.getAuthorNames().contains("Autor 2"));
        assertTrue(result.getAuthorNames().contains("Autor 3"));
        verify(bookRepository).findById(bookId);
        verify(authorRepository).findAllById(requestDto.getAuthorIds());
        verify(bookRepository).save(bookToUpdate);
    }

    @Test
    void testUpdateBook_whenBookNotFound_throwsBookNotFoundException() {
        Long bookId = 99L;
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto();
        requestDto.setTitle("El otoño del patriarca");
        requestDto.setAuthorIds(List.of(1L, 2L));

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        BookNotFoundException exception = assertThrows(
            BookNotFoundException.class,
            () -> bookService.updateBook(bookId, requestDto)
        );
        assertEquals("Book with id 99 not found", exception.getMessage());
        verify(bookRepository).findById(bookId);
        verify(authorRepository, never()).findAllById(any());
        verify(bookRepository, never()).save(any());
    }

    @Test
    void testUpdateBook_whenAuthorsNotFound_throwsAuthorNotFoundException() {
        Long bookId = 1L;
        CreateOrUpdateBookRequestDto requestDto = new CreateOrUpdateBookRequestDto();
        requestDto.setTitle("El otoño del patriarca");
        requestDto.setAuthorIds(List.of(99L, 100L));

        Book existingBook = new Book();
        existingBook.setId(bookId);
        existingBook.setTitle("Old Title");
        existingBook.setAuthors(new ArrayList<>());
        
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(existingBook));
        when(authorRepository.findAllById(List.of(99L, 100L))).thenReturn(new ArrayList<>());
        
        AuthorNotFoundException exception = assertThrows(
            AuthorNotFoundException.class, 
            () -> bookService.updateBook(bookId, requestDto)
        );

        assertEquals("Authors not found with IDs: [99, 100]", exception.getMessage());
        verify(authorRepository).findAllById(List.of(99L, 100L));
        verify(bookRepository, never()).save(any());
    }
}
