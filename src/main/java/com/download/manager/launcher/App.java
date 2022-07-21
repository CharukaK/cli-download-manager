package com.download.manager.launcher;


import com.download.manager.download.DownloadManager;
import com.download.manager.download.http.HTTPDownload;
import com.download.manager.download.http.HttpDownloadConfig;
import com.download.manager.exceptions.DownloadException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class would be the entry point for the application and contains the main method.
 */
public class App {
    /**
     * Entry point for the app.
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            HttpDownloadConfig httpConfig = new HttpDownloadConfig("http://0.0.0.0:8080/one-piece.mkv");
            DownloadManager.getInstance().submitDownload(new HTTPDownload().init(httpConfig));
            DownloadManager.getInstance().shutdown();
        } catch (DownloadException e) {
            throw new RuntimeException(e);
        }
    }

    static void downloadUsingFTP() {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect("0.0.0.0", 21);
            ftpClient.login("user", "123");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String filePath = "/one-piece.mkv";
            ftpClient.setRestartOffset(0);// restart byte position
            File file = new File(URLDecoder.decode("%5BSubsPlease%5D%20Kanojo%2C%20Okarishimasu%20-%2015%20%28720p%29%20%5BC7804134%5D.mkv", StandardCharsets.UTF_8.toString()));
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            byte[] buffer = new byte[8192];
            InputStream bufferedInputStream = ftpClient.retrieveFileStream(URLDecoder.decode(filePath, StandardCharsets.UTF_8.toString()));
            int bytesRead = 0;

            while ((bytesRead = bufferedInputStream.read(buffer)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }

            boolean success = ftpClient.completePendingCommand();
            if (success) {
                System.out.println("File #2 has been downloaded successfully.");
            }
            bufferedOutputStream.close();
            bufferedInputStream.close();
            ftpClient.disconnect();
            // check if mlistFile() method is supported else list files in the path and get the name
// retrieve file size
// ftpClient.getSize(URLDecoder.decode("[SubsPlease] One Piece - 1025 (720p) [60BD17DF].mkv", StandardCharsets.UTF_8.toString()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void downloadUsingHTTP() {

        try {
            URL url = new URL("http://0.0.0.0:8080/%5bSubsPlease%5d%20Kanojo,%20Okarishimasu%20-%2015%20(720p)%20%5bC7804134%5d.mkv");
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("User-Agent", "Charuka");
            String fileName = URLDecoder.decode(url.getFile(), StandardCharsets.UTF_8.toString());
            httpURLConnection.connect();
            Map<String, List<String>> m = httpURLConnection.getHeaderFields();
            if (m.get("Content-Disposition") != null && m.get("Content-Disposition").size() > 0) {
                //do stuff
                fileName = m.get("Content-Disposition").get(0);
            }
            File outputFile = new File(String.format(".%S", fileName));
            BufferedOutputStream bufferedOutputStream = null;
            FileOutputStream fileOutputStream = null;
            if (outputFile.exists()) {
                // to do stuff with resuming

            } else {
                fileOutputStream = new FileOutputStream(outputFile);
                bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            }
            BufferedInputStream inputStream = new BufferedInputStream(httpURLConnection.getInputStream());
            byte[] buffer = new byte[8192];

            int byteCount = 0;
            while ((byteCount = inputStream.read(buffer)) != -1) {
                assert bufferedOutputStream != null;
                bufferedOutputStream.write(buffer, 0, byteCount);
            }

            inputStream.close();
            assert bufferedOutputStream != null;
            bufferedOutputStream.flush();
            bufferedOutputStream.close();


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
