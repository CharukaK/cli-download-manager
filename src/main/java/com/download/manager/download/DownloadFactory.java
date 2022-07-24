package com.download.manager.download;

import com.download.manager.download.ftp.FTPDownload;
import com.download.manager.download.ftp.FTPDownloadConfig;
import com.download.manager.download.http.HTTPDownload;
import com.download.manager.download.http.HttpDownloadConfig;
import com.download.manager.download.sftp.SFTPConfig;
import com.download.manager.download.sftp.SFTPDownload;
import com.download.manager.exceptions.DownloadException;

public class DownloadFactory {
    public static Download generateDownloadRunnable(Object[] args) throws DownloadException {
        if (((String) args[0]).startsWith("http")) {
            HttpDownloadConfig downloadConfig = new HttpDownloadConfig((String) args[0]);
            return new HTTPDownload().init(downloadConfig);
        }

        if (((String) args[0]).startsWith("ftp")) {
            FTPDownloadConfig ftpDownloadConfig = new FTPDownloadConfig((String) args[0]);
            return new FTPDownload().init(ftpDownloadConfig);
        }

        if (((String) args[0]).startsWith("sftp")) {
            SFTPConfig sftpConfig = new SFTPConfig((String) args[0]);
            return new SFTPDownload().init(sftpConfig);
        }

        return null;
    }
}
