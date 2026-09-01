package com.example.transactionstarter.transaction.controller;
import com.example.transactionstarter.transaction.dto.UpdateStatusRequest;

import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.example.transactionstarter.transaction.entity.Transaction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.transactionstarter.transaction.service.TransactionService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Transaction createTransaction(@RequestBody Transaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/{transactionId}")
    public Transaction getTransaction(@PathVariable String transactionId) {
        return transactionService.getTransaction(transactionId);
    }

    @PatchMapping("/{transactionId}/status")
    public Transaction updateTransactionStatus(
            @PathVariable String transactionId,
            @RequestBody UpdateStatusRequest request) {

        return transactionService.updateTransactionStatus(transactionId, request);
    }
    @GetMapping("/customer/{customerId}")
    public List<Transaction> getCustomerTransactions(  
        @PathVariable String customerId) {

    return transactionService.getTransactionsByCustomer(customerId);
}

}