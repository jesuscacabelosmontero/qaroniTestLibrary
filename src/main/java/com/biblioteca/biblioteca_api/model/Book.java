package com.biblioteca.biblioteca_api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "BOOKS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String title;

    @ManyToMany
    @JoinTable(
        name = "BOOK_AUTHORS",
        joinColumns = @JoinColumn( name = "book_id", referencedColumnName = "id" ),
        inverseJoinColumns = @JoinColumn( name = "author_id", referencedColumnName = "id")
    )
    private List<Author> authors;
}