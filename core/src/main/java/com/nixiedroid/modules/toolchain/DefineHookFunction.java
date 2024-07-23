package com.nixiedroid.modules.toolchain;

import com.nixiedroid.classes.JavaClassParser;
import com.nixiedroid.function.ThrowableBiFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;

public class DefineHookFunction
        implements ThrowableBiFunction<Class<?>, byte[], Class<?>>,Tool {

    private MethodHandle privateLookupInMethodHandle;
    private MethodHandles.Lookup lookup;

    private  sun.misc.Unsafe U;
    private MethodHandle mh;

    public DefineHookFunction() throws Throwable {
        if (this.javaVersion >= 17) {
            this.lookup = Context.get(TrustedLookupSupplier.class).get();
            this.mh = this.lookup.findSpecial(
                    MethodHandles.Lookup.class,
                    "defineClass",
                    MethodType.methodType(Class.class, byte[].class),
                    MethodHandles.Lookup.class
            );
            this.privateLookupInMethodHandle = Context.get(PrivateLookupFunction.class).get();
        } else if (this.javaVersion >= 7) {
            this.U = Context.get(UnsafeSupplier.class).get();
            this.mh = retrieveHook(
                    Context.get(TrustedLookupSupplier.class).get(),
                    Context.get(PrivateLookupFunction.class).get()
            ).findSpecial(
                    this.U.getClass(),
                    "defineAnonymousClass",
                    MethodType.methodType(Class.class, Class.class, byte[].class, Object[].class),
                    this.U.getClass()
            );
        }
    }

    @Override
    public Class<?> apply(Class<?> clazz, byte[] bytes) throws Throwable {
        if (this.javaVersion >= 17) {
            MethodHandles.Lookup lookup = (MethodHandles.Lookup)
                    this.privateLookupInMethodHandle
                            .invokeWithArguments(clazz, bytes);
            try {
                return (Class<?>) this.mh.invokeWithArguments(lookup, bytes);
            } catch (LinkageError exc) {
                try {
                    return Class.forName(
                            JavaClassParser.create(ByteBuffer.wrap(bytes)).getName()
                    );
                } catch (Throwable excTwo) {
                    throw exc;
                }
            }
        } else if (this.javaVersion >= 7) {
            return (Class<?>) this.mh.invokeWithArguments(this.U, clazz, bytes, null);
        }
        return null;
    }

    private MethodHandles.Lookup retrieveHook(
            MethodHandles.Lookup lookup,
            MethodHandle privateLookupInMethodHandle
    ) throws Throwable {
        if (this.javaVersion >= 9) {
            return (MethodHandles.Lookup)
                    privateLookupInMethodHandle
                    .invokeWithArguments(U.getClass(), lookup);
        } else if (this.javaVersion >= 7) {
            return (MethodHandles.Lookup)
                    privateLookupInMethodHandle
                            .invokeWithArguments(lookup, U.getClass());
        }
        return null;
    }
}
