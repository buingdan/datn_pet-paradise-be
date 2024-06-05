package com.example.petparadisebe.Controllers;

import com.example.petparadisebe.Services.TransactionService;
import com.example.petparadisebe.dto.PaidTransactionsResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @GetMapping()
    //GET http://localhost:8090/api/v1/transactions
    public ResponseEntity<?> getPaidTransactionsWithTotalAmount() {
        PaidTransactionsResponse paidTransactionsResponse = transactionService.getPaidTransactionsAndTotalAmount();
        return new ResponseEntity<>(paidTransactionsResponse, HttpStatus.OK);
    }
}