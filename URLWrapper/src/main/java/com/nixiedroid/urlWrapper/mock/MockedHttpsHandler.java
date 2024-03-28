package com.nixiedroid.urlWrapper.mock;

import com.nixiedroid.urlWrapper.URLPatcher;

import java.io.IOException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class MockedHttpsHandler extends sun.net.www.protocol.https.Handler {
    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        URL newUrl = URLPatcher.replace(u);
        return super.openConnection(Objects.requireNonNullElse(newUrl, u), p);
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        return openConnection(u, Proxy.NO_PROXY);
    }
}
