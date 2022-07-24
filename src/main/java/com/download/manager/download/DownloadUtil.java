package com.download.manager.download;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Util class for downloads package
 */
public class DownloadUtil {

    /**
     * Method to resolve name clashes
     * @param outputFile File object with name clash
     * @return New File object with resolved name clash
     */
    public static File getNewFileName(File outputFile) {
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
            outputFile = new File(String.format("%s/%s (%s)%s", outputFile.getParentFile().toString(), base, index, ext));
            index++;
        }
        return outputFile;
    }
}
