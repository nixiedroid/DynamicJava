package com.nixiedroid.runtime;

public class Util {
    public static void printMemUsage(){
        System.out.println("Available heap size = " +
                (Runtime.getRuntime().freeMemory()>>20) + "MiB");
        System.out.println("Max heap size = " +
                (Runtime.getRuntime().totalMemory()>>20) + "MiB");
        System.out.println("Max jvm fat size = " +
                (Runtime.getRuntime().maxMemory()>>20) + "MiB");
    }
}
