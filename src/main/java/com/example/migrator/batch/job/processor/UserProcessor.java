package com.example.migrator.batch.job.processor;

import com.example.migrator.batch.domain.AccountDocument;
import com.example.migrator.batch.domain.TransactionDocument;
import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserProcessor implements ItemProcessor<UserEntity, UserDocument> {

    @Override
    public UserDocument process(UserEntity userEntity) {
        // Local variables ensure no shared mutable state
        UserDocument userDoc = createUserDocument(userEntity);
        return userDoc;
    }

    /**
     * Creates a UserDocument from a UserEntity, ensuring all operations are isolated and thread-safe.
     */
    private UserDocument createUserDocument(UserEntity userEntity) {
        UserDocument userDoc = new UserDocument();
        userDoc.setId(String.valueOf(userEntity.getId()));
        userDoc.setName(userEntity.getName());
        userDoc.setEmail(userEntity.getEmail());
        userDoc.setPhone(userEntity.getPhone());

        // Map Accounts
        List<AccountDocument> accountDocuments = userEntity.getAccounts()
                .parallelStream() // Use parallel stream for potential minor performance boost
                .map(this::mapAccount)
                .collect(Collectors.toList());

        userDoc.setAccounts(accountDocuments);
        return userDoc;
    }

    /**
     * Maps AccountEntity to AccountDocument.
     */
    private AccountDocument mapAccount(com.example.migrator.batch.domain.AccountEntity account) {
        AccountDocument accDoc = new AccountDocument();
        accDoc.setAccountType(account.getAccountType());
        accDoc.setBalance(account.getBalance());

        // Map Transactions
        List<TransactionDocument> transactionDocuments = account.getTransactions()
                .parallelStream() // Parallel stream for transactions
                .map(this::mapTransaction)
                .collect(Collectors.toList());

        accDoc.setTransactions(transactionDocuments);
        return accDoc;
    }

    /**
     * Maps TransactionEntity to TransactionDocument.
     */
    private TransactionDocument mapTransaction(com.example.migrator.batch.domain.TransactionEntity tx) {
        TransactionDocument txDoc = new TransactionDocument();
        txDoc.setTransactionType(tx.getTransactionType());
        txDoc.setAmount(tx.getAmount());
        txDoc.setTimestamp(tx.getTimestamp());
        return txDoc;
    }
}
