package com.download.manager.launcher;


import java.util.Arrays;

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

        for (String arg : args) {

        }
    }
}
