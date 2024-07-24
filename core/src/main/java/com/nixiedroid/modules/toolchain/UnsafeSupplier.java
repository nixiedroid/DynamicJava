package com.nixiedroid.modules.toolchain;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class UnsafeSupplier implements Supplier<sun.misc.Unsafe> {

    private final sun.misc.Unsafe U;

    public UnsafeSupplier() throws NoSuchFieldException, IllegalAccessException {
        Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeField.setAccessible(true);
        this.U = (sun.misc.Unsafe)theUnsafeField.get(null);
    }

    @Override
    public Unsafe get() {
        return this.U;
    }
}
