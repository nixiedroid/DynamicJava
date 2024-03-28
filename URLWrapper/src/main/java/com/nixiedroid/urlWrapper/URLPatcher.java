package com.nixiedroid.urlWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class URLPatcher
{
    private static final List<Pattern> patterns = new ArrayList<>();

    private URLPatcher() {
    }

    private static void fillPattern(String... config) {
        if (config.length % 2 != 0) return;
        for (int i = 0; i < config.length; i += 2) {
            patterns.add(new Pattern(config[i], config[i + 1]));
        }
    }
    public static void load(String filePath){
        load(new File(filePath));
        Logger.log.info("Loaded "+ patterns.size() + " directions");
    }

    public static void load(File configFile) {
        if (configFile.exists()) {
            Logger.log.info("Loading config file: " + configFile.getAbsolutePath());
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(configFile.toPath())))) {
                String[] configStrings = reader.lines()
                        .map(String::trim)
                        .filter(line -> line.length() > 2 && line.charAt(0) != '#')
                        .toArray(String[]::new);
                fillPattern(configStrings);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Logger.log.info("Config file not found: " + configFile.getAbsolutePath());
        }
    }

    public static URL replace(URL url) {
        String urlString = url.toString();
        try {
            for (Pattern p : patterns) {
                if (urlString.startsWith(p.source)) {
                    return new URL(p.destination + urlString.substring(p.source.length()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static class Pattern {
        final String source;
        final String destination;

        public Pattern(String source, String destination) {
            this.source = source;
            this.destination = destination;
        }
    }
}
