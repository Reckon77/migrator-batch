package com.example.migrator.batch.controller;

import com.example.migrator.batch.domain.AccountEntity;
import com.example.migrator.batch.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountEntity> createAccount(@RequestBody AccountEntity account) {
        AccountEntity savedAccount = accountService.createAccount(account);
        return ResponseEntity.ok(savedAccount);
    }
}
