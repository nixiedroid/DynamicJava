package com.nixiedroid.javaagent;

import java.lang.instrument.Instrumentation;

@SuppressWarnings("unused")
public class Premain {
    private static volatile Instrumentation globalInstrumentation;
    private static volatile boolean isPremainAvailable = false;

    public static void premain(final String agentArgs, final Instrumentation inst) {
        globalInstrumentation = inst;
        isPremainAvailable = true;
    }

    public static long sizeOf(final Object object) throws IllegalStateException {
        if (isPremainAvailable) {
            return globalInstrumentation.getObjectSize(object);
        }
        throw new IllegalStateException("PremainNotAvailable");
    }

}
