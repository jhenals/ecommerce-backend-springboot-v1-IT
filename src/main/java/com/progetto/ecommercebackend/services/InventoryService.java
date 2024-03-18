package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InventoryService {

    @Autowired
    BookRepository bookRepository;

    public void updateBookQuantityInInventory(Long bookId, Integer quantity){
        Book book = bookRepository.findBookById(bookId);
        int currentQuantity = book.getQuantity();
        book.setQuantity(currentQuantity + quantity);
        bookRepository.save(book);
    }

    public Integer getCurrentQuantity(Long bookId){
        Book book = bookRepository.findBookById(bookId);
        return book.getQuantity();
    }

    public void incrementNumPurchases(Long bookId) {
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isPresent()) {
            book.get().setNumPurchases(book.get().getNumPurchases()+1);
            bookRepository.save(book.get());
        }else{
            throw new CustomException("Book not found.");
        }
    }
}
