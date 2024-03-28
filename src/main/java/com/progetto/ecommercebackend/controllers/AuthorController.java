package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.AuthorService;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.support.domain.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    BookService bookService;

    //CREATE
    @PostMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> createNewAuthor(@RequestBody String authorName){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("author", authorService.addNewAuthor(authorName)))
                        .message("Un nuovo autore è stato creato.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    //READ
    @GetMapping
    public List<Author> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @GetMapping("/book")
    public Set<Author> getAllAuthorsOfBook(@RequestParam(name = "id") Long bookId){
        return authorService.getAllAuthorsOfBook(bookId);
    }

    @GetMapping("/{authorId}")
    public Author getAuthorById(@PathVariable("authorId") Long authorId ){
        return authorService.getAuthorById(authorId);
    }

    //UPDATE
    @RequestMapping(value = "/{authorId}", method= RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public  ResponseEntity<HttpResponse> updateAuthorName(@PathVariable("authorId") Long authorId, @RequestBody String author){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("author", authorService.updateAuthorName(authorId, author)))
                        .message("Autore aggiornato.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    //DELETE
    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<HttpResponse> deleteAuthor(@PathVariable("authorId") Long authorId){
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("author",  authorService.deleteAuthor(authorId)))
                        .message("Autore eliminato.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }



}
