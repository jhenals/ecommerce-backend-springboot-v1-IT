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

    @RequestMapping(value = "/{userId}/incr-quantity", method = RequestMethod.PUT, consumes = {"application/json"})
    public ResponseEntity increaseBookQtyInCart(@PathVariable("userId") String userId, @Valid @RequestBody Book book)  {
        try{
            return new ResponseEntity<>( orderService.incrementBookQtyInCart(userId, book), HttpStatus.OK);
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


    @RequestMapping(value = "/{userId}/reset", method = RequestMethod.PUT)
    public ResponseEntity resetCart(@PathVariable("userId") String userId){
        Order pendingCart = getPendingCart(userId);
        pendingCart= orderService.resetCart(userId, pendingCart.getId());
        return new ResponseEntity<>(pendingCart, HttpStatus.OK);
    }


    @RequestMapping(value ="/{userId}/checkout", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity checkout(@PathVariable("userId") String userId,
                                   @RequestBody OrderForm orderform) {
        try{
            return new ResponseEntity<>( orderService.checkout(userId, orderform), HttpStatus.OK);
        }catch(CustomException e){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Checkout failed. Please try again later!", e);
        }
    }



/*

    @PostMapping
    public ResponseEntity<HttpResponse> checkOut(
            @RequestParam(name = "id") String userId,
            @RequestBody OrderForm orderForm){
        List<OrderBook> booksOrdered = orderForm.getOrderBooks();
        validateBooksExistence(booksOrdered);

        Order order = new Order();
        order.setOrderStatus(OrderStatus.PAID);
        order = this.orderService.create(order)

        ;

        List<OrderBook> orderBooks = new ArrayList<>();
        for ( OrderBook ob : booksOrdered ) {
            orderBooks.add(orderBookService.create(new OrderBook(order, bookService.getBookById(ob.getBook().getId()), ob.getQuantity())));
        }
        order.setOrderBooks((orderBooks));
        this.orderService.update(order);

        return
                ResponseEntity.ok().body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(Map.of("order", order))
                                .message("New order has been made.")
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .build());
    }

        /*

    //CREATE
    @PostMapping("/user")
    public ResponseEntity<HttpResponse> checkOut(
            @RequestParam(name = "id") String userId,
            @RequestBody OrderForm orderForm){

        return
                ResponseEntity.ok().body(
                        HttpResponse.builder()
                                .timeStamp(now().toString())
                                .data(Map.of("order", orderService.checkOut(userId, orderForm)))
                                .message("New order has been made.")
                                .status(HttpStatus.CREATED)
                                .statusCode(HttpStatus.CREATED.value())
                                .build());
    }

    //READ

    @GetMapping("/user")
    public List<OrderDetail> getAllOrderDetailsByUserIdAndOrderId(@RequestParam(name = "id") String userId, @RequestParam Long orderId){
        return orderService.getAllOrderDetailsByUserIdAndOrderId(userId, orderId);
    }

    @GetMapping()
    public List<Order> getAllOrdersByUserId(@RequestParam String userId){
        return orderService.getAllOrdersByUserId(userId);
    }


    //UPDATE
    @RequestMapping(value = ("update-order"), method = RequestMethod.PUT)
    @PreAuthorize("hasRole('ROLE_admin')")
    public ResponseEntity<String> updateOrderStatus(@RequestParam Long orderId, @RequestParam OrderStatus orderStatus){
        orderService.updateOrderStatus(orderId, orderStatus);
        return new ResponseEntity<>("Order status of order "+ orderId + " is updated.", HttpStatus.OK);
    }

    //DELETE
    @RequestMapping(value = ("({userId}"), method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_user')")
    public ResponseEntity<String> cancelOrder(@PathVariable("userId") String userId, @RequestParam Long orderId){
        orderService.cancelOrder(userId, orderId);
        return new ResponseEntity<>("Order "+ orderId + " is cancelled.", HttpStatus.OK);
    }

    @GetMapping("orders-users-orderstatus")
    public List<Order> getAllOrdersByUserIdAndOrderStatus(@RequestParam String userId, @RequestParam OrderStatus orderStatus){
        return orderService.getAllOrdersByUserIdAndOrderStatus(userId, orderStatus);
    }

         */

    private void validateBooksExistence( List<OrderBook> orderBookList ){
        List<OrderBook> list = orderBookList
                .stream()
                .filter(op -> Objects.isNull(bookService.getBookById(op
                        .getBook()
                        .getId())))
                .collect(Collectors.toList());

        if( !CollectionUtils.isEmpty(list)){
            new ResourceNotFoundException("Book not found");
        }
    }



}
