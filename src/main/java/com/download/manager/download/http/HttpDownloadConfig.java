package com.download.manager.download.http;

import com.download.manager.download.DownloadConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Download config for HTTP resources
 */
public class HttpDownloadConfig extends DownloadConfig {
    private Map<String, String> headers;

    public HttpDownloadConfig(String url) {
        super(url);
        headers = new HashMap<>();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * Adds a new header if it doesn't exist, if it does old value will be replaced by the new.
     * @param key
     * @param value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }
}
