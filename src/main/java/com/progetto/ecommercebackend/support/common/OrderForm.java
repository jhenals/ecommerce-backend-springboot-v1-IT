package com.progetto.ecommercebackend.support.common;

import com.progetto.ecommercebackend.entities.OrderBook;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderForm {
    private String recipientName;
    private String shippingAddress;
    private String phoneNumber;

    /*
    private List<OrderBook> orderBookList;

    public List<OrderBook> getOrderBooks(){
        return orderBookList;
    }

    public void setOrderBookList( List<OrderBook> orderBookList ) {
        this.orderBookList = orderBookList;
    }

     */

}
