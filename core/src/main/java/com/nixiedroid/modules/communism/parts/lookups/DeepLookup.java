package com.nixiedroid.modules.communism.parts.lookups;

import com.nixiedroid.modules.communism.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

public  abstract class DeepLookup {
    protected MethodHandle methodHandle;
    public abstract MethodHandles.Lookup apply(Class<?> cl) throws Throwable;
    public static class ForJava7 extends DeepLookup {
        public ForJava7() throws Throwable {
            //Check if allowedModes exists if not throw NoSuchFieldException (for Semeru JDK compatibility)
            Context.i().getField(MethodHandles.Lookup.class, "allowedModes");
        }

        @Override
        public MethodHandles.Lookup apply(Class<?> input) throws Throwable {
            return  Context.i().targetedLookup(input);
        }

    }

    public static class ForJava9 extends DeepLookup {

        public ForJava9() throws Throwable {
            //Check if allowedModes exists if not throw NoSuchFieldException (for Semeru JDK compatibility)
            Context.i().getField(MethodHandles.Lookup.class, "allowedModes");
            Constructor<MethodHandles.Lookup> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            Context.i().setAccessible(lookupCtor, true);
            methodHandle = lookupCtor.newInstance(
                    MethodHandles.Lookup.class,
                    Lookup.ForJava7.TRUSTED).findConstructor(
                    MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, int.class)
            );
        }


        @Override
        public MethodHandles.Lookup apply(Class<?> input) throws Throwable {
            return (MethodHandles.Lookup)methodHandle.invokeWithArguments(input, Lookup.ForJava7.TRUSTED);

        }
    }


    public static class ForJava14 extends DeepLookup {

        public ForJava14() throws Throwable {
            //Check if allowedModes exists if not throw NoSuchFieldException (for Semeru JDK compatibility)
            Context.i().getField(MethodHandles.Lookup.class, "allowedModes");
            Constructor<?> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
            Context.i().setAccessible(lookupCtor, true);
            MethodType ctorArgs = MethodType.methodType(void.class, Class.class, Class.class, int.class);
            methodHandle = ((MethodHandles.Lookup)
                    lookupCtor.newInstance(
                            MethodHandles.Lookup.class,
                            null,
                            Lookup.ForJava7.TRUSTED))
                    .findConstructor(MethodHandles.Lookup.class,ctorArgs);

        }


        @Override
        public MethodHandles.Lookup apply(Class<?> input) throws Throwable {
            return (MethodHandles.Lookup)methodHandle
                    .invokeWithArguments(input, null, Lookup.ForJava7.TRUSTED);
        }

    }
}
