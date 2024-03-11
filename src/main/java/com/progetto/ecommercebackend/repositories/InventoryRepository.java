package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Book;
import com.progetto.ecommercebackend.entities.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {
}