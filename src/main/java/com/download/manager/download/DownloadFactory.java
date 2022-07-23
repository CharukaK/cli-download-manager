package com.download.manager.download;

import com.download.manager.download.ftp.FTPDownload;
import com.download.manager.download.ftp.FTPDownloadConfig;
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

        if (((String) args[0]).startsWith("ftp")) {
            System.out.println("ftp");
            FTPDownloadConfig ftpDownloadConfig = new FTPDownloadConfig((String) args[0]);
            return new FTPDownload().init(ftpDownloadConfig);
//            HttpDownloadConfig downloadConfig = new FTPDownloadConfig((String) args[0]);
//            return new HTTPDownload().init(downloadConfig);
        }


        return null;
    }
}
