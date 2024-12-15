package com.example.migrator.batch.listener;

import com.example.migrator.batch.domain.UserDocument;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.item.Chunk;

public class WriterListener implements ItemWriteListener<UserDocument> {
    @Override
    public void beforeWrite(Chunk<? extends UserDocument> items) {
        System.out.println("Before Writing: " + items);
    }

    @Override
    public void afterWrite(Chunk<? extends UserDocument> items) {
        System.out.println("Written items: " + items);
    }

    @Override
    public void onWriteError(Exception ex, Chunk<? extends UserDocument> items) {
        System.err.println("Error during writing: " + ex.getMessage());
    }
}
