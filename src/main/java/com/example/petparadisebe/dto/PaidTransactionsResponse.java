package com.example.petparadisebe.dto;
import com.example.petparadisebe.Entities.Transaction;

import java.util.List;

public class PaidTransactionsResponse {
    private List<Transaction> transactions;
    private Long totalAmount;

    public PaidTransactionsResponse(List<Transaction> transactions, Long totalAmount) {
        this.transactions = transactions;
        this.totalAmount = totalAmount;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }
}

