package com.example.petparadisebe.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
@Entity
public class Product {
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", length = 50)
    private String name;
    @Column(name = "price")
    private double price;
    @Column(name = "image")
    private String image;
    @Column(name = "is_delete")
    private boolean isDelete;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "quantity_in_stock")
    private int quantityInStock;
    @Column(name = "discount")
    private double discount;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "vote_average")
    private Double voteAverage;

    public Product(Long id, String name, double price, String image, int quantityInStock, double discount, Category category, Double voteAverage) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.isDelete = false;
        this.quantityInStock = quantityInStock;
        this.discount = discount;
        this.category = category;
        this.voteAverage = voteAverage;
    }
}