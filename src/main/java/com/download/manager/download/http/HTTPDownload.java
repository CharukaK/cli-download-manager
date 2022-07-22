package com.download.manager.download.http;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadInfo;
import com.download.manager.download.DownloadManager;
import com.download.manager.exceptions.DownloadException;
import com.download.manager.download.DownloadState;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPDownload extends Download {
    private final Logger logger = LoggerFactory.getLogger(HTTPDownload.class);
    private HttpDownloadConfig downloadConfig;
    private URL url;

    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        this.downloadConfig = (HttpDownloadConfig) config;
        setId(UUID.randomUUID().toString());
        logger.info("Initializing download " + downloadConfig.getUrl());
        try {
            url = new URL(this.downloadConfig.getUrl());
            getDownloadInfo().setFilePath(downloadConfig.getFullOutputFilePath());
        } catch (IOException e) {
            throw new DownloadException(e.getMessage(), e);
        }
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
            Map<String, List<String>> fileHeaders = httpURLConnection.getHeaderFields();
            if (fileHeaders.get("Content-Disposition") != null && fileHeaders.get("Content-Disposition").size() > 0) {
                // update file name according to server response
                downloadConfig.setFileName(fileHeaders.get("Content-Disposition").get(0));
            }
            File outputFile = new File(getDownloadInfo().getFilePath());
            FileOutputStream fileOutputStream;
            if (outputFile.exists()) {
                if (downloadConfig.getTries() == 0) {
                    // There exist a file name collision
                    Pattern pattern = Pattern.compile("^(?<base>.+?)\\s*(?:\\((?<idx>\\d+)\\))?(?<ext>\\.[\\w.]+)?$");
                    Matcher matcher = pattern.matcher(outputFile.getName());
                    String base = "";
                    String ext = "";
                    if (matcher.find()) {
                        ext = matcher.group("ext");
                        base = matcher.group("base");
                    }
                    int index = 1;
                    while (outputFile.exists()) {
                        downloadConfig.setFileName(String.format("%s (%s)%s", base, index, ext));
                        getDownloadInfo().setFilePath(downloadConfig.getFullOutputFilePath());
                        outputFile = new File(getDownloadInfo().getFilePath());
                        index++;
                    }
                } else {
                    httpURLConnection.setRequestProperty("Range", String.format("bytes=%s", outputFile.length()));
                }
                fileOutputStream = new FileOutputStream(outputFile);
            } else {
                fileOutputStream = new FileOutputStream(outputFile);
            }
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            byte[] buffer = new byte[8192];
            System.out.println("download started");
            int byteCount = 0;
            logger.info("Starting download " + downloadConfig.getUrl());
            while ((byteCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, byteCount);

            }

            logger.info("Finished download " + downloadConfig.getUrl());
            updateState(getId(), new DownloadInfo(DownloadState.COMPLETED, getDownloadInfo().getFilePath()));
        } catch (IOException e) {
            downloadConfig.increaseTries();
            try {
                logger.info("Error in downloading, " + downloadConfig.getUrl());
                if (downloadConfig.getTries() < downloadConfig.getRetryCount()) {
                    logger.info("Initializing retry, " + downloadConfig.getUrl());
                    DownloadManager.getInstance().submitDownload(new HTTPDownload().init(downloadConfig));
                } else {
                    updateState(getId(), new DownloadInfo(DownloadState.FAILED, getDownloadInfo().getFilePath()));
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
