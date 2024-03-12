package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}