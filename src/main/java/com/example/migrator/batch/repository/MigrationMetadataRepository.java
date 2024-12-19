package com.example.migrator.batch.repository;

import com.example.migrator.batch.domain.MigrationMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrationMetadataRepository extends JpaRepository<MigrationMetadata, Long> {

    // Custom query to fetch the latest migration metadata
    MigrationMetadata findTopByOrderByIdDesc();
}