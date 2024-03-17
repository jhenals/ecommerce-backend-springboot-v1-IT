package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.support.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    //CREATE
    @PostMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<Book> createNewBook(@RequestBody Book book, @RequestParam List<Long> authorIds) {
        bookService.addNewBook(book, authorIds);
        return new ResponseEntity<>(book, HttpStatus.CREATED);
    }

    //READ
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> bookList = bookService.getAllBooks();
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }

    @GetMapping("/author")
    public ResponseEntity<List<Book>> getAllBooksOfAuthor(@RequestParam(name = "id") Long authorId){
        List<Book> booksOfAuthor = bookService.getAllBooksOfAuthor(authorId);
        return new ResponseEntity<>(booksOfAuthor, HttpStatus.OK);
    }

    @GetMapping("/discounted-books")
    public List<Book> getAllDiscountedBooks() {
        return bookService.getAllDiscountedBooks();
    }


    @GetMapping("/best-sellers")
    public List<Book> getBestSellingBooks() {
        return bookService.getBestSellingBooks();
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Book>> getBooksByCategoryId(@RequestParam Long categoryId) {
        List<Book> books = bookService.getBooksByCategoryId(categoryId);
        return ResponseEntity.ok(books);
    }

    //UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateBook(@PathVariable long id, @RequestBody Book book) {
        bookService.updateBook(id, book);
        return new ResponseEntity<>("Book with ID " + id + " updated successfully", HttpStatus.OK);
    }



    @RequestMapping(value = ("inventory-quantity"), method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateBookQuantityInInventory(@RequestParam Long bookId, @RequestParam Integer qty){
        bookService.updateBookQuantityInInventory(bookId, qty);
        return new ResponseEntity<>("Book quantity is updated", HttpStatus.OK);
    }

    @RequestMapping(value = ("num-purchases"), method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_user')")
    public ResponseEntity<String> incrementNumPurchases(@RequestParam Long bookId){
        bookService.incrementNumPurchases(bookId);
        return new ResponseEntity<>("Number of purchase is updated", HttpStatus.OK);
    }

    //DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> deleteBook(@PathVariable long id) {
        bookService.deleteBookById(id);
        return new ResponseEntity<>("Book with ID " + id + " deleted successfully", HttpStatus.OK);
    }

}
