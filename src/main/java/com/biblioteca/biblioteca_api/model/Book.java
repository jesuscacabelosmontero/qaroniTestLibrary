package com.biblioteca.biblioteca_api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
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
        name = "book_author",
        joinColumns = @JoinColumn( name = "BOOK_ID", referencedColumnName = "id" ),
        inverseJoinColumns = @JoinColumn( name = "AUTHOR_ID", referencedColumnName = "id")
    )
    private List<Author> authors;
}