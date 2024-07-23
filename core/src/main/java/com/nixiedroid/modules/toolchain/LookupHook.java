package com.nixiedroid.modules.toolchain;


import com.nixiedroid.function.ThrowableFunction;
import com.nixiedroid.modules.Const;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.function.Function;

public class LookupHook implements Function<Class<?>, MethodHandles.Lookup>, Tool {

    Function<Class<?>, MethodHandles.Lookup> func;

    public LookupHook() {
        if (geq(9)) {
            try (InputStream inputStream = LookupHook.class.getResourceAsStream(Const.LOOKUP_HOOK);) {
                MethodHandle lookup = Context.get(PrivateLookupFunction.class).get();
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class).apply(Class.class, toByteArray(inputStream));
                Context.get(SetFieldFunction.class).accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("privateLookup"), lookup);
                this.func = aClass -> {
                    try {
                        return (MethodHandles.Lookup) Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass);
                    } catch (InstantiationException e) {
                        throw new RuntimeException(e);
                    }
                };
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else if (geq(7)) {
            final MethodHandles.Lookup consulter = Context.get(TrustedLookupSupplier.class).get();
            final MethodHandle privateLookupInMethodHandle = Context.
                    get(PrivateLookupFunction.class).get();
            this.func = aClass -> {
                try {
                    return (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(consulter, aClass);
                } catch (Throwable e) {
                    throw new RuntimeException(e);
                }
            };
        }
    }

    private static byte[] toByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while (-1 != (bytesRead = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    @Override
    public MethodHandles.Lookup apply(Class<?> aClass) {
        return this.func.apply(aClass);
    }
}
