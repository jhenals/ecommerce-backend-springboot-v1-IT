package com.progetto.ecommercebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_book")
public class OrderBook {
    @EmbeddedId
    @JsonIgnore
    private OrderBookPK pk;

    @Column(nullable = false)
    private Integer quantity;

    public OrderBook( Order order, Book book, Integer quantity){
        pk = new OrderBookPK();
        pk.setOrder(order);
        pk.setBook(book);
        this.quantity = quantity;
    }

    public OrderBook() {

    }

    @Transient
    public Book getBook(){
        return this.pk.getBook();
    }

    @Transient
    public Double getBookFinalPrice() {
      return getBook().getFinalPrice()* getQuantity();
    }

}