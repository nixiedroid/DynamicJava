package com.nixiedroid.reflection.toolchain;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * Represents a supplier of {@link Unsafe} instances.
 *
 * <p>This interface is a functional interface whose functional method is {@link #get()}.
 */
interface UnsafeSupplier extends Supplier<Unsafe> {

    /**
     * An implementation of {@link UnsafeSupplier} that retrieves an instance of {@link Unsafe}
     * using reflection. This implementation is specific to Java 7, where {@link Unsafe}
     * is accessed via a static field named "theUnsafe".
     */
    class Java7 implements UnsafeSupplier {

        private final Unsafe U;

        /**
         * Constructs a {@code Java7} instance by obtaining the {@link Unsafe} instance via reflection.
         *
         * @throws Throwable if unable to access the {@link Unsafe} instance
         */
        Java7() throws Throwable {
            try {
                Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                theUnsafeField.setAccessible(true);
                this.U = (Unsafe) theUnsafeField.get(null);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException("Unable to access Unsafe instance", e);
            }
        }

        /**
         * Returns the {@link Unsafe} instance obtained by this supplier.
         *
         * @return the {@link Unsafe} instance
         */
        @Override
        public Unsafe get() {
            return this.U;
        }
    }
}