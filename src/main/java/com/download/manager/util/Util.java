package com.download.manager.util;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

public class Util {
    public static Options getCLIOptionConfig() {
        Options options = new Options();
        Option help = new Option("help", "display help information");
        return options;
    }
}
