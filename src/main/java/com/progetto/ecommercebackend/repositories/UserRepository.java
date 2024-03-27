package com.progetto.ecommercebackend.repositories;

import com.progetto.ecommercebackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
}