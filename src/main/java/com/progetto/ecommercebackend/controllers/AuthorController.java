package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.AuthorService;
import com.progetto.ecommercebackend.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    BookService bookService;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> createNewAuthor(@RequestBody String authorName){
        authorService.addNewAuthor(authorName);
        return  new ResponseEntity<>(authorName + " is added as new author.", HttpStatus.CREATED);
    }

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

    @RequestMapping(value = "/{authorId}", method= RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateAuthorName(@PathVariable("authorId") Long authorId, @RequestBody String author){
        authorService.updateAuthorName(authorId, author);
        return new ResponseEntity<>(author + "'s name is updated.", HttpStatus.OK);
    }


    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> deleteAuthor(@PathVariable("authorId") Long authorId){
        authorService.deleteAuthor(authorId);
        return new ResponseEntity<>("Author with id "+ authorId + " is deleted.", HttpStatus.OK);
    }



}
