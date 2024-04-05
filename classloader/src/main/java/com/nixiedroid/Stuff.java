package com.nixiedroid;

import com.nixiedroid.classloaders.DummyClassLoader;

import java.util.Arrays;

public class Stuff {
    public static void printAll () throws Exception {
        print("\nMain OK");
        Premain.listAllLoadedAppClasses();
        new DummyClassLoader()
                .loadClass("com.nixiedroid.plugins.Plugin")
                .getConstructor().newInstance();
        Premain.listAllLoadedAppClasses();
        print("");
        print("ClassLoader of Classloader is " + java.lang.ClassLoader.class.getClassLoader());
        print("Platform loaded classes:\n" +
                Arrays.toString(
                        ClassLoader.getPlatformClassLoader().getDefinedPackages()
                )
        );
        print("App loaded classes:\n" +
                Arrays.toString(
                        ClassLoader.getSystemClassLoader().getDefinedPackages()
                )

        );
    }
    public static void print(String str){
        System.out.println(str);
    }
}
