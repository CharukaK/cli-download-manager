package com.download.manager.download;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class FileRemover implements Runnable {
    private static final Logger logger = LoggerFactory.getLogger(FileRemover.class);
    private final String filePath;

    public FileRemover(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void run() {
        File failedFile = new File(this.filePath);
        if (failedFile.delete()) {
            logger.info(String.format("Deleted incomplete download file %s", filePath));
        } else {
            logger.info(String.format("Failed to delete incomplete download file %s", filePath));
        }
    }
}
