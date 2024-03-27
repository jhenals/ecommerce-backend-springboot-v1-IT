package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Order;
import com.progetto.ecommercebackend.support.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus!= 'PENDING'")
    List<Order> findAllByUserId(@Param("userId") String userId);

    @Query("SELECT o FROM Order  o WHERE o.user.id = :userId AND o.id = :orderId")
    List<Order> findAllByUserIdAndOrderId(
            @Param("userId") String userId,
            @Param("orderId") Long orderId);


    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = :orderStatus")
    List<Order> findAllByUserIdAndOrderStatus(
            @Param("userId") String userId,
            @Param("orderStatus") OrderStatus orderStatus);

    @Query("SELECT o FROM Order o WHERE o.id = :orderId")
    List<Order> findAllByOrderId(@Param("orderId") Long orderId);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND o.orderStatus = :orderStatus")
    Order findByUserIdAndOrderStatus(
            @Param("userId") String userId,
            @Param("orderStatus") OrderStatus orderStatus);

    @Query("SELECT o FROM Order  o WHERE o.orderStatus!= 'PENDING' ")
    List<Order> findAllByOrderStatus();
}