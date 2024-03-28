package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@Service
public class InventoryService {

    @Autowired
    BookRepository bookRepository;

    public Book updateBookQuantityInInventory(Long bookId, Integer quantity){
        try{
            Book book = bookRepository.findBookById(bookId);
            int currentQuantity = book.getQuantity();
            book.setQuantity(currentQuantity + quantity);
            return bookRepository.save(book);
        }catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomException("Operazione fallita. Il libro è stato modificato o aggiornato da un altro utente. Si prega di riprovare.");
        }

    }

    public Integer getCurrentQuantity(Long bookId){
        Book book = bookRepository.findBookById(bookId);
        return book.getQuantity();
    }

    public Book incrementNumPurchases(Long bookId) {
        try{
            Optional<Book> book = bookRepository.findById(bookId);
            if(book.isPresent()) {
                book.get().setNumPurchases(book.get().getNumPurchases()+1);
                return bookRepository.save(book.get());
            }else{
                throw new CustomException("Libro non trovato.");
            }
        }catch (ObjectOptimisticLockingFailureException e) {
            throw new CustomException("Operazione fallita. Il libro è stato modificato o aggiornato da un altro utente. Si prega di riprovare.");
        }

    }
}
