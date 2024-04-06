package com.nixiedroid;

import com.nixiedroid.classloaders.CryptedClassLoader;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;

import static com.nixiedroid.Stuff.print;

public class Main {
    public static void main(String[] args) throws Exception {
        //Stuff.printSystemPaths();
        loadPlugin();


    }
    private static void loadPlugin(){
        String name = "com.nixiedroid.plugins.Plugin";
        ClassLoader classLoader = new CryptedClassLoader();

        try {
            Class<?> cl = classLoader.loadClass(name);
            Object plugin = cl.getDeclaredConstructor().newInstance();
            print("ClassLoader of plugin is " + cl.getClassLoader().getName());
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
    private static   void encrypt(){
        File rawFile = new File("./Plugin.clazz");
        File encFile = new File("./Plugin.enc");
        byte[] data = null;
        try {
            data = CryptedClassLoader.encrypt(Files.readAllBytes(rawFile.toPath()));
            Files.write(encFile.toPath(),data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}