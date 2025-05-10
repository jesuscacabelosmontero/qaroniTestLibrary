package com.biblioteca.biblioteca_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.biblioteca_api.model.Book;

public interface BookRepository extends JpaRepository<Book, Long>{
    List<Book> findAllByOrderByIdAsc();
}
