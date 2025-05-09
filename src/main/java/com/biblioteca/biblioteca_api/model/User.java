package com.biblioteca.biblioteca_api.model;

import java.util.List;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "library_user")
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
        name = "user_role",
        joinColumns = @JoinColumn( name = "USER_ID", referencedColumnName = "id" ),
        inverseJoinColumns = @JoinColumn( name = "ROLE_ID", referencedColumnName = "id")
    )
    private List<Role> roles;
}
