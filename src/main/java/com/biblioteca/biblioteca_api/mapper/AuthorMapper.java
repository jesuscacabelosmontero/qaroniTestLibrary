package com.biblioteca.biblioteca_api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.biblioteca_api.dto.AuthorDto;
import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.model.Book;

public class AuthorMapper {

    public static AuthorDto toAuthorDto(Author author) {
        List<String> bookTitles = author.getBooks().stream()
            .map(Book::getTitle)
            .collect(Collectors.toList());
        return new AuthorDto(author.getId(), author.getName(), bookTitles);
    }

    public static List<AuthorDto> toAuthorDtoList(List<Author> authors) {
        return authors.stream()
            .map(AuthorMapper::toAuthorDto)
            .collect(Collectors.toList());
    }
}