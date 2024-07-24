package com.nixiedroid.modules.toolchain;

import com.nixiedroid.classes.JavaClassParser;
import com.nixiedroid.function.ThrowableBiFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;

public class DefineHookFunction implements ThrowableBiFunction<Class<?>, byte[], Class<?>>, Tool {

    private final MethodHandle defineHook;
    private MethodHandle privateLookupIn;
    private MethodHandles.Lookup lookup;
    private sun.misc.Unsafe U;

    public DefineHookFunction() throws Throwable {
        if (thisVersionGEQ(JAVA_17)) {
            this.lookup = Context.get(TrustedLookupSupplier.class).get();
            this.defineHook = this.lookup.findSpecial(MethodHandles.Lookup.class, "defineClass", MethodType.methodType(Class.class, byte[].class), MethodHandles.Lookup.class);
            this.privateLookupIn = Context.get(PrivateLookupSupplier.class).get();
            return;
        } else if (thisVersionGEQ(JAVA_7)) {
            this.U = Context.get(UnsafeSupplier.class).get();
            this.defineHook = retrieveHook(Context.get(TrustedLookupSupplier.class).get(), Context.get(PrivateLookupSupplier.class).get()).findSpecial(this.U.getClass(), "defineAnonymousClass", MethodType.methodType(Class.class, Class.class, byte[].class, Object[].class), this.U.getClass());
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public Class<?> apply(Class<?> clazz, byte[] bytes) throws Throwable {
        if (thisVersionGEQ(JAVA_17)) {
            MethodHandles.Lookup targetedLookup = (MethodHandles.Lookup) this.privateLookupIn.invokeWithArguments(clazz, this.lookup);
            try {
                return (Class<?>) this.defineHook.invokeWithArguments(targetedLookup, bytes);
            } catch (LinkageError exc) {
                try {
                    return Class.forName(JavaClassParser.create(ByteBuffer.wrap(bytes)).getName());
                } catch (Throwable excTwo) {
                    throw exc;
                }
            }
        } else if (thisVersionGEQ(JAVA_7)) {
            return (Class<?>) this.defineHook.invokeWithArguments(this.U, clazz, bytes, null);
        }
        return null;
    }

    private MethodHandles.Lookup retrieveHook(MethodHandles.Lookup lookup, MethodHandle privateLookupIn) throws Throwable {
        if (thisVersionGEQ(JAVA_9)) {
            return (MethodHandles.Lookup) privateLookupIn.invokeWithArguments(this.U.getClass(), lookup);
        } else if (thisVersionGEQ(JAVA_7)) {
            return (MethodHandles.Lookup) privateLookupIn.invokeWithArguments(lookup, this.U.getClass());
        }
        return null;
    }
}
