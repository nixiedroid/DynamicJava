package com.nixiedroid.reflection.toolchain;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.function.Supplier;

interface UnsafeSupplier extends Supplier<sun.misc.Unsafe> {

    class Java7 implements UnsafeSupplier {

        private final sun.misc.Unsafe U;

        Java7() throws Throwable {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafeField.setAccessible(true);
            this.U = (sun.misc.Unsafe) theUnsafeField.get(null);
        }

        @Override
        public Unsafe get() {
            return this.U;
        }
    }
}
