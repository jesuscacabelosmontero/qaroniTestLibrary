package com.biblioteca.biblioteca_api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.biblioteca.biblioteca_api.model.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{

    Optional<Role> findByRole(String role);
}
