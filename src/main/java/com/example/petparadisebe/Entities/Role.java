package com.example.petparadisebe.Entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "roles")
public class Role {
    //tạo ra roles_sequence giúp giá trị của id tự động tăng khi một số hệ thống không hỗ trợ việc đó.
    // Ta phải quản lí quản lý sequence trong các hệ thống như Oracle
    // Tương đương việc sử dụng GenerationType.IDENTITY với MySQL sẽ là lựa chọn đơn giản và hiệu quả
    @Id
    @SequenceGenerator(
            name = "roles_sequence",
            sequenceName = "roles_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.AUTO,
            generator = "roles_sequence"
    )

    private Long id;
    private String name;
    @Column(name = "is_delete")
    private boolean isDelete;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    @Fetch(value = FetchMode.SELECT)
    @JsonIgnore
    private Set<User> users = new HashSet<>();

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
        this.isDelete = false;
    }
    public Role(String name) {
        this.name = name;
    }
}
