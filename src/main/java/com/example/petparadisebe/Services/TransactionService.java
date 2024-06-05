package com.example.petparadisebe.Services;

import com.example.petparadisebe.Entities.Transaction;
import com.example.petparadisebe.Repositories.TransactionRepository;
import com.example.petparadisebe.dto.PaidTransactionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public PaidTransactionsResponse getPaidTransactionsAndTotalAmount() {
        List<Transaction> paidTransactions = transactionRepository.findByStatus("Đã thanh toán");
        Long totalAmount = paidTransactions.stream().mapToLong(Transaction::getAmount).sum();
        return new PaidTransactionsResponse(paidTransactions, totalAmount);
    }
}
