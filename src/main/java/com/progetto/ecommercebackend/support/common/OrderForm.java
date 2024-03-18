package com.progetto.ecommercebackend.support.common;

import com.progetto.ecommercebackend.entities.OrderDetail;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderForm {
    private String recipientName;
    private String shippingAddress;
    private List<OrderDetail> orderDetailList;

}
