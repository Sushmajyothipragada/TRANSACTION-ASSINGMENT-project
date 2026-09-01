package com.example.transactionstarter.transaction.service;
import com.example.transactionstarter.transaction.entity.Transaction;
import com.example.transactionstarter.transaction.enums.TransactionStatus;
import com.example.transactionstarter.transaction.exception.DuplicateTransactionException;
import com.example.transactionstarter.transaction.exception.TransactionNotFoundException;
import com.example.transactionstarter.transaction.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import com.example.transactionstarter.transaction.dto.UpdateStatusRequest;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

public Transaction createTransaction(Transaction transaction) {

    validateTransaction(transaction);

    if (transactionRepository.existsById(transaction.getTransactionId())) {
        throw new DuplicateTransactionException("Transaction ID already exists");
    }

    return transactionRepository.save(transaction);
}


public Transaction getTransaction(String transactionId) {

        return transactionRepository.findById(transactionId)
                .orElseThrow(() ->
                        new TransactionNotFoundException("Transaction not found"));
    }

public Transaction updateTransactionStatus(
        String transactionId,
        UpdateStatusRequest request) {

    Transaction transaction = transactionRepository.findById(transactionId)
            .orElseThrow(() ->
                    new TransactionNotFoundException("Transaction not found"));

    validateStatusChange(transaction.getStatus(), request.getStatus());

    transaction.setStatus(request.getStatus());

    return transactionRepository.save(transaction);
}
private void validateStatusChange(
        TransactionStatus currentStatus,
        TransactionStatus newStatus) {

    if (newStatus == null) {
        throw new IllegalArgumentException("New status is required");
    }

    if (currentStatus != TransactionStatus.PENDING) {
        throw new IllegalArgumentException(
                "Only PENDING transactions can change status");
    }
}
private void validateTransaction(Transaction transaction) {

        if (transaction.getTransactionId() == null ||
            transaction.getTransactionId().isBlank()) {
            throw new IllegalArgumentException("Transaction ID is required");
        }

        if (transaction.getCustomerId() == null ||
            transaction.getCustomerId().isBlank()) {
            throw new IllegalArgumentException("Customer ID is required");
        }

        if (transaction.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }

        if (transaction.getCurrency() == null) {
            throw new IllegalArgumentException("Currency is required");
        }

        if (transaction.getTransactionType() == null) {
            throw new IllegalArgumentException("Transaction type is required");
        }

        if (transaction.getStatus() == null) {
            throw new IllegalArgumentException("Transaction status is required");
        }
    }
public List<Transaction> getTransactionsByCustomer(String customerId) {

    return transactionRepository.findByCustomerId(customerId);
}

}
