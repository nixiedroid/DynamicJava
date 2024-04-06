package com.nixiedroid;

import com.nixiedroid.classloaders.CryptedClassLoader;
import com.nixiedroid.classloaders.DummyClassLoader;
import com.nixiedroid.modules.ModuleManager;
import com.nixiedroid.premain.Handler;
import com.nixiedroid.runtime.Info;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Stuff {
    public static void printAll() throws Exception {
        print("\nMain OK");
        Handler.listAllLoadedAppClasses();
        new DummyClassLoader().loadClass("com.nixiedroid.plugins.Plugin").getConstructor().newInstance();
        Handler.listAllLoadedAppClasses();
        print("");
        print("ClassLoader of Classloader is " + java.lang.ClassLoader.class.getClassLoader());

        print("Num of App loaded packages: " + ClassLoader.getSystemClassLoader().getDefinedPackages().length);
        print("Platform loaded packages:" + Arrays.toString(ClassLoader.getPlatformClassLoader().getDefinedPackages()));
        print("ClassLoader of java.sql.Array is " + java.sql.Array.class.getClassLoader().getName());
        print("Platform loaded packages:" + Arrays.toString(ClassLoader.getPlatformClassLoader().getDefinedPackages()));
        // epicText();
    }

    public static void epicText() {
        ModuleLayer.boot().modules().stream().collect(Collectors.groupingBy(m -> Optional.ofNullable(m.getClassLoader()).map(ClassLoader::getName).orElse("boot"), Collectors.mapping(Module::getName, Collectors.toCollection(TreeSet::new)))).entrySet().stream().sorted(Comparator.comparingInt(e -> List.of("boot", "platform", "app").indexOf(e.getKey()))).map(e -> e.getKey() + "\n\t" + String.join("\n\t", e.getValue())).forEach(Stuff::print);
    }

    public static void printSystemPaths() {
        print(System.getProperty("java.class.path"));
        print(System.getProperty("jdk.module.path"));
    }

    public static void print(String str) {
        System.out.println(str);
    }

    public static void loadPlugin() {
        String name = "com.nixiedroid.plugins.Plugin";
        ClassLoader classLoader = new CryptedClassLoader();

        try {
            Class<?> cl = classLoader.loadClass(name);
            Object plugin = cl.getDeclaredConstructor().newInstance();
            print("ClassLoader of plugin is " + cl.getClassLoader().getName());
        } catch (Exception ignored){}

    }

    public static void encrypt() {
        File rawFile = new File("./Plugin.clazz");
        File encFile = new File("./Plugin.enc");
        byte[] data = null;
        try {
            data = CryptedClassLoader.encrypt(Files.readAllBytes(rawFile.toPath()));
            Files.write(encFile.toPath(), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void modulesTest() {
        String FLOAT_CONSTANTS_CLASS_NAME = "jdk.internal.math.FloatConsts";
        String THREAD_SLEEPING_EVENT_CLASS_NAME = "jdk.internal.event.ThreadSleepEvent";
        System.out.println(Info.getVersion());
        ModuleManager.poke();
        System.out.println(Main.class.getModule());
        System.out.println(ModuleManager.class.getModule());
        try {
            Class<?> cl = Class.forName(FLOAT_CONSTANTS_CLASS_NAME);
            System.out.println(cl.getModule());
            ModuleManager.allowAccess(Main.class, cl, false);
            Field field = cl.getDeclaredField("SIGNIFICAND_WIDTH");
            System.out.println(field.get(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
