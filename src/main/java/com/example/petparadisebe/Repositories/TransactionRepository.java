package com.example.petparadisebe.Repositories;

import com.example.petparadisebe.Entities.Cart;
import com.example.petparadisebe.Entities.Product;
import com.example.petparadisebe.Entities.Transaction;
import com.example.petparadisebe.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findFirstByOrderByIdDesc();

    List<Transaction> findAllByUser_Id(Long userID);

    Transaction findByTransNo(String transNo);
}
