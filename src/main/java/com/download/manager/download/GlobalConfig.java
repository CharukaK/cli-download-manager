package com.download.manager.download;

public class GlobalConfig {
    private int retryCount;
    private int retryInterval; // milliseconds
    private String downloadDir;

    public GlobalConfig() {
        retryCount = 3;
        retryInterval = 1000;
        downloadDir = "./";
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public int getRetryInterval() {
        return retryInterval;
    }

    public void setRetryInterval(int retryInterval) {
        this.retryInterval = retryInterval;
    }

    public String getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadDir(String downloadDir) {
        this.downloadDir = downloadDir;
    }
}
