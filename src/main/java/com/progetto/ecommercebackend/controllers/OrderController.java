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
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    BookService bookService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderBookService orderBookService;

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


    @PostMapping("/{userId}")
    public ResponseEntity addBookToCart(@Valid @RequestBody Book book, @PathVariable("userId") String userId ) {
        try {
            return new ResponseEntity<>(orderService.addBookToCart(book, userId), HttpStatus.OK);
        } catch (CustomException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book can not be added to cart!", e);
        }
    }


    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity removeBookFromCart(@Valid @RequestBody  Book book,@PathVariable("userId") String userId ) {
        try{
            return new ResponseEntity<>( orderService.removeBookFromCart(book, userId), HttpStatus.OK);
        }  catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book can not be removed from cart!", e);
        }
    }

    @RequestMapping(value = "/order", method = RequestMethod.PUT , consumes = {"application/json"})
    public ResponseEntity updateOrderStatus( @RequestParam(name = "id") Long orderId, @Valid @RequestBody Order order) {
        try{
            return new ResponseEntity<>( orderService.updateOrderStatus(orderId, order), HttpStatus.OK);
        }  catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order not updated!", e);
        }
    }

    @RequestMapping(value = "/{userId}/incr-quantity/book", method = RequestMethod.PUT, consumes = {"application/json"})
    public ResponseEntity increaseBookQtyInCart(@PathVariable("userId") String userId, @RequestParam(name = "id") Long bookId)  {
        try{
            return new ResponseEntity<>( orderService.increaseBookQtyInCart(userId, bookId), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book quantity can not be increased!", e);
        }

    }

    @RequestMapping(value = "/{userId}/decr-quantity", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity decreaseBookQtyInCart(@PathVariable("userId") String userId, @Valid @RequestBody Book book) {
        try{
            return new ResponseEntity<>( orderService.removeBookFromCart(book,userId), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book quantity can not be increased!", e);
        }
    }


    @RequestMapping(value = "/{userId}/reset", method = RequestMethod.DELETE)
    public ResponseEntity resetCart(@PathVariable("userId") String userId){
        Order pendingCart = getPendingCart(userId);
        pendingCart= orderService.resetCart(userId, pendingCart.getId());
        return new ResponseEntity<>(pendingCart, HttpStatus.OK);
    }


    @RequestMapping(value ="/{userId}/checkout", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkout(@PathVariable("userId") String userId,
                                   @RequestBody OrderForm orderform) {
        try{
            return new ResponseEntity<>( orderService.checkout(userId, orderform), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checkout failed. Please try again later!", e);
        }
    }

}
