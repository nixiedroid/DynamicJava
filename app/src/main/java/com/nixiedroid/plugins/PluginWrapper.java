package com.nixiedroid.plugins;

import java.lang.reflect.Method;

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
            return loadedClass.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.err.println("No Suitable Class Found :(");
            return null;
        }
    }
    public static void executeMethod(Object plugin, String methodName){
       // Method
    }
}
