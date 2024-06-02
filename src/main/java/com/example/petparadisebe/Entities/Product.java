package com.example.petparadisebe.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    @Column(name = "quantity_in_stock")
    private int quantityInStock;
//    @Column(name = "discount")
//    private double discount;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    @Column(name = "vote_average")
    private Double voteAverage;
    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;

    public Product(Long id, String name, double price, String image, int quantityInStock, Category category, Double voteAverage, Promotion promotion) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.isDelete = false;
        this.quantityInStock = quantityInStock;
        this.category = category;
        this.voteAverage = voteAverage;
        this.promotion = promotion;
    }
}