package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.entities.OrderBook;
import com.progetto.ecommercebackend.services.BookService;
import com.progetto.ecommercebackend.services.OrderBookService;
import com.progetto.ecommercebackend.services.OrderService;
import com.progetto.ecommercebackend.support.common.OrderForm;
import com.progetto.ecommercebackend.support.domain.HttpResponse;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import com.progetto.ecommercebackend.support.enums.OrderStatusDTO;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import com.progetto.ecommercebackend.support.exceptions.ResourceNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    BookService bookService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderBookService orderBookService;

    //CREATE
    @PostMapping("/{userId}")
    public ResponseEntity addBookToCart(@Valid @RequestBody Book book, @PathVariable("userId") String userId ) {
        try {
            return new ResponseEntity<>(orderService.addBookToCart(book, userId), HttpStatus.OK);
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Libro non può essere aggiunto al carrello!", e);
        }
    }


    @RequestMapping(value ="/{userId}/checkout", method = RequestMethod.POST)
    public ResponseEntity checkout(@PathVariable("userId") String userId,
                                   @RequestBody OrderForm orderform) {
        try{
            return new ResponseEntity<>( orderService.checkout(userId, orderform), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out non è andato a buon fine!", e);
        }
    }


    //READ
    @GetMapping
    @PreAuthorize("hasRole('ROLE_admin')")
    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }


    @GetMapping("/user")
    public List<Order> getAllOrdersOfUser(@RequestParam(name = "id") String userId){
        return orderService.getAllOrdersOfUser(userId);
    }


    @GetMapping("/pending-cart")
    public Order getPendingCart(@RequestParam String userId){
        return orderService.getPendingCart(userId);
    }


    @GetMapping("/{userId}/cart-items")
    public List<OrderBook> getItemsInPendingCart(@PathVariable("userId") String userId) {
        Order pendingCart = getPendingCart(userId);
        return orderBookService.getItemsInPendingCart(pendingCart.getId());
    }


    //UPDATE
    @RequestMapping(value = "/order", method = RequestMethod.PUT)
    public ResponseEntity updateOrderStatus( @RequestParam(name = "id") Long orderId, @Valid @RequestBody OrderStatusDTO orderStatus) {
        try{
            return new ResponseEntity<>( orderService.updateOrderStatus(orderId, orderStatus), HttpStatus.OK);
        }  catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Stato dell'ordine non aggiornato!", e);
        }
    }

    @RequestMapping(value = "/{userId}/incr-quantity/book", method = RequestMethod.PUT)
    public ResponseEntity increaseBookQtyInCart(@PathVariable("userId") String userId, @RequestParam(name = "id") Long bookId)  {
        try{
            return new ResponseEntity<>( orderService.increaseBookQtyInCart(userId, bookId), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore nell'aggiunta della quantità del libro nel carrello.", e);
        }

    }

    @RequestMapping(value = "/{userId}/decr-quantity", method = RequestMethod.PUT)
    public ResponseEntity decreaseBookQtyInCart(@PathVariable("userId") String userId, @Valid @RequestBody Book book) {
        try{
            return new ResponseEntity<>( orderService.removeBookFromCart(book,userId), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Errore nella rimozione dei libri dal carrello.", e);
        }
    }


    //DELETE
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity removeBookFromCart(@Valid @RequestBody  Book book,@PathVariable("userId") String userId ) {
        try{
            return new ResponseEntity<>( orderService.removeBookFromCart(book, userId), HttpStatus.OK);
        }  catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,  "Errore nella rimozione dei libri dal carrello.", e);
        }
    }


    @RequestMapping(value = "/{userId}/reset", method = RequestMethod.DELETE)
    public ResponseEntity resetCart(@PathVariable("userId") String userId){
        Order pendingCart = getPendingCart(userId);
        pendingCart= orderService.resetCart(userId, pendingCart.getId());
        return new ResponseEntity<>(pendingCart, HttpStatus.OK);
    }


}
