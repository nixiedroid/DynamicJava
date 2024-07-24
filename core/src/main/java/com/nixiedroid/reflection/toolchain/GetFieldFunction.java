package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableBiFunction;

import java.lang.reflect.Field;

/**
 * A functional interface for retrieving a {@link Field} from a given class by its name.
 * This interface allows you to find a specific field within a class using its name.
 */
interface GetFieldFunction extends ThrowableBiFunction<Class<?>, String, Field> {

    /**
     * Java 7 implementation of {@link GetFieldFunction}.
     * Uses the {@link GetDeclaredFieldsFunction} to retrieve all declared fields and then searches for the field with the specified name.
     */
    class Java7 implements GetFieldFunction {
        private final GetDeclaredFieldsFunction getDeclaredFieldsFunction;

        /**
         * Constructor for the Java 7 implementation.
         * Initializes the {@link GetDeclaredFieldsFunction} used to retrieve all declared fields of a class.
         */
        Java7() {
            this.getDeclaredFieldsFunction = Context.get(GetDeclaredFieldsFunction.class);
        }

        /**
         * Retrieves a {@link Field} from the specified class by its name.
         * This method first obtains all declared fields of the class and then searches for the field with the given name.
         *
         * @param clazz     the class from which to retrieve the field
         * @param fieldName the name of the field to retrieve
         * @return the {@link Field} object corresponding to the specified name
         * @throws NoSuchFieldException if no field with the specified name is found in the class
         * @throws Throwable if any other error occurs during retrieval
         */
        @Override
        public Field apply(Class<?> clazz, String fieldName) throws Throwable {
            Field[] fields = this.getDeclaredFieldsFunction.apply(clazz);
            for (Field field : fields) {
                if (field.getName().equals(fieldName)) {
                    return field;
                }
            }
            throw new NoSuchFieldException("Field with name " + fieldName + " not found in class " + clazz.getName());
        }
    }
}
