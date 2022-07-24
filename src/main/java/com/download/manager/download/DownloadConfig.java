package com.download.manager.download;

import com.download.manager.util.Constants;

import java.util.UUID;

public class DownloadConfig {
    private int retryCount;
    private int retryInterval;
    private String outputDir;
    private String fileName;
    private int tries;
    private String id;

    public DownloadConfig() {
        this.retryCount = Constants.DEFAULT_RETRY_COUNT;
        this.retryInterval = Constants.DEFAULT_RETRY_INTERVAL;
        this.outputDir = Constants.OUTPUT_DIR;
        this.tries = 0;
        this.id = UUID.randomUUID().toString();
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public int getTries() {
        return tries;
    }

    public void increaseTries() {
        this.tries++;
    }

    public String getId() {
        return id;
    }

    public String getFullOutputFilePath() {
        StringBuilder outputPathBuilder = new StringBuilder();
        outputPathBuilder.append(getOutputDir());
        if (getFileName().startsWith("/") && getOutputDir().endsWith("/")) {
            outputPathBuilder.append(getFileName().substring(1));
        } else if (!getOutputDir().endsWith("/") && !getFileName().startsWith("/")) {
            outputPathBuilder.append(String.format("/%s", outputPathBuilder.append(getFileName())));
        } else {
            outputPathBuilder.append(getFileName());
        }
        return outputPathBuilder.toString();
    }
}
