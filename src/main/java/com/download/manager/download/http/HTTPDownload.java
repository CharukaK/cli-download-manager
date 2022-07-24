package com.download.manager.download.http;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadInfo;
import com.download.manager.download.DownloadManager;
import com.download.manager.download.DownloadState;
import com.download.manager.exceptions.DownloadException;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.download.manager.download.Util.getNewFileName;

public class HTTPDownload extends Download {
    private final Logger logger = LoggerFactory.getLogger(HTTPDownload.class);
    private HttpDownloadConfig downloadConfig;
    private URL url;

    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        this.downloadConfig = (HttpDownloadConfig) config;
        logger.info("Initializing download " + downloadConfig.getUrl());
        try {
            url = new URL(this.downloadConfig.getUrl());
            String fileNameSegment = URLDecoder.decode(FilenameUtils.getName(url.getFile()), StandardCharsets.UTF_8.toString());
            this.downloadConfig.setFileName(fileNameSegment.contains("?") ?
                    fileNameSegment.split("\\?")[0] : fileNameSegment);
            getDownloadInfo().setFilePath(downloadConfig.getFullOutputFilePath());
        } catch (IOException e) {
            throw new DownloadException(e.getMessage(), e);
        }
        setId(downloadConfig.getId());
        updateState(getId(), new DownloadInfo(DownloadState.INITIALIZED, getDownloadInfo().getFilePath()));
        return this;
    }


    @Override
    public void run() {
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            updateState(getId(), new DownloadInfo(DownloadState.IN_PROGRESS, getDownloadInfo().getFilePath()));
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.connect();
            String raw = httpURLConnection.getHeaderField("Content-Disposition");
            // raw = "attachment; filename=abc.jpg"
            if (raw != null && raw.contains("=")) {
                String fileName = raw.split("=")[1]; //getting value after '='
                downloadConfig.setFileName(fileName);
                getDownloadInfo().setFilePath(downloadConfig.getFullOutputFilePath());
            }
            File outputFile = new File(getDownloadInfo().getFilePath());
            if (outputFile.exists()) {
                if (downloadConfig.getTries() == 0) {
                    // There exist a file name collision
                    outputFile = getNewFileName(outputFile);
                    downloadConfig.setFileName(outputFile.getName());
                    getDownloadInfo().setFilePath(downloadConfig.getFullOutputFilePath());
                    updateState(getId(), getDownloadInfo());
                } else {
                    httpURLConnection.setRequestProperty("Range", String.format("bytes=%s", outputFile.length()));
                }
            }

            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(outputFile));
            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            byte[] buffer = new byte[8192];
            int byteCount = 0;

            logger.info("Starting download " + downloadConfig.getUrl());
            while ((byteCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, byteCount);

            }

            logger.info("Finished download " + downloadConfig.getUrl());
            updateState(downloadConfig.getId(), new DownloadInfo(DownloadState.COMPLETED, getDownloadInfo().getFilePath()));
        } catch (IOException e) {
            logger.info("Error in downloading, " + downloadConfig.getUrl());
            downloadConfig.increaseTries();

            try {
                Thread.sleep(downloadConfig.getRetryInterval());
            } catch (InterruptedException ex) {
                logger.error(e.getMessage(), e);
            }

            try {
                if (downloadConfig.getTries() < downloadConfig.getRetryCount()) {
                    logger.info("Attempting retry for download " + downloadConfig.getUrl());
                    DownloadManager.getInstance().submitDownload(new HTTPDownload().init(downloadConfig));
                } else {
                    updateState(downloadConfig.getId(), new DownloadInfo(DownloadState.FAILED, getDownloadInfo().getFilePath()));
                }
            } catch (DownloadException ex) {
                // if downloadConfig exists at this point no exception would occur
            }
        } finally {
            try {
                Objects.requireNonNull(bufferedInputStream).close();
                Objects.requireNonNull(bufferedOutputStream).flush();
                Objects.requireNonNull(bufferedOutputStream).close();
                Objects.requireNonNull(httpURLConnection).disconnect();
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

}
