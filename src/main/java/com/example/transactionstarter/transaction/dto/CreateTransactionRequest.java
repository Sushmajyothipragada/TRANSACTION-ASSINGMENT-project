package com.example.transactionstarter.transaction.dto;

import com.example.transactionstarter.transaction.enums.Currency;
import com.example.transactionstarter.transaction.enums.TransactionType;

public class CreateTransactionRequest {
    private String transactionId;
    private String customerId;
    private double amount;
    private Currency currency;
    private TransactionType transactionType;

}
