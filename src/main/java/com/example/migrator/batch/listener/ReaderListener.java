package com.example.migrator.batch.listener;

import com.example.migrator.batch.domain.UserEntity;
import org.springframework.batch.core.ItemReadListener;

public class ReaderListener implements ItemReadListener<UserEntity> {
    @Override
    public void beforeRead() {
        System.out.println("Before Read");
    }

    @Override
    public void afterRead(UserEntity item) {
        System.out.println("Read item: " + item);
    }

    @Override
    public void onReadError(Exception ex) {
        System.err.println("Error during read: " + ex.getMessage());
    }
}
