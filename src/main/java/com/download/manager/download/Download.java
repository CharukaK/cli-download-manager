package com.download.manager.download;

import com.download.manager.exceptions.DownloadException;

import java.util.Map;

public abstract class Download implements Runnable {
    public abstract Download init(DownloadConfig config) throws DownloadException;
}
