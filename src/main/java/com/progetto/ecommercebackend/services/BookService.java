package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.entities.BookAuthor;
import com.progetto.ecommercebackend.repositories.AuthorRepository;
import com.progetto.ecommercebackend.repositories.BookAuthorRepository;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;
    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    public void addNewBook(Book book) {
        bookRepository.save(book);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book getBookById(Long bookId) {
        return bookRepository.findBookById(bookId);
    }

    public void updateBook(long id, Book book) {
        Optional<Book> bookOptional = Optional.ofNullable(bookRepository.findBookById(id));
        if(bookOptional.isPresent()) {
            Book newBook = book;
            bookRepository.save(newBook);
        }else{
            throw  new CustomException("Book is not found.");
        }
    }

    public void deleteBookById(long id) {
        Optional<Book> bookOptional = Optional.ofNullable(bookRepository.findBookById(id));
        if( bookOptional.isPresent() ){
            if (bookOptional.get().getQuantity() == 0) {
                bookRepository.deleteById(id);
            } else {
                throw new CustomException("Book can not be deleted because it is not empty in inventory.");
            }
        }else{
            throw new CustomException("Book is not found");
        }
    }

    public List<Book> getAllDiscountedBooks() {
        return bookRepository.findAllWithDiscount();
    }


    public List<Book> getBestSellingBooks() {
        List<Book> bestSellingBooks = bookRepository.findAllByNumPurchasesIsNotNull();
        bestSellingBooks.sort(Comparator.comparingInt(Book::getNumPurchases).reversed());
        bestSellingBooks.subList(0,9); //Prendo solo i primi 10
        return bestSellingBooks;
    }


    public List<Book> getBooksByAuthorId(Long authorId) {
       return bookAuthorRepository.findAllByAuthorId(authorId);
    }

    public List<Book> getBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllBooksByCategoryId(categoryId);
    }

    public void updateBookQuantityInInventory(Long bookId, Integer qty) {
        Book book = bookRepository.findBookById(bookId);
        book.setQuantity(qty);
        bookRepository.save(book);
    }
}
