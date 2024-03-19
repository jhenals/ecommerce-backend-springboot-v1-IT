package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.support.common.ApiResponse;
import com.progetto.ecommercebackend.support.domain.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

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

    @PostMapping("/{bookId}/authors")
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> assignBookToAuthors(@PathVariable("bookId") Long bookId, @RequestParam(name = "id") List<Long> authorIds) {
        bookService.assignBookToAuthors(bookId, authorIds);
        return new ResponseEntity<>("Book with id=" + bookId + " is assigned to author(s)=" + authorIds , HttpStatus.CREATED);
    }

    //READ
    @GetMapping
    public ResponseEntity<HttpResponse> getBooks(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<String> sortDirection
            ){
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
            @RequestParam(name = "ids") Optional<List<Long>> categoryIds,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("page", bookService.getAllBooksOfCategories(
                                categoryIds.orElse(null),
                                page.orElse(0),
                                size.orElse(12))))
                        .message("Books retrieved")
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
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateBook(@PathVariable long id, @RequestBody Book book) {
        bookService.updateBook(id, book);
        return new ResponseEntity<>("Book with ID " + id + " updated successfully", HttpStatus.OK);
    }


    //DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>("Book with ID " + id + " deleted successfully", HttpStatus.OK);
    }

}
