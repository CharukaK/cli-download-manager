package com.download.manager.download.sftp;

import com.download.manager.download.DownloadConfig;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class SFTPConfig extends DownloadConfig {
    private final Logger logger = LoggerFactory.getLogger(SFTPConfig.class);
    private String sftpUrl;

    public SFTPConfig(String sftpUrl) {
        this.sftpUrl = sftpUrl;
        try {
            URL parsedURL = new URL(sftpUrl.substring(1));
            String fileNameSegment = URLDecoder.decode(FilenameUtils.getName(parsedURL.getFile()), StandardCharsets.UTF_8.toString());
            setFileName(fileNameSegment);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public String getSftpUrl() {
        return sftpUrl;
    }

    public void setSftpUrl(String sftpUrl) {
        this.sftpUrl = sftpUrl;
    }
}
