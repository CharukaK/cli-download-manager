package com.download.manager.download.ftp;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.exceptions.DownloadException;

public class FTPDownload extends Download {
    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        return this;
    }

    @Override
    public void run() {

    }
}
