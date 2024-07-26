package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.bytes.converter.ByteArrays;
import com.nixiedroid.function.FunctionAdapter;
import com.nixiedroid.function.ThrowableFunction;

import java.io.InputStream;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

/**
 * A functional interface for providing a {@link MethodHandles.Lookup} for a given class.
 * This interface supports various mechanisms for creating or obtaining a {@link MethodHandles.Lookup} instance.
 */
@SuppressWarnings("unused")
interface LookupHook extends ThrowableFunction<Class<?>, MethodHandles.Lookup,Throwable> {

    /**
     * Default implementation of {@link LookupHook}.
     * Uses a {@link ThrowableFunction} to obtain a {@link MethodHandles.Lookup} for a given class.
     */
    abstract class Default implements LookupHook {
        protected ThrowableFunction<Class<?>, MethodHandles.Lookup,Throwable> function;

        /**
         * Applies the function to obtain a {@link MethodHandles.Lookup} for the given class.
         *
         * @param clazz the class for which to obtain the lookup
         * @return the {@link MethodHandles.Lookup} for the given class
         * @throws Throwable if an error occurs during the operation
         */
        @Override
        public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
            return this.function.apply(clazz);
        }
    }

    /**
     * Implementation of {@link LookupHook} for Java 7.
     * Uses reflection to invoke the "privateLookupIn" method to obtain a {@link MethodHandles.Lookup}
     * for a given class.
     */
    class Java7 extends Default {
        /**
         * Constructs a {@code Java7} instance and sets up the function to use reflection
         * to invoke "privateLookupIn" on the {@link MethodHandles.Lookup} to obtain the lookup
         * for the given class.
         */
        Java7() {
            final MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            final MethodHandle privateLookupInMethodHandle = Context.get(PrivateLookupSupplier.class).get();
            this.function = clazz -> (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(lookup, clazz);
        }
    }

    /**
     * Implementation of {@link LookupHook} for Java 9 and later.
     * Loads a class definition from a resource file, creates an instance of this class,
     * and uses it to obtain a {@link MethodHandles.Lookup} for a given class.
     */
    @SuppressWarnings("unchecked")
    class Java9 extends Default {
        /**
         * Constructs a {@code Java9} instance by loading a class definition from a resource file,
         * setting a method handle, and using it to create a {@link MethodHandles.Lookup} for the
         * given class.
         *
         * @throws Throwable if an error occurs during resource loading or method handle setup
         */
        Java9() throws Throwable {
            try (InputStream inputStream = LookupHook.class.getResourceAsStream(Const.LOOKUP_HOOK.getPath())) {
                if (inputStream == null) throw new IllegalArgumentException("Resource not found");

                MethodHandle lookup = Context.get(PrivateLookupSupplier.class).get();
                Class<?> methodHandleWrapperClass = Context.get(DefineHookFunction.class)
                        .apply(Class.class, ByteArrays.toByteArray(inputStream));

                Context.get(SetFieldValueFunction.class)
                        .accept(methodHandleWrapperClass, methodHandleWrapperClass.getDeclaredField("method"), lookup);

                this.function = new FunctionAdapter<>(((java.util.function.Function<Class<?>, MethodHandles.Lookup>)
                        Context.get(AllocateInstanceFunction.class).apply(methodHandleWrapperClass)));
            }
        }
    }
}