package com.nixiedroid;

import com.nixiedroid.classloaders.CryptedClassLoader;
import com.nixiedroid.classloaders.FileClassLoader;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static com.nixiedroid.Stuff.print;

public class Main {
    public static void main(String[] args) throws Exception {
        // Stuff.printAll();
        Class<?> cl = null;
        Object obj;
        String name = "com.nixiedroid.plugins.Plugin";
        CryptedClassLoader classLoader = new CryptedClassLoader();
        cl = classLoader.loadClass(name);
        Object plugin = cl.getDeclaredConstructor().newInstance();
        print("ClassLoader of plugin is " + cl.getClassLoader().getName());

    }
    private void encrypt(){
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