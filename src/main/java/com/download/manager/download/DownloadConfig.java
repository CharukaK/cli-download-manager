package com.download.manager.download;

import com.download.manager.util.Constants;

public class DownloadConfig {
    private String url;
    private int retryCount;
    private int retryInterval;

    public DownloadConfig(String url) {
        this.url = url;
        this.retryCount = Constants.DEFAULT_RETRY_COUNT;
        this.retryInterval = Constants.DEFAULT_RETRY_INTERVAL;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
