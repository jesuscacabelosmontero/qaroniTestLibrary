package com.biblioteca.biblioteca_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.biblioteca_api.model.Author;

public interface AuthorRepository extends JpaRepository<Author, Long>{
    List<Author> findAllByOrderByIdAsc();
}
