package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    @Query("SELECT b FROM Book b WHERE b.id = :bookId")
    Book findBookById(@Param("bookId") long bookId);

    @Query("SELECT b FROM Book b WHERE b.discount!=null")
    List<Book> findAllWithDiscount();

    @Query("SELECT b FROM Book b WHERE b.numPurchases != null")
    List<Book> findAllByNumPurchasesIsNotNull();

    @Query("SELECT b FROM Book b WHERE b.category.id = :categoryId")
    List<Book> findAllBooksByCategoryId(@Param("categoryId") Long categoryId);


}