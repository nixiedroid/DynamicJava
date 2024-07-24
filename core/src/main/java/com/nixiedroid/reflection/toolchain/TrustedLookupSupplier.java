package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.runtime.Properties;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.function.Supplier;

/**
 * A functional interface for supplying a {@link MethodHandles.Lookup} instance.
 * This interface is used to provide access to a trusted {@link MethodHandles.Lookup} object.
 */
@SuppressWarnings("unused")
interface TrustedLookupSupplier extends Supplier<MethodHandles.Lookup> {

    /**
     * Default implementation of {@link TrustedLookupSupplier}.
     * Provides a standard way to obtain a {@link MethodHandles.Lookup} object.
     */
    abstract class Default implements TrustedLookupSupplier {
        protected static final int TRUSTED = -1;
        protected final MethodHandles.Lookup lookup = MethodHandles.lookup();

        /**
         * Returns the {@link MethodHandles.Lookup} instance provided by this implementation.
         *
         * @return the {@link MethodHandles.Lookup} instance
         */
        @Override
        public MethodHandles.Lookup get() {
            return this.lookup;
        }
    }

    /**
     * Implementation of {@link TrustedLookupSupplier} for Java 7.
     * Uses reflection to set the "allowedModes" field of {@link MethodHandles.Lookup} to TRUSTED.
     */
    class Java7 extends Default {
        /**
         * Constructs a {@code Java7} instance by setting the "allowedModes" field of
         * {@link MethodHandles.Lookup} to TRUSTED using reflection.
         *
         * @throws Throwable if an error occurs while accessing or modifying the field
         */
        Java7() throws Throwable {
            Field modes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            modes.setAccessible(true);
            modes.setInt(this.lookup, TRUSTED);
        }
    }

    /**
     * Implementation of {@link TrustedLookupSupplier} for Java 17.
     * Uses {@link sun.misc.Unsafe} to set the "allowedModes" field of {@link MethodHandles.Lookup}
     * to TRUSTED.
     */
    class Java17 extends Default {
        /**
         * Constructs a {@code Java17} instance by using {@link sun.misc.Unsafe} to set the
         * "allowedModes" field of {@link MethodHandles.Lookup} to TRUSTED.
         * The memory offset of the "allowedModes" field depends on whether the JVM is 64-bit or not.
         */
        Java17() {
            sun.misc.Unsafe unsafe = Context.get(UnsafeSupplier.class).get();
            final long allowedModesFieldMemoryOffset = Properties.is64Bit() ? 12L : 8L;
            unsafe.putInt(this.lookup, allowedModesFieldMemoryOffset, TRUSTED);
        }
    }
}
