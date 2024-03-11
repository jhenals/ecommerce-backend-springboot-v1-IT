package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.support.common.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @Autowired
    BookService bookService;

    //CREATE
    @PostMapping
    public ResponseEntity<ApiResponse> addNewBook(@RequestBody Book book){
        bookService.addNewBook(book);
        return new ResponseEntity<>(new ApiResponse(true, "New book has been added"), HttpStatus.CREATED);
    }

    //READ
    @GetMapping
    public List<Book> getAllBooks(){
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long bookId){
        Book book = bookService.getBookById(bookId);
        return book != null ? ResponseEntity.ok(book) : ResponseEntity.notFound().build();
    }

    //UPDATE
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<String> updateBook( @PathVariable long id, @RequestBody Book book) {
        bookService.updateBook(id, book);
        return new ResponseEntity<>("Book with ID " + id + " updated successfully", HttpStatus.OK);
    }

    //DELETE
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public  ResponseEntity<String> deleteBook(@PathVariable long id){
        bookService.deleteBookById(id);
        return new ResponseEntity<>("Book with ID " + id + " deleted successfully", HttpStatus.OK);
    }
}
