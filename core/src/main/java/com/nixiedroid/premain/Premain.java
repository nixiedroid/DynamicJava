package com.nixiedroid.premain;

import java.lang.instrument.Instrumentation;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

@SuppressWarnings("unused")
public class Premain {
//    private static volatile Instrumentation globalInstrumentation;
//    private static volatile boolean isPremainAvailable = false;

    public static void premain(final String agentArgs, final Instrumentation inst) {
//        globalInstrumentation = inst;
//        isPremainAvailable = true;
        Optional<PremainHandler> handlerNullable =  findHandlerClass(agentArgs, inst);
        if (handlerNullable.isPresent()) {
            PremainHandler handler = handlerNullable.get();
            handler.handle(agentArgs, inst);
            inst.addTransformer(handler);
       }
    }

    private static void replaceClassloader() {

    }

    private static Optional<PremainHandler> findHandlerClass(final String agentArgs, final Instrumentation inst) {
        try {
            return Optional.of((PremainHandler) Class.forName(getHandlerName())
                    .getConstructor().newInstance());
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            e.getTargetException().printStackTrace();
            throw new RuntimeException();
        } catch (NoSuchMethodException | ClassNotFoundException ignored) {
           return Optional.empty();
        }
    }
    private static String getHandlerName(){
        return Premain.class.getPackage().getName() + ".Handler";
    }


    public static long sizeOf(final Object object) throws IllegalStateException {
//        if (isPremainAvailable) {
//            return globalInstrumentation.getObjectSize(object);
//        }
//        throw new IllegalStateException("PremainNotAvailable");
        return 1;
    }
}
