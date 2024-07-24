package com.nixiedroid.modules.toolchain.fields;


import com.nixiedroid.function.ThrowableTerConsumer;
import com.nixiedroid.modules.Classes;
import com.nixiedroid.modules.toolchain.Context;
import com.nixiedroid.modules.toolchain.UnsafeSupplier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class SetFieldValueFunction implements ThrowableTerConsumer<Object, Field, Object> {

    protected sun.misc.Unsafe unsafe;

    public SetFieldValueFunction() {
        this.unsafe = Context.get(UnsafeSupplier.class).get();
    }

    @SuppressWarnings("DataFlowIssue")
    @Override
    public void accept(Object target, Field field, Object value) {
        if (value != null && !Classes.isAssignableFrom(field.getType(), value.getClass())) {
            throw new IllegalArgumentException();
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
                throw new IllegalArgumentException("Target object class " + targetObjectClass + " is not assignable to " + fieldDeclaringClass);
            }
            fieldOffset = this.unsafe.objectFieldOffset(field);

        }
        Class<?> cls = field.getType();
        if (!cls.isPrimitive()) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putObject(realTarget, fieldOffset, value);
            } else {
                this.unsafe.putObjectVolatile(realTarget, fieldOffset, value);
            }
        } else if (cls == short.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putShort(realTarget, fieldOffset, ((Short) value));
            } else {
                this.unsafe.putShortVolatile(realTarget, fieldOffset, ((Short) value));
            }
        } else if (cls == int.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putInt(realTarget, fieldOffset, ((Integer) value));
            } else {
                this.unsafe.putIntVolatile(realTarget, fieldOffset, ((Integer) value));
            }
        } else if (cls == long.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putLong(realTarget, fieldOffset, ((Long) value));
            } else {
                this.unsafe.putLongVolatile(realTarget, fieldOffset, ((Long) value));
            }
        } else if (cls == float.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putFloat(realTarget, fieldOffset, ((Float) value));
            } else {
                this.unsafe.putFloatVolatile(realTarget, fieldOffset, ((Float) value));
            }
        } else if (cls == double.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putDouble(realTarget, fieldOffset, ((Double) value));
            } else {
                this.unsafe.putDoubleVolatile(realTarget, fieldOffset, ((Double) value));
            }
        } else if (cls == boolean.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putBoolean(realTarget, fieldOffset, ((Boolean) value));
            } else {
                this.unsafe.putBooleanVolatile(realTarget, fieldOffset, ((Boolean) value));
            }
        } else if (cls == byte.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putByte(realTarget, fieldOffset, ((Byte) value));
            } else {
                this.unsafe.putByteVolatile(realTarget, fieldOffset, ((Byte) value));
            }
        } else if (cls == char.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                this.unsafe.putChar(realTarget, fieldOffset, ((Character) value));
            } else {
                this.unsafe.putCharVolatile(realTarget, fieldOffset, ((Character) value));
            }
        }
    }


}
