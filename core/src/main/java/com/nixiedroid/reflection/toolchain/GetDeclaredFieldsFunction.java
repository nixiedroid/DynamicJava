package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * A functional interface for obtaining declared fields of a given class.
 * This interface uses reflection to access and retrieve the fields.
 */
interface GetDeclaredFieldsFunction extends ThrowableFunction<Class<?>, Field[]> {

    /**
     * Implementation of {@link GetDeclaredFieldsFunction} for Java 7.
     * Uses method handles to invoke the "getDeclaredFields0" method on the {@link Class} class
     * to retrieve the declared fields of a given class.
     */
    class Java7 implements GetDeclaredFieldsFunction {
        private final MethodHandle getFields0;

        /**
         * Constructs a {@code Java7} instance and sets up the method handle
         * to access the private "getDeclaredFields0" method of the {@link Class} class.
         *
         * @throws Throwable if an error occurs while setting up the method handle
         */
        Java7() throws Throwable {
            LookupHook getConsulterFunction = Context.get(LookupHook.class);
            MethodHandles.Lookup lookup = getConsulterFunction.apply(Class.class);
            this.getFields0 = lookup.findSpecial(Class.class, "getDeclaredFields0",
                    MethodType.methodType(Field[].class, boolean.class),
                    Class.class);
        }

        /**
         * Applies the function to obtain an array of declared fields of the given class.
         *
         * @param clazz the class from which to retrieve the declared fields
         * @return an array of {@link Field} objects representing the declared fields of the given class
         * @throws Throwable if an error occurs while retrieving the fields
         */
        @Override
        public Field[] apply(Class<?> clazz) throws Throwable {
            return (Field[]) this.getFields0.invokeWithArguments(clazz, false);
        }
    }
}

