package com.download.manager.launcher;


import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
        Arrays.stream(args).forEach(System.out::println);
    }

    static void downloadUsingFTP() {
        FTPClient ftpClient = new FTPClient();

        try {
            ftpClient.connect("0.0.0.0", 21);
            ftpClient.login("user", "123");
            ftpClient.enterLocalPassiveMode();
            ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
            String filePath = "/%5BSubsPlease%5D%20Kanojo%2C%20Okarishimasu%20-%2015%20%28720p%29%20%5BC7804134%5D.mkv";
            ftpClient.listFiles()
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
