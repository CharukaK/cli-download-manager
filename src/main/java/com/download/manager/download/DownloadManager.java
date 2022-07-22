package com.download.manager.download;

import com.download.manager.util.DownloadState;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.nio.channels.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DownloadManager implements PropertyChangeListener {
    private static DownloadManager downloadManager;
    private final ExecutorService executorService;
    private final Map<String, DownloadState> downloadStateMap;

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
        downloadStateMap.put(download.getId(), download.getDownloadState());
        download.addPropertyChangeListener(this);
        executorService.submit(download);
    }

    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public synchronized void propertyChange(PropertyChangeEvent propertyChangeEvent) {
        downloadStateMap.put(propertyChangeEvent.getPropertyName(), (DownloadState) propertyChangeEvent.getNewValue());
        if (downloadStateMap.values().stream().noneMatch(downloadState -> downloadState == DownloadState.IN_PROGRESS)) {
            shutdown();
        }
    }
}

