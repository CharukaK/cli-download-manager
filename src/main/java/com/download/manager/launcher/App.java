package com.download.manager.launcher;


import com.download.manager.download.Download;
import com.download.manager.download.DownloadFactory;
import com.download.manager.download.DownloadManager;
import com.download.manager.download.GlobalConfig;
import com.download.manager.exceptions.DownloadException;
import com.download.manager.util.Constants;
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
    public static void main(String[] args) throws DownloadException {
        GlobalConfig config = new GlobalConfig();
        List<List<String>> downloadList = new ArrayList<>();
        int index = -1;
        for (String arg : args) {
            if (arg.startsWith(Constants.RETRY_COUNT_ARGUMENT_OPTION)) {
                config.setRetryCount(Integer.parseInt(getOptionValue(arg)));
            } else if (arg.startsWith(Constants.RETRY_INTERVAL_ARGUMENT_OPTION)) {
                config.setRetryInterval(Integer.parseInt(getOptionValue(arg)));
            } else if (arg.startsWith(Constants.OUTPUT_DIR_ARGUMENT_OPTION)) {
                config.setDownloadDir(getOptionValue(arg));
            } else if (arg.equals(Constants.DOWNLOAD_LIST_DELIMITER)) {
                index++;
                downloadList.add(new ArrayList<>());
            } else {
                downloadList.get(index).add(arg);
            }
        }

        downloadList.forEach(listItem -> {
            Download download;
            try {
                download = DownloadFactory.generateDownloadRunnable(listItem.toArray(), config);
                if (download != null) {
                    DownloadManager.getInstance().submitDownload(download);
                }
            } catch (DownloadException e) {
                logger.error(e.getErrorMessage(), e);
            }
        });
    }

    private static String getOptionValue(String arg) throws DownloadException {
        if (!arg.contains("=") && arg.split("=").length == 2) {
            throw new DownloadException("Invalid argument format");
        }
        String value = arg.split("=")[1];
        return value;
    }
}
