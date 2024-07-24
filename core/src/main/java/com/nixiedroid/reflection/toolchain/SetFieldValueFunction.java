package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.TerConsumer;
import com.nixiedroid.reflection.Classes;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * A functional interface for setting field values using reflection and {@link sun.misc.Unsafe}.
 * This interface extends {@link TerConsumer} to handle exceptions and provide the required functionality.
 */
interface SetFieldValueFunction extends TerConsumer<Object, Field, Object> {

    /**
     * An implementation of {@link SetFieldValueFunction} for Java 7.
     * This implementation uses {@link sun.misc.Unsafe} to set values of fields in target objects.
     */
    class Java7 implements SetFieldValueFunction {
        protected sun.misc.Unsafe unsafe;

        /**
         * Constructs a {@code Java7} instance by obtaining the {@link sun.misc.Unsafe} instance
         * from the {@link UnsafeSupplier}.
         */
        Java7() {
            this.unsafe = Context.get(UnsafeSupplier.class).get();
        }

        /**
         * Sets the value of a specified field in a target object.
         * Uses {@link sun.misc.Unsafe} to handle both static and instance fields,
         * including primitive types and object references.
         *
         * @param target the target object on which the field value should be set. If the field is static, this parameter is ignored.
         * @param field the field whose value should be set
         * @param value the value to set in the field
         * @throws IllegalArgumentException if the value is incompatible with the field's type, or if the target object is null for non-static fields
         */
        @SuppressWarnings("DataFlowIssue")
        @Override
        public void accept(Object target, Field field, Object value) {
            if (value != null && !Classes.isAssignableFrom(field.getType(), value.getClass())) {
                throw new IllegalArgumentException("Value type " + value.getClass() + " is not assignable to field type " + field.getType());
            }

            Class<?> fieldDeclaringClass = field.getDeclaringClass();
            long fieldOffset;
            Object realTarget;

            if (Modifier.isStatic(field.getModifiers())) {
                fieldOffset = this.unsafe.staticFieldOffset(field);
                realTarget = fieldDeclaringClass;
            } else {
                realTarget = target;
                if (realTarget == null) {
                    throw new IllegalArgumentException("Target object is null");
                }
                Class<?> targetObjectClass = target.getClass();
                if (!Classes.isAssignableFrom(fieldDeclaringClass, targetObjectClass)) {
                    throw new IllegalArgumentException("Target object class " + targetObjectClass + " is not assignable to field declaring class " + fieldDeclaringClass);
                }
                fieldOffset = this.unsafe.objectFieldOffset(field);
            }

            Class<?> clazz = field.getType();
            if (!clazz.isPrimitive()) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putObject(realTarget, fieldOffset, value);
                } else {
                    this.unsafe.putObjectVolatile(realTarget, fieldOffset, value);
                }
            } else if (clazz == short.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putShort(realTarget, fieldOffset, (Short) value);
                } else {
                    this.unsafe.putShortVolatile(realTarget, fieldOffset, (Short) value);
                }
            } else if (clazz == int.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putInt(realTarget, fieldOffset, (Integer) value);
                } else {
                    this.unsafe.putIntVolatile(realTarget, fieldOffset, (Integer) value);
                }
            } else if (clazz == long.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putLong(realTarget, fieldOffset, (Long) value);
                } else {
                    this.unsafe.putLongVolatile(realTarget, fieldOffset, (Long) value);
                }
            } else if (clazz == float.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putFloat(realTarget, fieldOffset, (Float) value);
                } else {
                    this.unsafe.putFloatVolatile(realTarget, fieldOffset, (Float) value);
                }
            } else if (clazz == double.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putDouble(realTarget, fieldOffset, (Double) value);
                } else {
                    this.unsafe.putDoubleVolatile(realTarget, fieldOffset, (Double) value);
                }
            } else if (clazz == boolean.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putBoolean(realTarget, fieldOffset, (Boolean) value);
                } else {
                    this.unsafe.putBooleanVolatile(realTarget, fieldOffset, (Boolean) value);
                }
            } else if (clazz == byte.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putByte(realTarget, fieldOffset, (Byte) value);
                } else {
                    this.unsafe.putByteVolatile(realTarget, fieldOffset, (Byte) value);
                }
            } else if (clazz == char.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    this.unsafe.putChar(realTarget, fieldOffset, (Character) value);
                } else {
                    this.unsafe.putCharVolatile(realTarget, fieldOffset, (Character) value);
                }
            }
        }
    }
}
