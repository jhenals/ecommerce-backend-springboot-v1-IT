package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.entities.OrderBook;
import com.progetto.ecommercebackend.entities.User;
import com.progetto.ecommercebackend.repositories.BookRepository;
import com.progetto.ecommercebackend.repositories.OrderBookRepository;
import com.progetto.ecommercebackend.repositories.OrderRepository;
import com.progetto.ecommercebackend.repositories.UserRepository;
import com.progetto.ecommercebackend.support.common.OrderForm;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import com.progetto.ecommercebackend.support.exceptions.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class OrderService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderBookRepository orderBookRepository;

    @Autowired
    BookRepository bookRepository;

    @Transactional
    public Order getPendingCart(String userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Order pendingCart = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
            if (pendingCart == null) {
                pendingCart = new Order();
                pendingCart.setUser(user);
                pendingCart.setOrderStatus(OrderStatus.PENDING);
                pendingCart = orderRepository.save(pendingCart);
            }
            return pendingCart;
        } else {
            throw new CustomException("User not found.");
        }
    }

    @Transactional(readOnly = false)
    public Order resetCart(String userId, Long orderId) {
        Optional<Order> pendingCartOptional = orderRepository.findById(orderId);
        if (pendingCartOptional.isPresent()) {
            for (OrderBook orderBook : orderBookRepository.findAllByOrderId(orderId)) {
                orderBookRepository.delete(orderBook);
            }
            return pendingCartOptional.get();
        } else {
            throw new CustomException("Error in resetting cart");
        }
    }

    @Transactional(readOnly = false)
    public Order addBookToCart(Book book, String userId) {
        Optional<User> user = userRepository.findById(userId);
        Order pendingCart =  getPendingCart(userId);
        if(book == null ){
            throw new CustomException("Book is required");
        }
        Optional<OrderBook> orderBookOptional = Optional.ofNullable(orderBookRepository.findByBookIdAndOrderId(book.getId(), pendingCart.getId()));
        if( orderBookOptional.isPresent() ){
            throw new CustomException("Book is already present in cart");
        }else{
            OrderBook orderBook = new OrderBook(pendingCart, book, 1);
            orderBookRepository.save(orderBook);
            return pendingCart;

        }
    }

    @Transactional(readOnly = false)
    public Order removeBookFromCart(Book book, String userId) {
        Optional<User> user = userRepository.findById(userId);
        Order pendingCart =  getPendingCart(userId);
        if(book == null ){
            throw new CustomException("Book is required");
        }
        Optional<OrderBook> orderBookOptional = Optional.ofNullable(orderBookRepository.findByBookIdAndOrderId(book.getId(), pendingCart.getId()));
        if( orderBookOptional.isPresent() ){
            OrderBook orderBook = orderBookOptional.get();
            if (orderBook.getQuantity() == 1) {
                orderBookRepository.delete(orderBook);
            }else{
                orderBook.setQuantity(orderBook.getQuantity()-1);
                orderBookRepository.save(orderBook);
            }
            orderRepository.save(pendingCart);
            return pendingCart;
        }else{
            throw new CustomException("Book is not present in cart");
        }
    }

    @Transactional(readOnly = false)
    public OrderBook incrementBookQtyInCart(String userId, Book book) {
        Order pendingCart = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);
        OrderBook orderBook = orderBookRepository.findByBookIdAndOrderId(book.getId(), pendingCart.getId());
        if (orderBook == null){
            throw new CustomException("Book is not present in cart");
        }else{
            pendingCart.getOrderBooks().remove(orderBook);
            orderBook.setQuantity(orderBook.getQuantity()+1);
            orderBook = orderBookRepository.save(orderBook);
            pendingCart.getOrderBooks().add(orderBook);
            orderRepository.save(pendingCart);
            return orderBook;
        }
    }

    @Transactional(readOnly = false )
    public Order checkout(String userId, OrderForm orderForm) {
        Optional<User> userOptional = userRepository.findById(userId);
        Order pendingCart = orderRepository.findByUserIdAndOrderStatus(userId, OrderStatus.PENDING);

        if( orderForm.getRecipientName()!=null && orderForm.getShippingAddress() != null && orderForm.getPhoneNumber()!=null){
            Order newOrder = pendingCart;
            newOrder.setDateCreated(LocalDateTime.now());
            newOrder.setOrderStatus(OrderStatus.PROCESSING);
            newOrder.setRecipientName(orderForm.getRecipientName());
            newOrder.setShippingAddress(orderForm.getShippingAddress());
            newOrder.setPhoneNumber(orderForm.getPhoneNumber());
            newOrder.setTotalAmount(pendingCart.getTotalPrice());
            orderRepository.save(newOrder);
            Order newPendingCart = new Order();
            newPendingCart.setUser(userOptional.get());
            newPendingCart.setOrderStatus(OrderStatus.PENDING);
            orderRepository.save(newPendingCart);
            return newOrder;
        } else {
            throw new CustomException("Error in checking out.");
        }
    }



    /*
    public List<Order> checkOut(String userId, OrderForm orderForm) {
        log.info("Order of user {}: {}", userId, orderForm.toString());
        List<Order> orders = new ArrayList<>(); //return
        User user= null;
        Optional<User> userOptional = userRepository.findById(userId);
        if( userOptional.isPresent() ){
            user= userOptional.get();
        }else{
            throw new CustomException("Error in retrieving the custumer");
        }
        Order newOrder = new Order();
        newOrder.setUser(user);
        newOrder.setDateCreated(LocalDateTime.now());
        newOrder.setRecipientName(orderForm.getRecipientName());
        newOrder.setShippingAddress(orderForm.getShippingAddress());
        newOrder.setPhoneNumber(orderForm.getPhoneNumber());
        newOrder.setTotalAmount(orderForm.getTotalAmount());
        newOrder.setOrderStatus(OrderStatus.CREATED);
        if(orderForm.getOrderDetailList().size()==0){
            throw new CustomException("There is no product in cart.");
        }else{
            for ( OrderDetail od : orderForm.getOrderDetailList() ){
                newOrder.setOrderDetail(od);
                Book book = bookRepository.findBookById(od.getBook().getId());
            /*

            if( book.getQuantity()-1 <0 )
                throw new RuntimeException("Not enough stock for book" + od.getBook().getTitle() +".");
            book.setQuantity(book.getQuantity()-1);


                book.setNumPurchases(book.getNumPurchases()+1);
                bookRepository.save(book);
                orders.add(orderRepository.save(newOrder));
            }
            return orders;
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

     */
}
