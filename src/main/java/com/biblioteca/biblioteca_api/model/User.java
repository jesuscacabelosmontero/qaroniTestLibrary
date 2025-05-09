package com.biblioteca.biblioteca_api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LIBRARY_USERS")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @ManyToMany
    @JoinTable(
        name = "USER_ROLES",
        joinColumns = @JoinColumn( name = "user_id", referencedColumnName = "id" ),
        inverseJoinColumns = @JoinColumn( name = "role_id", referencedColumnName = "id")
    )
    private List<Role> roles;
}
