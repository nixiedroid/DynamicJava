package com.nixiedroid.classloaders;

public class ClUtil {
    public static String getClassLoaderOf(Class cl) {
        String clLdrName;
        if (cl.getClassLoader() == null) {
            clLdrName = "BootstrapClassLoader";
        } else {
            clLdrName = cl.getClassLoader().getClass().getSimpleName();
        }
        return "Classloader of " + cl.getName() + " : " + clLdrName;
    }
}
