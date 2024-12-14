package com.example.migrator.batch.domain;

import java.time.LocalDateTime;

public class TransactionDocument {
    private String transactionType;
    private Double amount;
    private LocalDateTime timestamp;

    // Getters and setters

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}

