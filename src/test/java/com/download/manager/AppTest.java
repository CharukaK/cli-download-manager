package com.download.manager;

import com.download.manager.download.GlobalConfig;
import com.download.manager.exceptions.DownloadException;
import com.download.manager.launcher.LauncherUtil;
import com.download.manager.util.Constants;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Unit test for simple App.
 */
public class AppTest {
    private final GlobalConfig config = new GlobalConfig();
    private final List<List<String>> downloadList = new ArrayList<>();

    @Test
    public void testArgumentExtractions() throws DownloadException {
        int retryCount = 2;
        int retryInterval = 5000;
        String outputDir = "./TEST";
        String firstURL = "http://hello.com/test123.txt";
        String secondURL = "ftp://hello.com/test123.txt";

        String[] args = {
                String.format("%s=%s", Constants.RETRY_COUNT_ARGUMENT_OPTION, retryCount),
                String.format("%s=%s", Constants.RETRY_INTERVAL_ARGUMENT_OPTION, retryInterval),
                String.format("%s=%s", Constants.OUTPUT_DIR_ARGUMENT_OPTION, outputDir),
                Constants.DOWNLOAD_LIST_DELIMITER,
                firstURL,
                Constants.DOWNLOAD_LIST_DELIMITER,
                secondURL
        };

        LauncherUtil.extractConfigFromArguments(args, config, downloadList);

        assertEquals(retryCount, config.getRetryCount());
        assertEquals(retryInterval, config.getRetryInterval());
        assertEquals(outputDir, config.getDownloadDir());
        assertEquals(2, downloadList.size());
        assertEquals(1, downloadList.get(0).size());
        assertEquals(firstURL, downloadList.get(0).get(0));
        assertEquals(1, downloadList.get(1).size());
        assertEquals(secondURL, downloadList.get(1).get(0));
    }
}
