package com.nixiedroid.premain;

import java.lang.instrument.Instrumentation;

public class Handler implements PremainHandler {
    private static volatile Instrumentation globalInstrumentation;

    public static void listAllLoadedClasses() {
        for (Class<?> cl : globalInstrumentation.getAllLoadedClasses()) {
            System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader());
        }
    }

    public static void listAllLoadedAppClasses() {
        for (Class<?> cl : globalInstrumentation.getAllLoadedClasses()) {
            if (cl.getClassLoader() != null) {
                System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader().getClass().getSimpleName());
            }
        }
    }

    @Override
    public void handle(String args, Instrumentation inst) {
        globalInstrumentation = inst;
        System.out.println("Premain OK");
        listAllLoadedAppClasses();
    }
}
