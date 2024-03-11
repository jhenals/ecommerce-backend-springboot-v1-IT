package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.repositories.InventoryRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    InventoryRepository inventoryRepository;

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
            if (inventoryRepository.findById(id).get().getQuantity() == 0) {
                bookRepository.deleteById(id);
            } else {
                throw new CustomException("Book can not be deleted because it is not empty in inventory.");
            }
        }else{
            throw new CustomException("Book is not found");
        }
    }

}
