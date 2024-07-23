package com.nixiedroid.classes;

import java.io.ByteArrayOutputStream;

public class Classes {
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
