package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.model.Book;
import com.biblioteca.biblioteca_api.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookDetail(Long id) {
        Optional<Book> book = bookRepository.findById(id);
        return book.orElse(null);
    }

    public Book createBook(Book book) {
        return bookRepository.save(book); 
    }

    public Book updateBook(Long id, Book updatedBook) {
        Optional<Book> existingBook = bookRepository.findById(id);
        if (existingBook.isPresent()) {
            Book bookToUpdate = existingBook.get();
            bookToUpdate.setTitle(updatedBook.getTitle());
            bookToUpdate.setAuthors(updatedBook.getAuthors());
            return bookRepository.save(bookToUpdate);
        }
        return null;
    }
}