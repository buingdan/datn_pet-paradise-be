package com.example.petparadisebe.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "promotions")
@Entity
public class Promotion {
    @PrePersist
    protected void onCreate() {
        this.createDate = LocalDateTime.now();
    }
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;

    @Column(name = "discount")
    private double discount;

    @Column(name = "start_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "end_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate  endDate;

    @Column(name = "create_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;

    @Column(name = "is_delete")
    private boolean isDelete;

    public Promotion(Long id, String name, double discount, LocalDate  startDate, LocalDate  endDate, boolean isDelete) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isDelete = false;
    }
}
