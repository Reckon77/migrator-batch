package com.example.migrator.batch.listener;

import com.example.migrator.batch.domain.UserDocument;
import com.example.migrator.batch.domain.UserEntity;
import org.springframework.batch.core.ItemProcessListener;

public class ProcessorListener implements ItemProcessListener<UserEntity, UserDocument> {
    @Override
    public void beforeProcess(UserEntity item) {
        System.out.println("Before Processing: " + item);
    }

    @Override
    public void afterProcess(UserEntity item, UserDocument result) {
        System.out.println("Processed item: " + result);
    }

    @Override
    public void onProcessError(UserEntity item, Exception ex) {
        System.err.println("Error during processing: " + ex.getMessage());
    }
}
