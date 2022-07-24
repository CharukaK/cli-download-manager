package com.download.manager.download.ftp;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadInfo;
import com.download.manager.download.DownloadManager;
import com.download.manager.download.DownloadState;
import com.download.manager.exceptions.DownloadException;
import org.apache.commons.net.ftp.FTPClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.download.manager.download.DownloadUtil.getNewFileName;

public class FTPDownload extends Download {
    private final Logger logger = LoggerFactory.getLogger(FTPDownload.class);
    private FTPDownloadConfig config;

    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        this.config = (FTPDownloadConfig) config;
        setId(config.getId());
        updateState(getId(), new DownloadInfo(DownloadState.INITIALIZED, ""));
        logger.info("Initialized download " + ((FTPDownloadConfig) config).getFilePath());
        return this;
    }

    @Override
    public void run() {
        FTPClient ftpClient = new FTPClient();
        FileOutputStream fileOutputStream;
        InputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            if (config.getPort().length() == 0) {
                ftpClient.connect(config.getHost());
            } else {
                ftpClient.connect(config.getHost(), Integer.parseInt(config.getPort()));
            }
            ftpClient.login(config.getUserName(), config.getPassword());
            ftpClient.enterLocalPassiveMode();
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
            }
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            bufferedInputStream = ftpClient.retrieveFileStream(URLDecoder.decode(config.getFilePath(), StandardCharsets.UTF_8.toString()));

            byte[] buffer = new byte[8192];
            int byteCount = 0;

            logger.info("Starting download " + config.getFilePath());
            while ((byteCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, byteCount);
            }

            logger.info("Finished download " + config.getFilePath());
            updateState(config.getId(), new DownloadInfo(DownloadState.COMPLETED, getDownloadInfo().getFilePath()));
        } catch (IOException e) {
            config.increaseTries();
            try {
                Thread.sleep(config.getRetryInterval());
            } catch (InterruptedException ex) {
                logger.error(e.getMessage(), e);
            }

            try {
                logger.info("Error in downloading, " + config.getFilePath());

                if (config.getTries() < config.getRetryCount()) {
                    logger.info("Attempting retry for download " + config.getFilePath());
                    DownloadManager.getInstance().submitDownload(new FTPDownload().init(config));
                } else {
                    updateState(config.getId(), new DownloadInfo(DownloadState.FAILED, getDownloadInfo().getFilePath()));
                }
            } catch (DownloadException ex) {
                // if downloadConfig exists at this point no exception would occur
            }
        } finally {
            try {
                Objects.requireNonNull(bufferedOutputStream).close();
                Objects.requireNonNull(bufferedInputStream).close();
                ftpClient.disconnect();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}
