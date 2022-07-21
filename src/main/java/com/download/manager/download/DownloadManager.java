package com.download.manager.download;

import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager {
    private static  DownloadManager downloadManager;
    private final ExecutorService executorService;

    private DownloadManager() {
        executorService = Executors.newFixedThreadPool(10);
    }

    public static DownloadManager getInstance() {
        if (downloadManager != null) {
            return downloadManager;
        } else {
            downloadManager = new DownloadManager();
            return  downloadManager;
        }
    }

    public void submitDownload(Download download) {
        executorService.submit(download);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}

