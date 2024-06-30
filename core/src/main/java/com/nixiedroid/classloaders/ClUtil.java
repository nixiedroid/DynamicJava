package com.nixiedroid.classloaders;

import java.io.ByteArrayOutputStream;

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
    public static ByteArrayOutputStream readFile(){
        return null;
    }



}
