package com.nixiedroid.waytwo.magic.parts;

import com.nixiedroid.waytwo.magic.Context;


import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;


public abstract class ClassFinder {
    protected MethodHandle forName0;

    public MethodHandle get() {
        return forName0;
    }

    public static class ForJava7 extends ClassFinder {

        public ForJava7() throws NoSuchMethodException, IllegalAccessException {
            MethodType type = MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class);
            forName0 = Context.i().lookup().findStatic(Class.class, "forName0",type);
        }


//        public Class<?> apply(String className, Boolean initialize, ClassLoader classLoader, Class<?> caller) throws Throwable {
//            return (Class<?>) forName0.invokeWithArguments(className, initialize.booleanValue(), classLoader, caller);
//        }
    }
}

