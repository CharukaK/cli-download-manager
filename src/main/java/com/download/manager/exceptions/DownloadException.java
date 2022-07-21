package com.download.manager.exceptions;

public class DownloadException extends Exception {
    private String errorMessage;

    public DownloadException(String message) {
        super(message);
        this.errorMessage = message;
    }

    public DownloadException(String message, Throwable cause) {
        super(message, cause);
        this.errorMessage = message;
    }

    public DownloadException(Throwable cause) {
        super(cause);
    }

    public DownloadException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorMessage = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
