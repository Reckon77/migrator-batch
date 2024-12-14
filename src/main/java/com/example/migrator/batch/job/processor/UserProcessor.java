package com.example.migrator.batch.job.processor;
import com.example.migrator.batch.domain.AccountDocument;
import com.example.migrator.batch.domain.TransactionDocument;
import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserProcessor implements ItemProcessor<UserEntity, UserDocument> {

    @Override
    public UserDocument process(UserEntity userEntity) throws Exception {
        UserDocument userDoc = new UserDocument();
        userDoc.setName(userEntity.getName());
        userDoc.setEmail(userEntity.getEmail());
        userDoc.setPhone(userEntity.getPhone());
        System.out.println("Processor.............................................");

        userDoc.setAccounts(userEntity.getAccounts().stream().map(account -> {
            AccountDocument accDoc = new AccountDocument();
            accDoc.setAccountType(account.getAccountType());
            accDoc.setBalance(account.getBalance());
            accDoc.setTransactions(account.getTransactions().stream().map(tx -> {
                TransactionDocument txDoc = new TransactionDocument();
                txDoc.setTransactionType(tx.getTransactionType());
                txDoc.setAmount(tx.getAmount());
                txDoc.setTimestamp(tx.getTimestamp());
                return txDoc;
            }).collect(Collectors.toList()));
            return accDoc;
        }).collect(Collectors.toList()));

        return userDoc;
    }
}

