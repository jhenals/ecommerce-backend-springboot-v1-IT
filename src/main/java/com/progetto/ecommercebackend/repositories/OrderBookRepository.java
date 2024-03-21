package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.OrderBook;
import com.progetto.ecommercebackend.entities.OrderBookPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderBookRepository extends JpaRepository<OrderBook, OrderBookPK> {
    @Query("SELECT ob FROM OrderBook ob WHERE ob.pk.order.id= :cartId")
    List<OrderBook> findAllByOrderId(
            @Param("cartId") Long orderId);


    @Query("SELECT ob FROM OrderBook  ob WHERE ob.pk.book.id = :bookId AND ob.pk.order.id = :orderId")
    OrderBook findByBookIdAndOrderId(
            @Param("bookId")Long bookId,
            @Param("orderId")Long orderId);
}