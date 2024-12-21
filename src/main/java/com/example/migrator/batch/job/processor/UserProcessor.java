package com.example.migrator.batch.job.processor;

import com.example.migrator.batch.domain.AccountDocument;
import com.example.migrator.batch.domain.TransactionDocument;
import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component // Marks this class as a Spring-managed component, allowing it to be injected as a bean
public class UserProcessor implements ItemProcessor<UserEntity, UserDocument> {

    private final Map<Long, UserDocument> entityDocumentMap; // Shared map to store processed UserDocuments for reference

    // Constructor for dependency injection
    public UserProcessor(Map<Long, UserDocument> entityDocumentMap) {
        this.entityDocumentMap = entityDocumentMap;
    }

    /**
     * Processes a UserEntity into a UserDocument.
     * @param userEntity The source entity to process.
     * @return The processed UserDocument.
     */
    @Override
    public UserDocument process(UserEntity userEntity) {
        // Creates a UserDocument from the UserEntity
        UserDocument userDoc = createUserDocument(userEntity);

        // Stores the UserDocument in the shared map for tracking
        entityDocumentMap.put(userEntity.getId(), userDoc);

        return userDoc; // Returns the processed UserDocument
    }

    /**
     * Creates a UserDocument from a UserEntity.
     * This method performs the mapping for the UserEntity and its related entities (accounts and transactions).
     * @param userEntity The UserEntity to convert.
     * @return The corresponding UserDocument.
     */
    private UserDocument createUserDocument(UserEntity userEntity) {
        UserDocument userDoc = new UserDocument();
        userDoc.setId(String.valueOf(userEntity.getId())); // Set the ID as a string
        userDoc.setName(userEntity.getName()); // Map the name
        userDoc.setEmail(userEntity.getEmail()); // Map the email
        userDoc.setPhone(userEntity.getPhone()); // Map the phone

        // Map the related accounts
        List<AccountDocument> accountDocuments = userEntity.getAccounts()
                .parallelStream() // Use a parallel stream for potential performance improvement
                .map(this::mapAccount) // Map each AccountEntity to AccountDocument
                .collect(Collectors.toList());

        userDoc.setAccounts(accountDocuments); // Set the mapped accounts
        return userDoc;
    }

    /**
     * Maps an AccountEntity to an AccountDocument.
     * @param account The AccountEntity to convert.
     * @return The corresponding AccountDocument.
     */
    private AccountDocument mapAccount(com.example.migrator.batch.domain.AccountEntity account) {
        AccountDocument accDoc = new AccountDocument();
        accDoc.setAccountType(account.getAccountType()); // Map the account type
        accDoc.setBalance(account.getBalance()); // Map the balance

        // Map the related transactions
        List<TransactionDocument> transactionDocuments = account.getTransactions()
                .parallelStream() // Use a parallel stream for transactions as well
                .map(this::mapTransaction) // Map each TransactionEntity to TransactionDocument
                .collect(Collectors.toList());

        accDoc.setTransactions(transactionDocuments); // Set the mapped transactions
        return accDoc;
    }

    /**
     * Maps a TransactionEntity to a TransactionDocument.
     * @param tx The TransactionEntity to convert.
     * @return The corresponding TransactionDocument.
     */
    private TransactionDocument mapTransaction(com.example.migrator.batch.domain.TransactionEntity tx) {
        TransactionDocument txDoc = new TransactionDocument();
        txDoc.setTransactionType(tx.getTransactionType()); // Map the transaction type
        txDoc.setAmount(tx.getAmount()); // Map the amount
        txDoc.setTimestamp(tx.getTimestamp()); // Map the timestamp
        return txDoc;
    }
}
