package com.nixiedroid.urlWrapper.mock;

import java.net.URLStreamHandler;
import java.net.URLStreamHandlerFactory;

public class MockedUrlHandlerFactory implements URLStreamHandlerFactory {
    private static final String PREFIX = "sun.net.www.protocol.";
    public URLStreamHandler createURLStreamHandler(String protocol) {
        if (protocol.equals("http")) return new MockedHttpHandler();
        if (protocol.equals("https")) return new MockedHttpsHandler();
        String name = PREFIX + protocol + ".Handler";
        System.out.println(protocol);
        try {
            Object o = Class.forName(name).getDeclaredConstructor().newInstance();
            System.out.println(protocol);
            return (URLStreamHandler) o;
        } catch (Exception ignored) {
        }
        return null;
    }
}