package com.biblioteca.biblioteca_api.service;

import com.biblioteca.biblioteca_api.model.Author;
import com.biblioteca.biblioteca_api.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author getAuthorDetail(Long id) {
        Optional<Author> author = authorRepository.findById(id);
        return author.orElse(null);
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthor(Long id, Author updatedAuthor) {
        Optional<Author> existingAuthor = authorRepository.findById(id);

        if (existingAuthor.isPresent()) {
            Author authorToUpdate = existingAuthor.get();
            return authorRepository.save(authorToUpdate);
        }

        return null;
    }
}