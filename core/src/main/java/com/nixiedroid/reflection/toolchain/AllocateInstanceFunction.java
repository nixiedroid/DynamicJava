package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableFunction;

/**
 * A functional interface that provides a method to allocate an instance of a class.
 * This interface extends {@link ThrowableFunction} to allow throwing exceptions.
 */
interface AllocateInstanceFunction extends ThrowableFunction<Class<?>, Object> {

    /**
     * An implementation of {@link AllocateInstanceFunction} for Java 7.
     * This implementation uses {@link sun.misc.Unsafe} to allocate instances of classes.
     */
    class Java7 implements AllocateInstanceFunction {

        private final sun.misc.Unsafe U;

        /**
         * Constructs a {@code Java7} instance by obtaining the {@link sun.misc.Unsafe} instance
         * from the {@link UnsafeSupplier}.
         */
        Java7() {
            this.U = Context.get(UnsafeSupplier.class).get();
        }

        /**
         * Allocates an instance of the specified class using {@link sun.misc.Unsafe}.
         *
         * @param clazz the class of which to allocate an instance
         * @return a new instance of the specified class
         * @throws InstantiationException if the class cannot be instantiated
         */
        @Override
        public Object apply(Class<?> clazz) throws InstantiationException {
            return this.U.allocateInstance(clazz);
        }
    }
}
