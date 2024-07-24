package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableQuadFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/**
 * A functional interface for retrieving a {@link Class} object based on the class name.
 * It allows retrieving a class by its name with additional parameters such as initialization flag, class loader, and caller class.
 */
interface GetClassByNameFunction extends ThrowableQuadFunction<String, Boolean, ClassLoader, Class<?>, Class<?>> {

    /**
     * Java 7 implementation of {@link GetClassByNameFunction}.
     * Uses reflection and method handles to invoke {@code Class.forName0} to retrieve the class by name.
     */
    class Java7 implements GetClassByNameFunction {
        private final MethodHandle classFinder;

        /**
         * Constructor for the Java 7 implementation.
         * Initializes the method handle for {@code Class.forName0} to allow class retrieval by name.
         *
         * @throws Throwable if any error occurs while setting up the method handle
         */
        Java7() throws Throwable {
            MethodHandles.Lookup lookup = Context.get(LookupCtorFunction.class).apply(Class.class);
            this.classFinder = lookup.findStatic(Class.class, "forName0", MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class));
        }

        /**
         * Retrieves a {@link Class} object based on the given class name, initialization flag, class loader, and caller class.
         *
         * @param name        the name of the class to retrieve
         * @param initialize  whether to initialize the class
         * @param loader      the class loader to use
         * @param caller      the caller class
         * @return the {@link Class} object corresponding to the specified name
         * @throws Throwable if any error occurs during class retrieval
         */
        @Override
        public Class<?> apply(String name, Boolean initialize, ClassLoader loader, Class<?> caller) throws Throwable {
            return (Class<?>) this.classFinder.invoke(name, initialize, loader, caller);
        }
    }
}

