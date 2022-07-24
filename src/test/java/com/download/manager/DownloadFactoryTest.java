package com.download.manager;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadFactory;
import com.download.manager.download.ftp.FTPDownload;
import com.download.manager.download.http.HTTPDownload;
import com.download.manager.download.models.GlobalConfig;
import com.download.manager.download.sftp.SFTPDownload;
import com.download.manager.exceptions.DownloadException;
import com.download.manager.util.Constants;
import org.junit.Assert;
import org.junit.Test;

public class DownloadFactoryTest {
    @Test
    public void testHttpClientGeneration() throws DownloadException {
        String url = "http://hello.com/test123.txt";
        String[] args = {
                url
        };
        Download download = DownloadFactory.generateDownloadRunnable(args, new GlobalConfig());
        Assert.assertTrue(download instanceof HTTPDownload);
    }

    @Test
    public void testFtpClientGeneration() throws DownloadException {
        String url = "ftp://user:password@hello.com/test123.txt";

        String[] args = {
                url
        };
        Download download = DownloadFactory.generateDownloadRunnable(args, new GlobalConfig());
        Assert.assertTrue(download instanceof FTPDownload);
    }

    @Test
    public void testSftpClientGeneration() throws DownloadException {
        String url = "sftp://user:password@hello.com//test123.txt";

        String[] args = {
                url
        };
        Download download = DownloadFactory.generateDownloadRunnable(args, new GlobalConfig());
        Assert.assertTrue(download instanceof SFTPDownload);
    }
}
