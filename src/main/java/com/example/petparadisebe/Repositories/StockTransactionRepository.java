package com.example.petparadisebe.Repository;

import com.example.petparadisebe.Entities.StockTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StockTransactionRepository extends JpaRepository<StockTransaction, Long> {
    List<StockTransaction> findByIsDeleteFalse();
}
