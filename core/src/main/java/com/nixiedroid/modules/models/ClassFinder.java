package com.nixiedroid.modules.models;

import com.nixiedroid.modules.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;


public abstract class ClassFinder {
    protected MethodHandle classFinder;

    public abstract Class<?> apply(String s, boolean b, ClassLoader c, Class<?> z) throws Throwable;

    public static class ForJava7 extends ClassFinder {

        public ForJava7() throws Throwable {
            MethodHandles.Lookup lookup = Context.i().lookupCtor(Class.class);
            MethodType methodType = MethodType.methodType(
                    Class.class,
                    String.class,
                    boolean.class,
                    ClassLoader.class,
                    Class.class);
            classFinder = lookup.findStatic(Class.class, "forName0", methodType);
        }

        @Override
        public Class<?> apply(
                String className,
                boolean initialize,
                ClassLoader classLoader,
                Class<?> caller
        ) throws Throwable {
            return (Class<?>) classFinder
                    .invokeWithArguments(className, initialize, classLoader, caller);
        }

    }
}

