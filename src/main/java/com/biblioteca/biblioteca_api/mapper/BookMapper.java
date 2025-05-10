package com.biblioteca.biblioteca_api.mapper;

import java.util.List;
import java.util.stream.Collectors;

import com.biblioteca.biblioteca_api.dto.BookDto;
import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.model.Book;

public class BookMapper {

    public static BookDto toBookDto(Book book) {
        List<String> authorNames = book.getAuthors().stream()
            .map(Author::getName)
            .collect(Collectors.toList());
        return new BookDto(book.getId(), book.getTitle(), authorNames);
    }

    public static List<BookDto> toBookDtoList(List<Book> books) {
        return books.stream()
            .map(BookMapper::toBookDto)
            .collect(Collectors.toList());
    }
}