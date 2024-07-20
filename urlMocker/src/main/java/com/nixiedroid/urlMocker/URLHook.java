package com.nixiedroid.urlMocker;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

@SuppressWarnings("unused")
public class URLHook {
    private static boolean skip = false;

    static {
        URLPatcher.load("URLMocker/URLMocker.urls");
    }
    public static URLConnection openConnection(URL url, Proxy proxy) {
        if (skip) {
            return null;
        }
        try {
            skip = true;
            return openActualConnection(url, proxy);
        } catch (Throwable t) {
            Logger.log.info(" ERROR:");
            Logger.log.info(t.toString());
            return null;
        } finally {
            skip = false;
        }
    }

    private static URLConnection openActualConnection(URL url, Proxy proxy) throws IOException {
        Logger.log.info("Connecting to: " + url);
        if ("false".equals(System.getProperty("urlMocker.doRedirect", "false"))) {
            return url.openConnection(proxy);
        }
        URL newUrl = URLPatcher.replace(url);
        if (newUrl != null) {
            Logger.log.info("->>Redirect: " +newUrl);
            return newUrl.openConnection(proxy);
        }
        Logger.log.info("->>Skip");
        return url.openConnection(proxy);

    }
}
