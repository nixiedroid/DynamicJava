package com.nixiedroid.premainImpl;

import com.nixiedroid.premain.PremainHandler;

import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public class Handler extends PremainHandler {
    public static void listAllLoadedClasses() {
        for (Class<?> cl : instrumentation.getAllLoadedClasses()) {
            System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader());
        }
    }

    public static void listAllLoadedAppClasses() {
        for (Class<?> cl : instrumentation.getAllLoadedClasses()) {
            if (cl.getClassLoader() != null) {
                System.out.println(cl.getName() + " is loaded via " + cl.getClassLoader().getClass().getSimpleName());
            }
        }
    }

    @Override
    public void handle(String args, Instrumentation inst) {
        System.out.println("Premain OK");
        System.out.println(System.getProperty("sun.java.command"));
//        new Thread(()->
//                JOptionPane.showMessageDialog(null, "My Goodness, this is so concise")
//        ).start();
        super.handle(args,inst);
    }

    @Override
    public byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {

        System.out.println("Loading: " + className);
        return super.transform(
                loader,
                className,
                classBeingRedefined,
                protectionDomain,
                classfileBuffer);
    }
}
