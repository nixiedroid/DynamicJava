package com.nixiedroid.javaagent;

import java.lang.instrument.Instrumentation;

@SuppressWarnings("unused")
public class Premain {
    private static volatile Instrumentation globalInstrumentation;
    private static volatile boolean isPremainAvailable = false;

    public static void premain(final String agentArgs, final Instrumentation inst) {
        System.out.println("Premain OK");
        globalInstrumentation = inst;
        isPremainAvailable = true;
        listAllLoadedAppClasses();
    }
    private static void replaceClassloader() {

    }


    public static long sizeOf(final Object object) throws IllegalStateException {
        if (isPremainAvailable) {
            return globalInstrumentation.getObjectSize(object);
        }
        throw new IllegalStateException("PremainNotAvailable");
    }

    public static void listAllLoadedClasses() {
        for (Class cl : globalInstrumentation.getAllLoadedClasses()) {
            System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader());
        }
    }

    public static void listAllLoadedAppClasses() {
        for (Class cl : globalInstrumentation.getAllLoadedClasses()) {
            if (cl.getClassLoader() != null) {
                System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader().getClass().getSimpleName());
            }
        }
    }
}
