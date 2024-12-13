package com.example.migrator.batch.service;
import com.example.migrator.batch.domain.AccountEntity;
import com.example.migrator.batch.domain.UserEntity;
import com.example.migrator.batch.repository.AccountRepository;
import com.example.migrator.batch.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public AccountEntity createAccount(AccountEntity account) {
        Long userId = account.getUser().getId();

        // Check if user exists
        Optional<UserEntity> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " does not exist.");
        }

        // Set the user entity in the account
        account.setUser(user.get());
        return accountRepository.save(account);
    }
}
