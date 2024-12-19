package com.example.migrator.batch.domain;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "migration_metadata")
public class MigrationMetadata {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "last_migration_time", nullable = false)
    private LocalDateTime lastMigrationTime;

    public MigrationMetadata() {
    }

    public MigrationMetadata(LocalDateTime lastMigrationTime) {
        this.lastMigrationTime = lastMigrationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getLastMigrationTime() {
        return lastMigrationTime;
    }

    public void setLastMigrationTime(LocalDateTime lastMigrationTime) {
        this.lastMigrationTime = lastMigrationTime;
    }
}

