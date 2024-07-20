package com.nixiedroid.urlMocker;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Logger {
    private static boolean isLoggingEnabled = false;

    static {
        log = new Logger();
    }
    public static final String TAG = "[URL Mocker log init]: ";
    public static final Logger log;

    private final PrintStream logStream;

    private Logger() {
        if ("file".equals(System.getProperty("urlMocker.log", "false"))) {
            PrintStream temp = System.out;
            try {
                temp = new PrintStream(new FileOutputStream("Mocker.log", true), false);
                System.out.println(TAG + "Found urlMocker.log=file");
                temp.println(TAG + "Found urlMocker.log=file");
                System.out.println(TAG + "Initialised logger to Mocker.log");
                temp.println(TAG + "Initialised logger to Mocker.log (THIS file)");
            } catch (FileNotFoundException | SecurityException e) {
                temp = System.out;
                temp.println(TAG + "Found urlMocker.log=file");
                temp.println(TAG + "Unable to write to Mocker.log. Using stdout");
            } finally {
                logStream = temp;
                isLoggingEnabled = true;
            }
        } else
        if ("true".equals(System.getProperty("urlMocker.log", "false"))) {
            logStream = System.out;
            isLoggingEnabled = true;
            logStream.println(TAG + "Found urlMocker.log=true");
            logStream.println(TAG + "Initialised logger to stdout");
        } else {
            logStream = null;
            isLoggingEnabled = false;
            System.out.println(TAG + "Logging is disabled. Use -DurlMocker.log = { true , file } to override ");
        }
    }

    public void info(String text) {
        if (isLoggingEnabled) logStream.println("[URL Mocker]: " + text);
    }
}