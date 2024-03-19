package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByIsDeleteFalse();
    List<Product> findByCategoryAndIsDeleteFalse(Category category);
    List<Product> findByCategoryIdAndIsDeleteFalse(Long categoryId);

    Page<Product> findByNameContainsIgnoreCase(String name, Pageable pageable);
    List<Product> findByNameContainsIgnoreCase(String name);

    List<Product> findByIdNotAndNameContainsIgnoreCase(Long id, String name);

    @Query("select j from Product j where j.name like %:key%")
    Page<Product> searchProduct(@Param("key") String textSearch, Pageable pageable);
    @Query("SELECT p.price FROM Product p WHERE p.id = :productId")
    Double getPriceById(@Param("productId") Long productId);
}
