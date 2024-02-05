package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsDeleteFalse();
    List<Product> findByCategoryAndIsDeleteFalse(Category category);

    Page<Product> findByNameContainsIgnoreCase(String name, Pageable pageable);
    List<Product> findByNameContainsIgnoreCase(String name);

    List<Product> findByIdNotAndNameContainsIgnoreCase(Long id, String name);
}
