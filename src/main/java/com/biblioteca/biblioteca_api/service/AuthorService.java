package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.dto.AuthorDto;
import com.biblioteca.biblioteca_api.dto.CreateOrUpdateAuthorRequestDto;
import com.biblioteca.biblioteca_api.exception.AuthorNotFoundException;
import com.biblioteca.biblioteca_api.exception.BookNotFoundException;
import com.biblioteca.biblioteca_api.mapper.AuthorMapper;
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
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<AuthorDto> getAllAuthors() {
        return AuthorMapper.toAuthorDtoList(authorRepository.findAllByOrderByIdAsc());
    }

    public AuthorDto getAuthorDetail(Long id) {
        Author author = authorRepository.findById(id)
            .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " not found"));
        return AuthorMapper.toAuthorDto(author);
    }

    public AuthorDto createAuthor(CreateOrUpdateAuthorRequestDto requestDto) {
        List<Book> books = retrieveBooks(requestDto.getBookIds());
        Author author = new Author();
        author.setName(requestDto.getName());
        author.setBooks(books);
        return AuthorMapper.toAuthorDto(authorRepository.save(author));
    }

    public AuthorDto updateAuthor(Long id, CreateOrUpdateAuthorRequestDto requestDto) {
        Author authorToUpdate = authorRepository.findById(id)
            .orElseThrow(() -> new AuthorNotFoundException("Author with id " + id + " not found"));
        List<Book> books = retrieveBooks(requestDto.getBookIds());
        authorToUpdate.setName(requestDto.getName());
        authorToUpdate.setBooks(books);
        return AuthorMapper.toAuthorDto(authorRepository.save(authorToUpdate));
    }

    private List<Book> retrieveBooks(List<Long> bookIds) {
        List<Book> books = bookRepository.findAllById(bookIds);
        if (books.size() != bookIds.size()) {
            Set<Long> foundIds = books.stream()
                .map(Book::getId)
                .collect(Collectors.toSet());
            List<Long> missing = bookIds.stream()
                .filter(id -> !foundIds.contains(id))
                .collect(Collectors.toList());
            throw new BookNotFoundException("Books not found with IDs: " + missing);
        }
        return books;
    }
}