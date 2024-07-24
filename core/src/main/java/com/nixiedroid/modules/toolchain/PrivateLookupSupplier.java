package com.nixiedroid.modules.toolchain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Supplier;

public class PrivateLookupSupplier implements Supplier<MethodHandle>, Tool {

    private final MethodHandle methodHandle;

    public PrivateLookupSupplier() throws NoSuchMethodException, IllegalAccessException {
        if (javaVersion >= 9) {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            this.methodHandle = lookup.findStatic(MethodHandles.class, "privateLookupIn", MethodType.methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class));
            return;
        } else if (javaVersion >= 7) {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            this.methodHandle = lookup.findSpecial(MethodHandles.Lookup.class, "in", MethodType.methodType(MethodHandles.Lookup.class, Class.class), MethodHandles.Lookup.class);
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public MethodHandle get() {
        return this.methodHandle;
    }
}
