package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Long> {
}