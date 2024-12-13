package com.example.migrator.batch.controller;
import com.example.migrator.batch.domain.UserEntity;
import com.example.migrator.batch.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserEntity> createUser(@RequestBody UserEntity user) {
        UserEntity savedUser = userService.createUser(user);
        return ResponseEntity.ok(savedUser);
    }
}
