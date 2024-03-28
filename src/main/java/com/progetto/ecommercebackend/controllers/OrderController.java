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

import static java.time.LocalDateTime.now;

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
    public ResponseEntity<HttpResponse> addBookToCart(@Valid @RequestBody Book book, @PathVariable("userId") String userId ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("item", orderService.addBookToCart(book, userId)))
                        .message("Libro aggiunto nel carrello.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @RequestMapping(value ="/{userId}/checkout", method = RequestMethod.POST)
    public ResponseEntity<HttpResponse> checkout(@PathVariable("userId") String userId,
                                   @RequestBody OrderForm orderform) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("ordine", orderService.checkout(userId, orderform)))
                        .message("Checkout è andato a buon fine.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
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
    public ResponseEntity<HttpResponse> updateOrderStatus( @RequestParam(name = "id") Long orderId, @Valid @RequestBody OrderStatusDTO orderStatus) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("order", orderService.updateOrderStatus(orderId, orderStatus)))
                        .message("Stato dell'ordine aggiornato.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @RequestMapping(value = "/{userId}/incr-quantity/book", method = RequestMethod.PUT)
    public ResponseEntity<HttpResponse> increaseBookQtyInCart(@PathVariable("userId") String userId, @RequestParam(name = "id") Long bookId)  {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("book", orderService.increaseBookQtyInCart(userId, bookId)))
                        .message("Libro aggiunto al carrello.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }

    @RequestMapping(value = "/{userId}/decr-quantity", method = RequestMethod.PUT)
    public ResponseEntity<HttpResponse> decreaseBookQtyInCart(@PathVariable("userId") String userId, @Valid @RequestBody Book book) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("book", orderService.removeBookFromCart(book,userId)))
                        .message("Libro eliminato dal carrello.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    //DELETE
    @RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
    public  ResponseEntity<HttpResponse> removeBookFromCart(@Valid @RequestBody  Book book,@PathVariable("userId") String userId ) {
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("book",orderService.removeBookFromCart(book, userId)))
                        .message("Libro eliminato dal carrello.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


    @RequestMapping(value = "/{userId}/reset", method = RequestMethod.DELETE)
    public ResponseEntity<HttpResponse> resetCart(@PathVariable("userId") String userId){
        Order pendingCart = getPendingCart(userId);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timeStamp(now().toString())
                        .data(Map.of("carrello",orderService.resetCart(userId, pendingCart.getId())))
                        .message("Carrello ripristinato.")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .build());
    }


}
