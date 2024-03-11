package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Book findBookById(@Param("bookId") long bookId);
}