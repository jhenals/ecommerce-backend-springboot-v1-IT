package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.AuthorRepository;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class AuthorService {

    @Autowired
    AuthorRepository authorRepository;
    @Autowired
    private BookRepository bookRepository;

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author addNewAuthor(String author) {
        Author newAuthor = new Author();
        newAuthor.setName(author);
        newAuthor.setBooks(new HashSet<>());
        return authorRepository.save(newAuthor);
    }

    public Author updateAuthorName(Long id, String authorName) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if( authorOptional.isEmpty() ){
            throw new CustomException("L'autore con ID "+ id + " è stato modificato.");
        }
        Author author = authorOptional.get();
        author.setName(authorName);
        return authorRepository.save(author);
    }

    public String deleteAuthor(Long authorId) {
        try{
            authorRepository.deleteById(authorId);
            return "Autore eliminato.";
        }catch (CustomException e){
            throw new CustomException("Cancellazione dell'autore nel database non riuscita.");
        }

    }

    public Author getAuthorById(Long authorId) {
        Optional<Author> authorOptional = authorRepository.findById(authorId);
        if( authorOptional.isPresent() ){
            return authorOptional.get();
        }else{
            throw new CustomException("L'autore non esiste nel database.");
        }
    }

    public Set<Author> getAllAuthorsOfBook(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if( bookOptional.isPresent() ){
            return bookOptional.get().getAuthors();
        }else{
            throw new CustomException("Errore nel recupero dell'autore/i del libro.");
        }
    }
}
