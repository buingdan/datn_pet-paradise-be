package com.example.petparadisebe.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
@Entity
public class Order {

    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "address")
    private String address;

    @Column(name = "order_info")
    private String orderInfo;

    @Column(name = "payment_method")
    private String paymentMethod;

    @Column(name = "is_paid")
    private Boolean isPaid = false;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    @Column(name = "is_delete")
    private boolean isDelete;

    public Order(Long id, User user, double totalAmount) {
        this.id = id;
        this.user = user;
        this.isDelete = false;
    }
}

