package com.nixiedroid.premain;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;

@SuppressWarnings("unused")
public class Premain {
    private static volatile Instrumentation globalInstrumentation;
    private static volatile boolean isPremainAvailable = false;

    public static void premain(final String agentArgs, final Instrumentation inst) {
        globalInstrumentation = inst;
        isPremainAvailable = true;
        PremainHandler handler = null;
        try {
            handler = (PremainHandler) Class
                    .forName(Premain.class.getPackage()+"Handler")
                    .getConstructor().newInstance();
            handler.handle(agentArgs,inst);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            throw new RuntimeException();
        } catch (NoSuchMethodException | ClassNotFoundException ignored) {}

    }
    private static void replaceClassloader() {

    }


    public static long sizeOf(final Object object) throws IllegalStateException {
        if (isPremainAvailable) {
            return globalInstrumentation.getObjectSize(object);
        }
        throw new IllegalStateException("PremainNotAvailable");
    }


}
