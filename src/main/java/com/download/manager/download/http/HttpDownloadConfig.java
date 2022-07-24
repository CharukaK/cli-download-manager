package com.download.manager.download.http;

import com.download.manager.download.DownloadConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Download config for HTTP resources
 */
public class HttpDownloadConfig extends DownloadConfig {
    private final Map<String, String> headers;
    private final String url;

    public HttpDownloadConfig(String url) {
        headers = new HashMap<>();
        this.url = url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Adds a new header if it doesn't exist, if it does old value will be replaced by the new.
     *
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    public String getUrl() {
        return url;
    }
}
