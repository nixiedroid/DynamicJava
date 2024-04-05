package com.nixiedroid.waytwo.magic.parts;

import com.nixiedroid.waytwo.magic.ClassRetriever;
import com.nixiedroid.waytwo.magic.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public abstract class HookClass {
    protected MethodHandle defineHookClassMethodHandle;

    public abstract Class<?> apply(Class<?> clientClass, byte[] byteCode) throws Throwable;

    public static class ForJava7 extends HookClass {
        protected sun.misc.Unsafe unsafe;

        public ForJava7() throws Throwable {
            super();
            unsafe = Context.i().getUnsafe();
            MethodHandles.Lookup l = Context.i().lookup();
            MethodHandle p = Context.i().privateLookup();
            MethodType type = MethodType.methodType(Class.class, Class.class, byte[].class, Object[].class);
            defineHookClassMethodHandle = retrieveConsulter(l, p).findSpecial(unsafe.getClass(), "defineAnonymousClass", type, unsafe.getClass());
        }

        public MethodHandles.Lookup retrieveConsulter(MethodHandles.Lookup consulter, MethodHandle privateLookupInMethodHandle) throws Throwable {
            return (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(consulter, unsafe.getClass());
        }

        @Override
        public Class<?> apply(Class<?> clientClass, byte[] byteCode) throws Throwable {
            return (Class<?>) defineHookClassMethodHandle.invokeWithArguments(unsafe, clientClass, byteCode, null);
        }

    }


    public static class ForJava9 extends ForJava7 {

        public ForJava9() throws Throwable {
            super();
        }

        @Override
        public MethodHandles.Lookup retrieveConsulter(MethodHandles.Lookup consulter, MethodHandle lookupMethod) throws Throwable {
            return (MethodHandles.Lookup) lookupMethod.invokeWithArguments(unsafe.getClass(), consulter);
        }

    }


    public static class ForJava17 extends HookClass {
        protected MethodHandle privateLookupInMethodHandle;
        protected MethodHandles.Lookup lookup;

        public ForJava17() throws NoSuchMethodException, IllegalAccessException {
            super();
            lookup = Context.i().lookup();
            privateLookupInMethodHandle = Context.i().privateLookup();
            MethodType type = MethodType.methodType(Class.class, byte[].class);
            defineHookClassMethodHandle = lookup.findSpecial(MethodHandles.Lookup.class, "defineClass", type, MethodHandles.Lookup.class);
        }


        @Override
        public Class<?> apply(Class<?> clientClass, byte[] byteCode) throws Throwable {
            MethodHandles.Lookup pLookup = (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(clientClass, lookup);
            try {
                return (Class<?>) defineHookClassMethodHandle.invokeWithArguments(pLookup, byteCode);
            } catch (LinkageError exc) {
                try {
                    return Class.forName(ClassRetriever.getClassName(byteCode));
                } catch (Throwable excTwo) {
                    throw exc;
                }
            }
        }

    }
}
