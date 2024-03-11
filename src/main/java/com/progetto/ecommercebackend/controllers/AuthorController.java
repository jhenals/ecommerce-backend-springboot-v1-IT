package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.services.AuthorService;
import com.progetto.ecommercebackend.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.PathParam;
import java.util.List;

@RestController
@RequestMapping("/api/v1/authors")
public class AuthorController {

    @Autowired
    AuthorService authorService;

    @Autowired
    BookService bookService;

    @GetMapping
    public List<Author> getAllAuthors(){
        return authorService.getAllAuthors();
    }

    @PostMapping
    public ResponseEntity<String> addNewAuthor(@RequestBody String author){
        authorService.addNewAuthor(author);
        return  new ResponseEntity<>(author + " is added as new author.", HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{authorId}", method= RequestMethod.PUT)
    public ResponseEntity<String> updateAuthorName(@PathVariable("authorId") Long authorId, @RequestBody String author){
        authorService.updateAuthorName(author);
        return new ResponseEntity<>(author + "'s name is updated.", HttpStatus.OK);
    }


    @RequestMapping(value = "/{authorId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAuthor(@PathVariable("authorId") Long authorId){
        authorService.deleteAuthor(authorId);
        return new ResponseEntity<>("Author with id "+ authorId + " is deleted.", HttpStatus.OK);
    }



}
