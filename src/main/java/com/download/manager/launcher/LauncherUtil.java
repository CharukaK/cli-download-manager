package com.download.manager.launcher;

import com.download.manager.download.models.GlobalConfig;
import com.download.manager.exceptions.DownloadException;
import com.download.manager.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class LauncherUtil {
    public static void extractConfigFromArguments(String[] args, GlobalConfig config, List<List<String>> downloadList)
            throws DownloadException {

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
    }

    private static String getOptionValue(String arg) throws DownloadException {
        if (!arg.contains("=") && arg.split("=").length == 2) {
            throw new DownloadException("Invalid argument format");
        }
        return arg.split("=")[1];
    }

}
