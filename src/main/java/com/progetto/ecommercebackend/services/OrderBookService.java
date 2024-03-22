package com.progetto.ecommercebackend.services;

import com.progetto.ecommercebackend.entities.OrderBook;
import com.progetto.ecommercebackend.repositories.OrderBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OrderBookService {

    @Autowired
    OrderBookRepository orderBookRepository;


    public List<OrderBook> getItemsInPendingCart(Long cartId) {
        return orderBookRepository.findAllByOrderId(cartId);
    }

}
