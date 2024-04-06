package com.nixiedroid.parts.lookups;

import com.nixiedroid.Const;
import com.nixiedroid.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public abstract class LookupSupplyFunction {
    protected MethodHandles.Lookup lookup;
    protected MethodHandle privateLookup;
    protected Object hook;

    public abstract MethodHandles.Lookup apply(Class<?> cls) throws Throwable;

    public static class ForJava7 extends LookupSupplyFunction {
        public ForJava7() {
            lookup = Context.i().lookup();
            privateLookup = Context.i().privateLookup();
        }

        @Override
        public MethodHandles.Lookup apply(Class<?> cls) throws Throwable {
            return (MethodHandles.Lookup) privateLookup.invokeWithArguments(lookup, cls);
        }

    }

    public static class ForJava9 extends LookupSupplyFunction {

        public ForJava9() throws Throwable {
            try (
                    InputStream inputStream = LookupSupplyFunction.class.getResourceAsStream(
                            Const.LOOKUP_HOOK
                    );
            ) {
                privateLookup = Context.i().privateLookup();
                byte[] classBytes;
                try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                    byte[] buffer = new byte[1024];
                    int bytesRead = 0;
                    while (-1 != (bytesRead = inputStream.read(buffer))) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                     classBytes = outputStream.toByteArray();
                }
                Class<?> methodHandleWrapperClass = Context.i().useHookClass(Class.class, classBytes);
                Context.i().setFieldValue(
                        methodHandleWrapperClass,
                        methodHandleWrapperClass.getDeclaredField("privateLookup"),
                        privateLookup
                );
                empowerMainConsulter((MethodHandles.Lookup) methodHandleWrapperClass.getDeclaredField("lookup").get(null));
                hook = Context.i().allocateInstance(methodHandleWrapperClass);
            }
        }

        protected void empowerMainConsulter(MethodHandles.Lookup lookup) throws Throwable {

        }

        @Override
        public MethodHandles.Lookup apply(Class<?> input) {
            return ((Function<Class<?>, MethodHandles.Lookup>) hook).apply(input);
        }
    }
}