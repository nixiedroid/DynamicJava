package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

/**
 * A functional interface for obtaining a {@link MethodHandles.Lookup} instance for a given class.
 * Provides various implementations depending on the Java version.
 */
@SuppressWarnings({"unused"})
interface LookupCtorFunction extends ThrowableFunction<Class<?>, MethodHandles.Lookup,Throwable> {

    /**
     * Abstract base class for {@link LookupCtorFunction} implementations.
     * Provides a method to apply a function to obtain a {@link MethodHandles.Lookup} instance.
     */
    abstract class Default implements LookupCtorFunction {
        protected ThrowableFunction<Class<?>, MethodHandles.Lookup,Throwable> func;

        /**
         * Applies the function to obtain a {@link MethodHandles.Lookup} instance for the given class.
         *
         * @param clazz the class for which to obtain the lookup
         * @return the {@link MethodHandles.Lookup} instance
         * @throws Throwable if any error occurs during lookup
         */
        @Override
        public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
            return this.func.apply(clazz);
        }
    }

    /**
     * Java 7 implementation of {@link LookupCtorFunction}.
     * Uses reflection and existing hooks to obtain a {@link MethodHandles.Lookup} instance.
     */
    class Java7 extends Default {
        Java7() throws Throwable {
            // Ensure access to the "allowedModes" field in MethodHandles.Lookup
            Context.get(GetFieldFunction.class).apply(MethodHandles.Lookup.class, "allowedModes");
            // Define the function to obtain a MethodHandles.Lookup instance
            this.func = clazz -> Context.get(LookupHook.class).apply(clazz);
        }
    }

    /**
     * Java 9 implementation of {@link LookupCtorFunction}.
     * Uses reflection to obtain a {@link MethodHandles.Lookup} instance via its constructor.
     */
    class Java9 extends Default {
        Java9() throws Throwable {
            // Access the constructor of MethodHandles.Lookup
            Constructor<MethodHandles.Lookup> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            Context.get(SetAccessibleBiConsumer.class).accept(lookupCtor, true); // Make it accessible
            final MethodHandle methodHandle = lookupCtor.newInstance(MethodHandles.Lookup.class, -1)
                    .findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, int.class));
            this.func = clazz -> (MethodHandles.Lookup) methodHandle.invokeWithArguments(clazz, -1);
        }
    }

    /**
     * Java 14 implementation of {@link LookupCtorFunction}.
     * Uses reflection to obtain a {@link MethodHandles.Lookup} instance with additional parameters.
     */
    class Java14 extends Default {
        @SuppressWarnings({"JavaReflectionMemberAccess", "JavaLangInvokeHandleSignature"})
        Java14() throws Throwable {
            // Access the constructor of MethodHandles.Lookup with additional parameters
            Constructor<?> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
            Context.get(SetAccessibleBiConsumer.class).accept(lookupCtor, true); // Make it accessible
            final MethodHandle mthHandle = ((MethodHandles.Lookup) lookupCtor.newInstance(MethodHandles.Lookup.class, null, -1))
                    .findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, Class.class, int.class));
            this.func = clazz -> (MethodHandles.Lookup) mthHandle.invokeWithArguments(clazz, null, -1);
        }
    }
}
