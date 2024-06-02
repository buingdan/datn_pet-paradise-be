package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.Promotion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    List<Promotion> findByNameContainsIgnoreCase(String name);
    @Query("select j from Promotion j where j.name like %:key%")
    Page<Promotion> searchPromotion(@Param("key") String textSearch, Pageable pageable);
}
