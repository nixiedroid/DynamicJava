package com.nixiedroid;

import com.nixiedroid.plugins.PluginWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Main {

    public static void main(String[] args) {
        //Object plugin = PluginWrapper.loadPluginFromFile("Plugin");
       // PluginWrapper.loadPluginFromCP("com.nixiedroid.plugins.Plugin");
        Class<?> cl = null;
        System.out.println(ClassLoader.getSystemResource("java/lang/Class.class"));
        char[] intToBase64 = null;
        try {
            cl = Class.forName("java.util.prefs.Base64",
                    false,ClassLoader.getSystemClassLoader());
            Field f = cl.getDeclaredField("intToBase64");
            f.setAccessible(true);
            intToBase64 = (char[]) f.get(null);
            System.out.println(intToBase64);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}