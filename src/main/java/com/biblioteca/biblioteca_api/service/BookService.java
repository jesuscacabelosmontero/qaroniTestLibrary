package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.dto.BookDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateBookRequestDto;
import com.biblioteca.biblioteca_api.exception.AuthorNotFoundException;
import com.biblioteca.biblioteca_api.exception.BookNotFoundException;
import com.biblioteca.biblioteca_api.mapper.BookMapper;
import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.model.Book;
import com.biblioteca.biblioteca_api.repository.AuthorRepository;
import com.biblioteca.biblioteca_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    public List<BookDto> getAllBooks() {
        return BookMapper.toBookDtoList(bookRepository.findAllByOrderByIdAsc());
    }

    public BookDto getBookDetail(Long id) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
        return BookMapper.toBookDto(book);
    }

    public BookDto createBook(CreateOrUpdateBookRequestDto bookRequest) {
        Book book = new Book();
        List<Author> authors = retrieveAuthors(bookRequest.getAuthorIds());
        book.setTitle(bookRequest.getTitle());
        book.setAuthors(authors);
        return BookMapper.toBookDto(bookRepository.save(book));
    }

    public BookDto updateBook(Long id, CreateOrUpdateBookRequestDto updatedBook) {
        Book bookToUpdate = bookRepository.findById(id)
            .orElseThrow(() -> new BookNotFoundException("Book with id " + id + " not found"));
        List<Author> authors = retrieveAuthors(updatedBook.getAuthorIds());
        bookToUpdate.setTitle(updatedBook.getTitle());
        bookToUpdate.setAuthors(authors);
        return BookMapper.toBookDto(bookRepository.save(bookToUpdate));
    }

    private List<Author> retrieveAuthors (List<Long> authorsIds) {
        List<Author> authors = authorRepository.findAllById(authorsIds);
        if (authors.size() != authorsIds.size()) {
            Set<Long> foundIds = authors.stream()
                .map(Author::getId)
                .collect(Collectors.toSet());
            List<Long> missingIds = authorsIds.stream()
                .filter(idToCheck -> !foundIds.contains(idToCheck))
                .collect(Collectors.toList());
            throw new AuthorNotFoundException("Authors not found with IDs: " + missingIds);
        }
        return authors;
    }
}