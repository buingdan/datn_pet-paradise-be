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
@Table(name = "stock_transactions")
@Entity
public class StockTransaction {

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "quantity")
    private int quantity;
    @Column(name = "transaction_type")
    private String transactionType;
    @Column(name = "is_delete")
    private boolean isDelete;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    public StockTransaction(Long id, Product product, int quantity, String transactionType) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.transactionType = transactionType;
        this.isDelete = false;
    }
}