package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.BookAuthor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookAuthorRepository extends JpaRepository<BookAuthor, Long> {

   @Query("SELECT ba FROM BookAuthor  ba WHERE ba.author.id = :authorId")
    List<BookAuthor> findAllBooksByAuthorId(@Param("authorId") Long authorId);
}