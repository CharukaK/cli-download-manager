package com.download.manager;

import com.download.manager.download.ftp.FTPDownloadConfig;
import com.download.manager.download.http.HttpDownloadConfig;
import com.download.manager.download.sftp.SFTPConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DownloadConfigTests {
    @Test
    public void testFtpUrlWithoutPort() {
        FTPDownloadConfig downloadConfig = new FTPDownloadConfig("ftp://user:password@hello.io/test.txt");
        assertEquals("user", downloadConfig.getUserName());
        assertEquals("password", downloadConfig.getPassword());
        assertEquals("test.txt", downloadConfig.getFileName());
    }

    @Test
    public void testFtpUrlWithPort() {
        FTPDownloadConfig downloadConfig = new FTPDownloadConfig("ftp://user:password@0.0.0.0:20/test.txt");
        assertEquals("user", downloadConfig.getUserName());
        assertEquals("password", downloadConfig.getPassword());
        assertEquals("test.txt", downloadConfig.getFileName());
        assertEquals("0.0.0.0", downloadConfig.getHost());
        assertEquals("20", downloadConfig.getPort());
    }


    @Test
    public void testHttpUrl() {
        String url = "http://hello.io/test.txt";
        HttpDownloadConfig downloadConfig = new HttpDownloadConfig(url);
        assertEquals(url, downloadConfig.getUrl());
    }

    @Test
    public void testSftpUrl() {
        String url = "sftp://user:password@hello.io//test.txt";
        SFTPConfig downloadConfig = new SFTPConfig(url);
        assertEquals(url, downloadConfig.getSftpUrl());
        assertEquals("test.txt", downloadConfig.getFileName());
    }

}
