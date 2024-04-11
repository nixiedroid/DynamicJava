package com.nixiedroid.modules.models.lookups;

import com.nixiedroid.modules.Const;
import com.nixiedroid.modules.Context;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public abstract class TargetedLookup {
    protected MethodHandles.Lookup lookup;
    protected MethodHandle privateLookup;
    protected Function<Class<?>, MethodHandles.Lookup> hook;

    public abstract MethodHandles.Lookup apply(Class<?> cls) throws Throwable;

    public static class ForJava7 extends TargetedLookup {
        public ForJava7() {
            lookup = Context.i().trustedLookup();
            privateLookup = Context.i().privateLookup();
        }

        @Override
        public MethodHandles.Lookup apply(Class<?> cls) throws Throwable {
            return (MethodHandles.Lookup) privateLookup.invokeWithArguments(lookup, cls);
        }

    }
    @SuppressWarnings("unchecked")
    public static class ForJava9 extends TargetedLookup {

        public ForJava9() throws Throwable {
            try (
                    InputStream inputStream = TargetedLookup.class.getResourceAsStream(
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
                hook = (Function<Class<?>, MethodHandles.Lookup>) Context.i().allocateInstance(methodHandleWrapperClass);
            }
        }

        @Override
        public MethodHandles.Lookup apply(Class<?> input) {
            return hook.apply(input);
        }
    }
}