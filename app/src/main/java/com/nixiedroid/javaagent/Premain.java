package com.nixiedroid.javaagent;

import java.lang.instrument.Instrumentation;

@SuppressWarnings("unused")
public class Premain {
    private static volatile Instrumentation globalInstrumentation;
    private static volatile boolean isPremainAvailable = false;

    public static void premain(final String agentArgs, final Instrumentation inst) {
        globalInstrumentation = inst;
        isPremainAvailable = true;
        listAllLoadedAppClasses();
    }
    private static void replaceClassloader(){

    }

    public static void listAllLoadedClasses() {
        for (Class cl : globalInstrumentation.getAllLoadedClasses()) {
            System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader());
        }
    }

    public static void listAllLoadedAppClasses() {
        for (Class cl : globalInstrumentation.getAllLoadedClasses()) {
            if (cl.getClassLoader() != null) {
                System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader());
            }
        }
    }


    public static boolean isPremainAvailable() {
        return isPremainAvailable;
    }

    public static long sizeOf(final Object object) throws IllegalStateException {
        if (isPremainAvailable) {
            return globalInstrumentation.getObjectSize(object);
        }
        throw new IllegalStateException("PremainNotAvailable");
    }

}
