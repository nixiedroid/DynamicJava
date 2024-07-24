package com.nixiedroid.reflection.toolchain;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

/**
 * A functional interface for retrieving the value of a specified field from a target object
 * using reflection and {@link sun.misc.Unsafe}.
 * This interface extends {@link BiFunction} to handle exceptions and provide the required functionality.
 */
interface GetFieldValueFunction extends BiFunction<Object, Field, Object> {

    /**
     * An implementation of {@link GetFieldValueFunction} for Java 7.
     * This implementation uses {@link sun.misc.Unsafe} to get values of fields from target objects.
     */
    class Java7 implements GetFieldValueFunction {

        protected sun.misc.Unsafe unsafe;

        /**
         * Constructs a {@code Java7} instance by obtaining the {@link sun.misc.Unsafe} instance
         * from the {@link UnsafeSupplier}.
         */
        Java7() {
            this.unsafe = Context.get(UnsafeSupplier.class).get();
        }

        /**
         * Retrieves the value of a specified field from a target object.
         * Uses {@link sun.misc.Unsafe} to handle both static and instance fields,
         * including primitive types and object references.
         *
         * @param target the target object from which to retrieve the field value. For static fields, this parameter is ignored.
         * @param field the field whose value should be retrieved
         * @return the value of the specified field
         * @throws IllegalArgumentException if the target object is null for non-static fields or the field's type is not compatible with the target object
         */
        @Override
        public Object apply(Object target, Field field) {
            // Determine the actual target for static fields
            target = Modifier.isStatic(field.getModifiers()) ? field.getDeclaringClass() : target;

            // Get the field offset based on whether the field is static or not
            long fieldOffset = Modifier.isStatic(field.getModifiers()) ?
                    this.unsafe.staticFieldOffset(field) :
                    this.unsafe.objectFieldOffset(field);

            Class<?> clazz = field.getType();

            // Retrieve the field value based on its type and volatility
            if (!clazz.isPrimitive()) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getObject(target, fieldOffset);
                } else {
                    return this.unsafe.getObjectVolatile(target, fieldOffset);
                }
            } else if (clazz == short.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getShort(target, fieldOffset);
                } else {
                    return this.unsafe.getShortVolatile(target, fieldOffset);
                }
            } else if (clazz == int.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getInt(target, fieldOffset);
                } else {
                    return this.unsafe.getIntVolatile(target, fieldOffset);
                }
            } else if (clazz == long.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getLong(target, fieldOffset);
                } else {
                    return this.unsafe.getLongVolatile(target, fieldOffset);
                }
            } else if (clazz == float.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getFloat(target, fieldOffset);
                } else {
                    return this.unsafe.getFloatVolatile(target, fieldOffset);
                }
            } else if (clazz == double.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getDouble(target, fieldOffset);
                } else {
                    return this.unsafe.getDoubleVolatile(target, fieldOffset);
                }
            } else if (clazz == boolean.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getBoolean(target, fieldOffset);
                } else {
                    return this.unsafe.getBooleanVolatile(target, fieldOffset);
                }
            } else if (clazz == byte.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getByte(target, fieldOffset);
                } else {
                    return this.unsafe.getByteVolatile(target, fieldOffset);
                }
            } else { // Assumes 'char' is the only remaining primitive type
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return this.unsafe.getChar(target, fieldOffset);
                } else {
                    return this.unsafe.getCharVolatile(target, fieldOffset);
                }
            }
        }
    }
}