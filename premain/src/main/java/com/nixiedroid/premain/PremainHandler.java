package com.nixiedroid.premain;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;

public abstract class PremainHandler implements ClassFileTransformer {
    protected static volatile Instrumentation instrumentation;
    public PremainHandler(){}
    public void handle(String args, Instrumentation inst){
        instrumentation = inst;
    };
    public byte[] transform(ClassLoader loader,
                                     String className,
                                     Class<?> classBeingRedefined,
                                     ProtectionDomain protectionDomain,
                                     byte[] classfileBuffer)
            throws IllegalClassFormatException{
        return null;
    };
}
