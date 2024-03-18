package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.entities.OrderDetail;
import com.progetto.ecommercebackend.services.OrderService;
import com.progetto.ecommercebackend.support.common.OrderForm;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    @Autowired
    OrderService orderService;


    //CREATE
    @PostMapping("/user")
    public ResponseEntity<String> checkOut(
            @RequestParam(name = "id") String userId,
            @RequestBody OrderForm orderForm){
        orderService.checkOut(userId, orderForm);
        return new ResponseEntity<>("New order is created", HttpStatus.CREATED);
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




}
