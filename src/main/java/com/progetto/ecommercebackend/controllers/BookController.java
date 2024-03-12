package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.support.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    //CREATE
    @PostMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<ApiResponse> addNewBook(@RequestBody Book book) {
        bookService.addNewBook(book);
        return new ResponseEntity<>(new ApiResponse(true, "New book has been added"), HttpStatus.CREATED);
    }

    //READ
    @GetMapping
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId) {
        Book book = bookService.getBookById(bookId);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
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

    @GetMapping("/discounted-books")
    public List<Book> getAllDiscountedBooks() {
        return bookService.getAllDiscountedBooks();
    }


    @GetMapping("/best-sellers")
    public List<Book> getBestSellingBooks() {
        return bookService.getBestSellingBooks();
    }

    @GetMapping("/authors")
    public ResponseEntity<List<Book>> getBooksByAuthorId(@RequestParam Long authorId) {
        List<Book> books = bookService.getBooksByAuthorId(authorId);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Book>> getBooksByCategoryId(@RequestParam Long categoryId) {
        List<Book> books = bookService.getBooksByCategoryId(categoryId);
        return ResponseEntity.ok(books);
    }


    @RequestMapping(value = ("inventory-quantity"), method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateBookQuantityInInventory(@RequestParam Long bookId, @RequestBody Integer qty){
        bookService.updateBookQuantityInInventory(bookId, qty);
        return new ResponseEntity<>("Book quantity is updated", HttpStatus.OK);
    }
}
