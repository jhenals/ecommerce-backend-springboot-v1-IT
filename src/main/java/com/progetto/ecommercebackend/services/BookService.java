package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Author;
import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.repositories.AuthorRepository;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    AuthorRepository authorRepository;

    public Book addNewBook( @NotNull Book book, @NotNull List<Long> authorIds) {
        Set<Author> authors = new HashSet<>();
        for ( Long authorId : authorIds ){
            Optional<Author> authorOptional = authorRepository.findById(authorId);
            if (authorOptional.isPresent() ) {
                Author author = authorOptional.get();
                author.getBooks().add(book);
                authorRepository.save(author);
            }
        }
        book.setAuthors(authors);
        Book savedBook = bookRepository.save(book);
        return savedBook;
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public List<Book> getAllBooksOfAuthor(Long authorId) {
        Optional<Author> authorOptional =  authorRepository.findById(authorId);
        if( authorOptional.isPresent() ){
            return authorOptional.get().getBooks().stream().toList();
        }else{
            throw new CustomException("Error in getting books written by author with id" + authorId);
        }
    }


    /*
    @Autowired
    private BookAuthorRepository bookAuthorRepository;

    public BookAuthor addNewBook(Book book, Author author) {
        bookRepository.save(book);
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        return bookAuthorRepository.save(bookAuthor);
    }


    public List<BookAuthor> getAllBookAuthors() {
        List<BookAuthor> bookAuthorList=  bookAuthorRepository.findAll();
        return new ArrayList<>(bookAuthorList);

    }

    public Book getBookById(Long bookId) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);
        if( bookOptional.isPresent() ){
            return bookOptional.get();
        }else{
            throw new CustomException("Book not found.");
        }
    }

    public void updateBook(long id, Book book) {
        Optional<Book> bookOptional = Optional.ofNullable(bookRepository.findBookById(id));
        if(bookOptional.isPresent()) {
            Book newBook = bookOptional.get();
            newBook = book;
            bookRepository.save(newBook);
        }else{
            throw  new CustomException("Book is not found");
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
        if(bestSellingBooks.size()>10){
            bestSellingBooks.subList(0,9); //Prendo solo i primi 1
        }

        return bestSellingBooks;
    }


    public List<Book> getBooksByAuthorId(Long authorId) {
       List<BookAuthor> bookAuthorList =  bookAuthorRepository.findAllByAuthorId(authorId);
       List<Book> bookList= new ArrayList<>();
       for( BookAuthor ba : bookAuthorList ){
           bookList.add(ba.getBook());
       }
       return bookList;
    }

    public List<Book> getBooksByCategoryId(Long categoryId) {
        return bookRepository.findAllBooksByCategoryId(categoryId);
    }

    public void updateBookQuantityInInventory(Long bookId, Integer qty) {
        Optional<Book> book = bookRepository.findById(bookId);
        if(book.isPresent()) {
            book.get().setQuantity(qty);
            bookRepository.save(book.get());
        }else{
            throw new CustomException("Book not found.");
        }
    }


    public BookAuthor getBookAuthorByBook(Book book) {
        return bookAuthorRepository.findByBookId(book.getId());
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

     */
}
