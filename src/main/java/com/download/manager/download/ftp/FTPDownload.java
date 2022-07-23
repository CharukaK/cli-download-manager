package com.download.manager.download.ftp;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadInfo;
import com.download.manager.download.DownloadState;
import com.download.manager.exceptions.DownloadException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static com.download.manager.download.Util.getNewFileName;

public class FTPDownload extends Download {
    private FTPDownloadConfig config;

    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        this.config = (FTPDownloadConfig) config;
        setId(config.getId());
        updateState(getId(), new DownloadInfo(DownloadState.INITIALIZED, ""));
        return this;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fileOutputStream;
        BufferedInputStream bufferedInputStream;
        BufferedOutputStream bufferedOutputStream;

        try {
            if (config.getPort().length() == 0) {
                ftpClient.connect(config.getHost());
            } else {
                ftpClient.connect(config.getHost(), Integer.parseInt(config.getPort()));
            }
            ftpClient.login(config.getUserName(), config.getPassword());
            ftpClient.enterLocalPassiveMode();
            String fileNameSegment = URLDecoder.decode(FilenameUtils.getName(config.getFileName()), StandardCharsets.UTF_8.toString());
            config.setFileName(fileNameSegment.contains("?") ?
                    fileNameSegment.split("\\?")[0] : fileNameSegment);
            File outputFile = new File(config.getFullOutputFilePath());
            if (outputFile.exists()) {
                if (config.getTries() == 0) {
                    // There exist a file name collision
                    outputFile = getNewFileName(outputFile);
                    config.setFileName(outputFile.getName());
                    getDownloadInfo().setFilePath(config.getFullOutputFilePath());
                    updateState(getId(), getDownloadInfo());
                } else {
                    ftpClient.setRestartOffset(outputFile.length());
                }
                fileOutputStream = new FileOutputStream(outputFile);
            } else {
                fileOutputStream = new FileOutputStream(outputFile);
            }
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
