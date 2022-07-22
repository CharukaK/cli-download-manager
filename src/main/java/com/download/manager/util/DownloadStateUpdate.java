package com.download.manager.util;

public class DownloadStateUpdate {
    private String id;
    private DownloadState downloadState;

    public DownloadStateUpdate(String id, DownloadState downloadState) {
        this.id = id;
        this.downloadState = downloadState;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DownloadState getDownloadState() {
        return downloadState;
    }

    public void setDownloadState(DownloadState downloadState) {
        this.downloadState = downloadState;
    }
}
