package com.example.transactionstarter.transaction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicateTransactionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicateTransactionException(String message) {
        super(message);
    }
}