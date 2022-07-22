package com.download.manager.download;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager implements PropertyChangeListener {
    private static DownloadManager downloadManager;
    private final ExecutorService executorService;
    private final Map<String, DownloadInfo> downloadStateMap;

    private DownloadManager() {
        executorService = Executors.newFixedThreadPool(10);
        downloadStateMap = new HashMap<>();
    }

    public static synchronized DownloadManager getInstance() {
        if (downloadManager != null) {
            return downloadManager;
        } else {
            downloadManager = new DownloadManager();
            return downloadManager;
        }
    }

    public void submitDownload(Download download) {
        downloadStateMap.put(download.getId(), download.getDownloadInfo());
        download.addPropertyChangeListener(this);
        executorService.submit(download);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        downloadStateMap.put(propertyChangeEvent.getPropertyName(), (DownloadInfo) propertyChangeEvent.getNewValue());
        if (downloadStateMap.values()
                .stream().noneMatch(downloadInfo ->
                        downloadInfo.getState() == DownloadState.IN_PROGRESS
                                || downloadInfo.getState() == DownloadState.INITIALIZED)) {

            // Cleans up failed downloads
            downloadStateMap.values().stream()
                    .filter(downloadInfo -> downloadInfo.getState() == DownloadState.FAILED)
                    .forEach(downloadInfo -> {
                        this.executorService.submit(new FileRemover(downloadInfo.getFilePath()));
                    });

            // gracefully shutdown executor service
            shutdown();
        }
    }
}

