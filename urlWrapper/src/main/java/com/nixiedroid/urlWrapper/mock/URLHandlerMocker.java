package com.nixiedroid.urlWrapper.mock;

import com.nixiedroid.unsafe.UnsafeWrapper;

import java.lang.reflect.Field;
import java.net.URL;

public class URLHandlerMocker {
    private URLHandlerMocker(){
        throw new RuntimeException("Should not be run");
    }
    public static void init(){
        System.out.println(URLHandlerMocker.class.getModule());
        UnsafeWrapper.moveToJavaBase(MockedHttpHandler.class);
        UnsafeWrapper.moveToJavaBase(MockedHttpsHandler.class);
        System.out.println(MockedHttpsHandler.class.getModule());
        UnsafeWrapper.moveToJavaBase(URLHandlerMocker.class);
        UnsafeWrapper.moveToJavaBase(MockedUrlHandlerFactory.class);

        System.out.println(URLHandlerMocker.class.getModule());
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
            System.err.println("Can not find required field in URL class!");
            e.printStackTrace(System.err);
        } catch (IllegalAccessException e) {
            System.err.println("Can not access URL module");
            e.printStackTrace(System.err);
        }
    }
}
