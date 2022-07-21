package com.download.manager.download.ftp;

import com.download.manager.download.DownloadConfig;

public class FTPDownloadConfig extends DownloadConfig {
    private String userName;
    private String password;
    private String port;
    private String host;
    private String filePath;

    public FTPDownloadConfig(String userName, String password, String port, String host, String filePath) {
        this.userName = userName;
        this.password = password;
        this.port = port;
        this.host = host;
        this.filePath = filePath;
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
