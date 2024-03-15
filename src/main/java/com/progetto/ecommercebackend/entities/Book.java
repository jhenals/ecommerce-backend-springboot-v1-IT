package com.progetto.ecommercebackend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Blob;
import java.sql.NClob;
import java.time.LocalDate;

@Getter
@Setter
@Entity(name = "Book")
@Table(name = "book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "cover_url")
    private String coverUrl;




    @Column(name = "discount")
    private Integer discount;

    @Column(name = "price")
    private Double price;

    @Column(name = "final_price")
    private Double finalPrice;
    @Column(name = "publication_date")
    private LocalDate publicationDate;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;




    //INVENTORY

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "num_purchases")
    private Integer numPurchases;

    @Lob
    @Column(name = "description")
    private NClob description;

    public Double getFinalPrice() {
        if (discount != null) {
            double discountAmount = (price * discount) / 100.0;
            double discountedPrice = BigDecimal.valueOf(price - discountAmount)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
            return discountedPrice;
        } else {
            return price;
        }
    }

}