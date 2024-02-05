package com.example.petparadisebe.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data //toString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "carts")
public class Cart {

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column(name = "total_price")
    private double totalPrice; // Thành tiền của mỗi sản phẩm trong giỏ hàng
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "is_deleted")
    private boolean isDeleted;
    @Column(name = "order_status")
    private String orderStatus; // Trạng thái đơn hàng: "Chưa thanh toán", "Đã thanh toán"

    public Cart(Long id, User user, Product product, double totalPrice, boolean isDeleted, String orderStatus) {
        this.id = id;
        this.user = user;
        this.product = product;
        this.totalPrice = totalPrice;
        this.isDeleted = false; // Set the default value here
        this.orderStatus = orderStatus;
    }
}