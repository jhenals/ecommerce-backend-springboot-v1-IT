package com.progetto.ecommercebackend.controllers;

import com.progetto.ecommercebackend.entities.OrderDetail;
import com.progetto.ecommercebackend.services.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders}")
public class OrderController {

    @Autowired
    OrderService orderService;

    @PostMapping("/{userId}")
    public ResponseEntity<String> checkOut(
            @PathVariable("userId") String userId,
            @RequestBody String recipientName,
            @RequestBody String shippingAddress,
            @RequestBody List<OrderDetail> orderDetailList){
        orderService.checkOut(userId, recipientName, shippingAddress, orderDetailList);
        return ResponseEntity.ok("Order is created");
    }

}
