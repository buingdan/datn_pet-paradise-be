package com.example.petparadisebe.Repository;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsDeleteFalse();
    List<Product> findByCategoryAndIsDeleteFalse(Category category);
}
