package com.download.manager.launcher;


import com.download.manager.download.Download;
import com.download.manager.download.DownloadFactory;
import com.download.manager.download.DownloadManager;
import com.download.manager.exceptions.DownloadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * This class would be the entry point for the application and contains the main method.
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    /**
     * Entry point for the app.
     *
     * @param args
     */
    public static void main(String[] args) {
        List<List<String>> downloadList = new ArrayList<>();
        int index = -1;
        for (String arg : args) {
            if (arg.equals("-a")) {
                index++;
                downloadList.add(new ArrayList<>());
            } else {
                downloadList.get(index).add(arg);
            }
        }

        downloadList.forEach(listItem -> {
            Download download = null;
            try {
                download = DownloadFactory.generateDownloadRunnable(listItem.toArray());
                if (download != null) {
                    DownloadManager.getInstance().submitDownload(download);
                }
            } catch (DownloadException e) {
                logger.error(e.getErrorMessage(), e);
            }
        });
    }
}
