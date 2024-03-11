package com.progetto.ecommercebackend.entities;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class OrderDetail {
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

}