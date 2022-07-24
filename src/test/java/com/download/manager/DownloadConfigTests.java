package com.download.manager;

import com.download.manager.download.ftp.FTPDownloadConfig;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DownloadConfigTests {
    @Test
    public void testFtpUrl() {
        FTPDownloadConfig downloadConfig = new FTPDownloadConfig("ftp://user:password@hello.io/test.txt");
        assertEquals("user", downloadConfig.getUserName());
        assertEquals("password", downloadConfig.getPassword());
        assertEquals("test.txt", downloadConfig.getFileName());
    }
}
