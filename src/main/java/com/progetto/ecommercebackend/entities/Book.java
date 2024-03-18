package com.progetto.ecommercebackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.proxy.HibernateProxy;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

    @Lob
    @Column(name = "description")
    private String description;


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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            },
            mappedBy = "books")
    @JsonIgnore
    private Set<Author> authors = new HashSet<>();


    //INVENTORY

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "num_purchases")
    private Integer numPurchases;


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

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Book book = (Book) o;
        return getId() != null && Objects.equals(getId(), book.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}