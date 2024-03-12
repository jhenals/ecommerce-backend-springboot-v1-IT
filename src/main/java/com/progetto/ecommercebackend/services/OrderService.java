package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.entities.OrderDetail;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.repositories.OrderRepository;
import com.progetto.ecommercebackend.repositories.UserRepository;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;


    public void checkOut(String userId, String recipientName, String shippingAddress, List<OrderDetail> orderDetailList) {
        User user= null;
        Optional<User> userOptional = userRepository.findById(userId);
        if( userOptional.isPresent() ){
            user= userOptional.get();
        }else{
            throw new CustomException("Error in retrieving the custumer");
        }
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setDateOrder(LocalDateTime.now());
        newOrder.setRecipientName(recipientName);
        newOrder.setShippingAddress(shippingAddress);
        newOrder.setTotalAmount(calculateTotalAmount(orderDetailList));
        newOrder.setOrderStatus(OrderStatus.CREATED);
        for ( OrderDetail od : orderDetailList ){
            newOrder.setOrderDetail(od);
            orderRepository.save(newOrder);
        }

    }

    private Double calculateTotalAmount(List<OrderDetail> orderDetailList) {
        Double totAmount = 0D;
        for(OrderDetail od : orderDetailList ){
            totAmount += od.getBook().getFinalPrice()*od.getQuantity();
        }
        return totAmount;
    }
}
