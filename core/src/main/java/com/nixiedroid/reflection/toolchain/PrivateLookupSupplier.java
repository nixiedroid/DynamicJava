package com.nixiedroid.reflection.toolchain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Supplier;

/**
 * A functional interface for supplying a {@link MethodHandle} instance.
 * This interface is used to obtain a {@link MethodHandle} that can be used for private lookups.
 */
@SuppressWarnings("unused")
interface PrivateLookupSupplier extends Supplier<MethodHandle> {

    /**
     * Default implementation of {@link PrivateLookupSupplier}.
     * Provides a way to access a {@link MethodHandle} for private lookup operations.
     */
    abstract class Default implements PrivateLookupSupplier {
        protected MethodHandle methodHandle;

        /**
         * Returns the {@link MethodHandle} instance provided by this implementation.
         *
         * @return the {@link MethodHandle} instance
         */
        @Override
        public MethodHandle get() {
            return this.methodHandle;
        }
    }

    /**
     * Implementation of {@link PrivateLookupSupplier} for Java 7.
     * Uses reflection to find the {@link MethodHandle} for the "in" method of {@link MethodHandles.Lookup}.
     */
    class Java7 extends Default {
        /**
         * Constructs a {@code Java7} instance by finding the {@link MethodHandle} for the "in" method
         * of {@link MethodHandles.Lookup} using reflection.
         *
         * @throws Throwable if an error occurs while accessing or finding the method handle
         */
        Java7() throws Throwable {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            this.methodHandle = lookup.findSpecial(MethodHandles.Lookup.class, "in", MethodType.methodType(MethodHandles.Lookup.class, Class.class), MethodHandles.Lookup.class);
        }
    }

    /**
     * Implementation of {@link PrivateLookupSupplier} for Java 9 and later.
     * Finds the {@link MethodHandle} for the "privateLookupIn" method of {@link MethodHandles}.
     */
    class Java9 extends Default {
        /**
         * Constructs a {@code Java9} instance by finding the {@link MethodHandle} for the
         * "privateLookupIn" method of {@link MethodHandles} using reflection.
         *
         * @throws Throwable if an error occurs while accessing or finding the method handle
         */
        Java9() throws Throwable {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            this.methodHandle = lookup.findStatic(MethodHandles.class, "privateLookupIn", MethodType.methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class));
        }
    }
}
