package com.download.manager.download.http;

import com.download.manager.download.Download;
import com.download.manager.download.DownloadConfig;
import com.download.manager.download.DownloadManager;
import com.download.manager.exceptions.DownloadException;

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
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTTPDownload extends Download {
    private final Logger logger = Logger.getLogger(String.valueOf(HTTPDownload.class));
    private HttpDownloadConfig downloadConfig;
    private URL url;

    @Override
    public Download init(DownloadConfig config) throws DownloadException {
        this.downloadConfig = (HttpDownloadConfig) config;
        try {
            url = new URL(this.downloadConfig.getUrl());
            this.downloadConfig.setFileName(URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.toString()));
        } catch (IOException e) {
            throw new DownloadException(e.getMessage(), e);
        }

        return this;
    }

    @Override
    public void run() {
        try {
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(1000);
            httpURLConnection.setReadTimeout(1000);
            httpURLConnection.connect();
            Map<String, List<String>> fileHeaders = httpURLConnection.getHeaderFields();
            if (fileHeaders.get("Content-Disposition") != null && fileHeaders.get("Content-Disposition").size() > 0) {
                // update file name according to server response
                downloadConfig.setFileName(fileHeaders.get("Content-Disposition").get(0));
            }
            String outputFilePath = constructOutputFilePath(downloadConfig);
            File outputFile = new File(outputFilePath);
            BufferedOutputStream bufferedOutputStream = null;
            FileOutputStream fileOutputStream = null;
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
                        outputFile = new File(constructOutputFilePath(downloadConfig));
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
            BufferedInputStream bufferedInputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            byte[] buffer = new byte[8192];
            System.out.println("download started");
            int byteCount = 0;
            while ((byteCount = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, byteCount);

            }

            bufferedInputStream.close();
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            httpURLConnection.disconnect();
        } catch (IOException e) {
            downloadConfig.increaseTries();
            try {
                DownloadManager.getInstance().submitDownload(new HTTPDownload().init(downloadConfig));
            } catch (DownloadException ex) {
                // if downloadConfig exists no exception would occur
            }
//            throw new RuntimeException(e);
        }
    }

    private String constructOutputFilePath(HttpDownloadConfig downloadConfig) {
        StringBuilder outputPathBuilder = new StringBuilder();
        outputPathBuilder.append(downloadConfig.getOutputDir());
        if (downloadConfig.getFileName().startsWith("/") && downloadConfig.getOutputDir().endsWith("/")) {
            outputPathBuilder.append(downloadConfig.getFileName().substring(1));
        } else if (!downloadConfig.getOutputDir().endsWith("/") && downloadConfig.getFileName().startsWith("/")){
        } else {
            outputPathBuilder.append(downloadConfig.getFileName());
        }
        System.out.println(outputPathBuilder.toString());
        return outputPathBuilder.toString();
    }
}
