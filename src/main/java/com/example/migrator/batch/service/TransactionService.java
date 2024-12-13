package com.example.migrator.batch.service;
import com.example.migrator.batch.domain.AccountEntity;
import com.example.migrator.batch.domain.TransactionEntity;
import com.example.migrator.batch.repository.AccountRepository;
import com.example.migrator.batch.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    public TransactionEntity createTransaction(TransactionEntity transaction) {
        Long accountId = transaction.getAccount().getId();

        // Check if account exists
        Optional<AccountEntity> account = accountRepository.findById(accountId);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("Account with ID " + accountId + " does not exist.");
        }

        // Set the account entity in the transaction
        transaction.setAccount(account.get());
        return transactionRepository.save(transaction);
    }
}
