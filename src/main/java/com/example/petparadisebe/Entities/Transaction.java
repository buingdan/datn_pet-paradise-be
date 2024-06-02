package com.example.petparadisebe.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transaction implements Serializable {
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_no")
    private String billNo;
    @Column(name = "trans_no", length = 50)
    private String transNo;
    @Column(name = "bank_code")
    private String bankCode;
    @Column(name = "card_type")
    private String cardType;
    @Column(name = "amount")
    private Integer amount;
    @Column(name = "currency")
    private String currency;
    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    @Column(name = "order_infor")
    private String orderInfor;
    @Column(name = "status")
    private String status;
    @Column(name = "payment_time")
    private String paymentTime;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
