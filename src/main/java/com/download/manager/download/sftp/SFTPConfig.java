package com.download.manager.download.sftp;

import com.download.manager.download.models.DownloadConfig;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

/**
 * Class containing SFTP download configuration
 */
public class SFTPConfig extends DownloadConfig {
    private final Logger logger = LoggerFactory.getLogger(SFTPConfig.class);
    private String sftpUrl;

    public SFTPConfig(String sftpUrl) {
        this.sftpUrl = sftpUrl;
        try {
            URI parsedURL = new URI(sftpUrl.substring(1));
            String fileNameSegment = URLDecoder.decode(FilenameUtils.getName(parsedURL.getPath()), StandardCharsets.UTF_8.toString());
            setFileName(fileNameSegment);
        } catch (UnsupportedEncodingException | URISyntaxException e) {
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
