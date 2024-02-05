package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Category;
import com.example.petparadisebe.Entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameStartsWith(String name, Pageable pageable);
    Page<Category> findByNameContainsIgnoreCase(String name, Pageable pageable);
    List<Category> findByNameContainsIgnoreCase(String name);
}
