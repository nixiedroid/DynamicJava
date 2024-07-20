package com.nixiedroid.urlMocker.mock;

import com.nixiedroid.urlMocker.Logger;

import java.lang.reflect.Field;
import java.net.URL;

public final class URLHandlerMocker {
    private URLHandlerMocker(){
        throw new RuntimeException("Should not be run");
    }
    public static void init(){
        unsetURLStreamHandlerFactory();
        URL.setURLStreamHandlerFactory(new MockedUrlHandlerFactory());
    }

    private static void unsetURLStreamHandlerFactory() {
        try {
            Field field = URL.class.getDeclaredField("factory");
            field.setAccessible(true);
            field.set(null, null);
            field.setAccessible(false);
            URL.setURLStreamHandlerFactory(null);
        } catch (NoSuchFieldException e) {
            Logger.log.info("Can not find required field in URL class!");
            e.printStackTrace(System.err);
        } catch (IllegalAccessException e) {
            Logger.log.info("Can not access URL module");
            e.printStackTrace(System.err);
        }
    }
}
