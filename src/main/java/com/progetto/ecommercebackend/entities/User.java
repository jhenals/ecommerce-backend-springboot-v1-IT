package com.progetto.ecommercebackend.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "User")
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "firstname")
    private String firstname;

    @Column(name = "lastname")
    private String lastname;

    @Column(name = "email")
    private String email;

    @Column(name ="createdAt")
    private LocalDateTime createdAt;

    @ElementCollection
    @Column(name = "role")
    @CollectionTable(name = "user_roles", joinColumns = @JoinColumn(name = "owner_id"))
    private Set<String> roles = new LinkedHashSet<>();

    public void setFirstName(String firstName) {
        this.firstname = firstName;
    }
    public void setLastName(String lastName) {
        this.lastname = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstname;
    }

    public String getLastName() {
        return this.lastname;
    }


    public String getEmail() {
        return email;
    }


//Other infos are managed by Keycloak

}