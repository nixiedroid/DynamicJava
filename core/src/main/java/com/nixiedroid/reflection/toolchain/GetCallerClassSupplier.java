package com.nixiedroid.reflection.toolchain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Supplier;

/**
 * Interface that provides a mechanism to retrieve the caller class.
 *
 * <p>This interface extends {@link Supplier} and is designed to retrieve the
 * class that invoked a particular method. The implementation for Java 7 is provided,
 * which uses internal Java reflection APIs to achieve this.</p>
 */
@SuppressWarnings("unused")
interface GetCallerClassSupplier extends Supplier<Class<?>> {

    /**
     * Implementation of {@link GetCallerClassSupplier} for Java 7.
     *
     * <p>This implementation uses internal Java APIs to retrieve the caller class. It
     * accesses the `jdk.internal.reflect.Reflection.getCallerClass` method through
     * reflection. This is useful for scenarios where you need to determine the caller
     * class in a dynamic or reflective context.</p>
     */
    class Java7 implements GetCallerClassSupplier {

        private final MethodHandle getCaller;

        /**
         * Constructs an instance of {@link Java7} and initializes the method handle
         * for the `getCallerClass` method.
         *
         * <p>It uses {@link GetClassByNameFunction} to retrieve the internal
         * {@code jdk.internal.reflect.Reflection} class, and then obtains a
         * {@link MethodHandle} for the `getCallerClass` method through reflection.</p>
         *
         * @throws Throwable if there is an error retrieving the method handle or invoking
         *         internal methods.
         */
        Java7() throws Throwable {
            try {
                // Retrieve the internal Reflection class using its name
                Class<?> reflectionClass = Context.get(GetClassByNameFunction.class).apply(
                        "jdk.internal.reflect.Reflection",
                        false,
                        Java7.class.getClassLoader(),
                        Class.class
                );

                // Obtain a MethodHandle for the getCallerClass method
                MethodHandles.Lookup lookup = Context.get(LookupCtorFunction.class).apply(reflectionClass);
                this.getCaller = lookup.findStatic(
                        reflectionClass,
                        "getCallerClass",
                        MethodType.methodType(Class.class)
                );
            } catch (Exception e) {
                // Wrap and rethrow exceptions as RuntimeException for better error handling
                throw new RuntimeException("Failed to initialize GetCallerClassFunction", e);
            }
        }

        /**
         * Retrieves the caller class by invoking the internal `getCallerClass` method.
         *
         * <p>This method uses the previously initialized method handle to obtain the
         * class that invoked the method where this instance was created.</p>
         *
         * @return the caller class, which is the class that invoked the method
         *         where this instance was created.
         */
        @Override
        public Class<?> get()  {
            try {
                // Invoke the method handle to get the caller class
                return (Class<?>) this.getCaller.invoke();
            } catch (Throwable t) {
                // Wrap and rethrow any exceptions encountered during method invocation
                throw new Error("Failed to get caller class", t);
            }
        }
    }
}
