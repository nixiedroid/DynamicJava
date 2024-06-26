package com.nixiedroid.premain;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


public class Transformer {
    public static byte[] transform(
            ClassLoader loader,
            String className,
            Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain,
            byte[] classfileBuffer) throws IllegalClassFormatException {

        System.out.println("Loading: " + className);


    }
}