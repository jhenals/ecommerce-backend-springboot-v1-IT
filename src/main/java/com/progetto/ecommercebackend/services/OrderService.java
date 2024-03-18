package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.entities.OrderDetail;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.repositories.OrderRepository;
import com.progetto.ecommercebackend.repositories.UserRepository;
import com.progetto.ecommercebackend.support.common.OrderForm;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    BookRepository bookRepository;


    public void checkOut(String userId, OrderForm orderForm) {
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
        newOrder.setRecipientName(orderForm.getRecipientName());
        newOrder.setShippingAddress(orderForm.getShippingAddress());
        newOrder.setTotalAmount(calculateTotalAmount(orderForm.getOrderDetailList()));
        newOrder.setOrderStatus(OrderStatus.CREATED);
        for ( OrderDetail od : orderForm.getOrderDetailList() ){
            newOrder.setOrderDetail(od);
            Book book = bookRepository.findBookById(od.getBook().getId());
            if( book.getQuantity()-1 <0 )
                throw new RuntimeException("Not enough stock for book" + od.getBook().getTitle() +".");
            book.setQuantity(book.getQuantity()-1);
            book.setNumPurchases(book.getNumPurchases()+1);
            bookRepository.save(book);
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


    public List<Order> getAllOrdersByUserId(String userId) {
        return orderRepository.findAllByUserId(userId);
    }

    public List<OrderDetail> getAllOrderDetailsByUserIdAndOrderId(String userId, Long orderId) {
        List <OrderDetail> orderDetailList = new ArrayList<>();
        List<Order> orderList = orderRepository.findAllByUserIdAndOrderId(userId, orderId);
        for( Order o : orderList){
            orderDetailList.add(o.getOrderDetail());
        }
        return orderDetailList;
    }

    public List<Order> getAllOrdersByUserIdAndOrderStatus(String userId, OrderStatus orderStatus) {
        return orderRepository.findAllByUserIdAndOrderStatus(userId, orderStatus);
    }

    public void cancelOrder(String userId, Long orderId) {
        List<Order> orderList = orderRepository.findAllByUserIdAndOrderId(userId, orderId);
        for(Order order : orderList ){
            orderRepository.delete(order);
        }
    }

    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        List<Order> orderList = orderRepository.findAllByOrderId(orderId);
        for (Order order : orderList){
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);
        }
    }
}
