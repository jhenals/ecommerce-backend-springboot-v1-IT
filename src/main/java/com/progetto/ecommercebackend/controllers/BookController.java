package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.support.domain.HttpResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @Autowired
    BookService bookService;
    @Autowired
    private BookRepository bookRepository;

    //CREATE
    @PostMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<Book> createNewBook(@RequestBody Book book, @RequestParam List<Long> authorIds) {
        bookService.addNewBook(book, authorIds);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    //READ
   @GetMapping
    public ResponseEntity<HttpResponse> getBooks(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortDirection
    ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("page", bookService.getBooks(
                                page.orElse(0),
                                size.orElse(12),
                                sortBy.orElse("id"),
                                sortDirection.orElse("ASC"))))
                        .message("Books retrieved")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/author")
    public ResponseEntity<List<Book>> getAllBooksOfAuthor(@RequestParam(name = "id") Long authorId){
        List<Book> booksOfAuthor = bookService.getAllBooksOfAuthor(authorId);
        return new ResponseEntity<>(booksOfAuthor, HttpStatus.OK);
    }

    @GetMapping("/book")
    public ResponseEntity<Book> getBookById(@RequestParam(name = "id") Long bookId) {
        Book book= bookService.getBookById(bookId);
        book.setFinalPrice(book.getFinalPrice());
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @GetMapping("/categories")
    public ResponseEntity<HttpResponse> getAllBooksOfCategories(
            @RequestParam(name = "ids") Optional<List<Long>> categoryIds) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("page", bookService.getAllBooksOfCategories(
                                categoryIds.orElse(null))))
                        .message("Libri recuperati")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @GetMapping("/discounted-books")
    public List<Book> getAllDiscountedBooks() {
        return bookService.getAllDiscountedBooks();
    }


    @GetMapping("/best-sellers")
    public List<Book> getBestSellingBooks() {
        return bookService.getBestSellingBooks();
    }


    //UPDATE
    @RequestMapping(value = "", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> updateBook(@RequestParam long id, @RequestBody @Valid Book book) {
         return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("book:",bookService.updateBook(id, book)))
                        .message("Libro aggiornato")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    //DELETE
    @RequestMapping(value = "/book", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> deleteBook(@RequestParam long id) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("page",bookService.deleteBookById(id)))
                        .message("Libro eliminato")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

}
