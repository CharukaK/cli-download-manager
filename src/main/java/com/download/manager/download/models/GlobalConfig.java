package com.download.manager.download.models;

import com.download.manager.util.Constants;

/**
 * Class to hold configurations that is common to all downloads.
 */
public class GlobalConfig {
    private int retryCount;
    private int retryInterval; // milliseconds
    private String downloadDir;

    public GlobalConfig() {
        retryCount = Constants.DEFAULT_RETRY_COUNT;
        retryInterval = Constants.DEFAULT_RETRY_INTERVAL;
        downloadDir = Constants.DEFAULT_OUTPUT_DIR;
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
