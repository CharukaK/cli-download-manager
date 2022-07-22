package com.download.manager.download;

public class DownloadInfo {
    private DownloadState state;
    private String filePath;

    public DownloadInfo(DownloadState state, String fileName) {
        this.state = state;
        this.filePath = fileName;
    }

    public DownloadState getState() {
        return state;
    }

    public void setState(DownloadState state) {
        this.state = state;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
