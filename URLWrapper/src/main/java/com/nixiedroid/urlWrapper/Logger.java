package com.nixiedroid.urlWrapper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

public class Logger {
    private static boolean isLoggingEnabled = false;

    static {
        log = new Logger();
    }
    public static final Logger log;

    private final PrintStream logStream;

    private Logger() {
        if ("file".equals(System.getProperty("urlWrapper.log", "false"))) {
            PrintStream temp = System.out;
            try {
                temp = new PrintStream(new FileOutputStream("Wrapper.log", true), false);
                System.out.println("[URL Wrapper log init]: Found urlWrapper.log=file");
                temp.println("[URL Wrapper log init]: Found urlWrapper.log=file");
                System.out.println("[URL Wrapper log init]: Initialised logger to Wrapper.log");
                temp.println("[URL Wrapper log init]: Initialised logger to Wrapper.log (THIS file)");
            } catch (FileNotFoundException | SecurityException e) {
                temp = System.out;
                temp.println("[URL Wrapper log init]: Found urlWrapper.log=file");
                temp.println("[URL Wrapper log init]: Unable to write to Wrapper.log. Using stdout");
            } finally {
                logStream = temp;
                isLoggingEnabled = true;
            }
        } else
        if ("true".equals(System.getProperty("urlWrapper.log", "false"))) {
            logStream = System.out;
            isLoggingEnabled = true;
            logStream.println("[URL Wrapper log init]: Found urlWrapper.log=true");
            logStream.println("[URL Wrapper log init]: Initialised logger to stdout");
        } else {
            logStream = null;
            isLoggingEnabled = false;
            System.out.println("Logging is disabled. Use -DurlWrapper.log = { true , file } to override ");
        }
    }

    public void info(String text) {
        if (isLoggingEnabled) logStream.println("[URL Wrapper]: " + text);
    }
}