package com.example.petparadisebe.dto;

import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CartDto {
    private Long id;
    private User user;
    private Product product;
    private double totalPrice;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    private boolean isDeleted;
    private int quantity;
    private String orderStatus;
}
