package com.example.migrator.batch.domain;

import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "users")
public class UserDocument {
    @Id
    private String id;
    private String name;
    private String email;
    private String phone;
    private List<AccountDocument> accounts;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<AccountDocument> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<AccountDocument> accounts) {
        this.accounts = accounts;
    }
// Getters and setters
}
