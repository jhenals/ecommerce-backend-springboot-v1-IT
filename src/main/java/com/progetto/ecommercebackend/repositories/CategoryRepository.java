package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Transactional
    @Modifying
    @Query("delete from Category c where c.id = ?1")
    void deleteCategoryById(Long id);


    Optional<Category> findByValue(String catValue);
}