package com.example.migrator.batch.domain;

import java.util.List;

public class AccountDocument {
    private String accountType;
    private Double balance;
    private List<TransactionDocument> transactions;

    // Getters and setters

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public List<TransactionDocument> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDocument> transactions) {
        this.transactions = transactions;
    }
}

