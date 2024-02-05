package com.example.petparadisebe.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;
    private String image;
    private String address;
    private boolean isDelete;
    private LocalDateTime createDate;
    private String role;
    @Column(name = "phone_number", unique = true)
    private String phoneNumber;
}
