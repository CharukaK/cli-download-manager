package com.download.manager.download;

import com.download.manager.download.http.HTTPDownload;
import com.download.manager.download.http.HttpDownloadConfig;
import com.download.manager.exceptions.DownloadException;

import java.util.Arrays;

public class DownloadFactory {
    public static Download generateDownloadRunnable(Object[] args) throws DownloadException {
        if (((String) args[0]).startsWith("http")) {
            HttpDownloadConfig downloadConfig = new HttpDownloadConfig((String) args[0]);
            return new HTTPDownload().init(downloadConfig);
        }

        return null;
    }
}
