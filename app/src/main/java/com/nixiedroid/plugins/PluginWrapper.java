package com.nixiedroid.plugins;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PluginWrapper {
    public static Object loadPluginFromFile(String fileName) {
        return loadUsingClassloader(fileName,ClassLoader.getSystemClassLoader());
    }
    public static Object loadPluginFromCP(String className) {
        return loadUsingClassloader(className,ClassLoader.getSystemClassLoader());
    }
    public static Class<?> getClassObjectFromCP(String className) {
        return getClassObject(className,ClassLoader.getSystemClassLoader());
    }

    private static Class<?> getClassObject(String className, ClassLoader loader){
        try  {
            return Class.forName(className,false,loader);
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("No Suitable Class Found :(");
            return null;
        }
    }
    private static Object loadUsingClassloader(String className, ClassLoader loader) {
        try  {
            Class<?> loadedClass = Class.forName(className,false,loader);
            Constructor<?> c = loadedClass.getDeclaredConstructor();
            return c.newInstance();
        } catch (ClassNotFoundException |
                 InstantiationException |
                 IllegalAccessException |
                 InvocationTargetException |
                 NoSuchMethodException e
        ) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("No Suitable Class Found :(");
            return null;
        }
    }
}
