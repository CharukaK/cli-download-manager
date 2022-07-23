package com.download.manager.download.ftp;

import com.download.manager.download.DownloadConfig;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class FTPDownloadConfig extends DownloadConfig {
    private static final Logger logger = LoggerFactory.getLogger(FTPDownloadConfig.class);
    private String userName;
    private String password;
    private String port;
    private String host;
    private String filePath;

    public FTPDownloadConfig(String ftpUrl) {
        try {
            URL parsedURL = new URL(ftpUrl);
            this.userName = parsedURL.getUserInfo().contains(":") ?
                    parsedURL.getUserInfo().split(":")[0] : parsedURL.getUserInfo();
            this.password = parsedURL.getUserInfo().contains(":") ?
                    parsedURL.getUserInfo().split(":")[1] : "";

            this.port = parsedURL.getPort() == -1 ? "" : String.valueOf(parsedURL.getPort());
            this.host = parsedURL.getHost();

            String fileNameSegment = URLDecoder.decode(FilenameUtils.getName(parsedURL.getFile()), StandardCharsets.UTF_8.toString());
            setFileName(fileNameSegment.contains("?") ?
                    fileNameSegment.split("\\?")[0] : fileNameSegment);
            this.filePath = parsedURL.getPath();

        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
