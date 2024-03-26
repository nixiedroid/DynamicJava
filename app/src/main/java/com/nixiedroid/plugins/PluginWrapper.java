package com.nixiedroid.plugins;

import java.lang.reflect.Method;

public class PluginWrapper {
    public static Object loadPluginFromFile(String fileName) {
        return loadUsingClassloader(fileName,ClassLoader.getSystemClassLoader());
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
