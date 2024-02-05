package com.example.petparadisebe.Controller;

import com.example.petparadisebe.Entities.StockTransaction;
import com.example.petparadisebe.Service.StockTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock-transactions")
public class StockTransactionController {
    @Autowired
    private StockTransactionService stockTransactionService;

    @GetMapping
    public List<StockTransaction> getAllStockTransactions() {
        return stockTransactionService.getAllStockTransactions();
    }

}
