package com.nixiedroid.magic.parts;

import com.nixiedroid.magic.Context;
import io.github.toolfactory.jvm.util.Classes;
import io.github.toolfactory.jvm.util.Strings;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class SetFieldValue {
    public abstract void accept(Object origTarget, Field field, Object value);

    public static class ForJava7 extends SetFieldValue {
        final sun.misc.Unsafe unsafe;

        public ForJava7() {
            super();
            unsafe = Context.i().getUnsafe();
        }

        public void accept(Object origTarget, Field field, Object value) {
            if(value != null && !Classes.isAssignableFrom(field.getType(), value.getClass())) {
                throw new IllegalArgumentException(Strings.compile("Value {} is not assignable to {}", value , field.getName()));
            }
            Class<?> fieldDeclaringClass = field.getDeclaringClass();
            long fieldOffset;
            Object target;
            if (Modifier.isStatic(field.getModifiers())) {
                fieldOffset = unsafe.staticFieldOffset(field);
                target = fieldDeclaringClass;
            } else {
                if ((target = origTarget) == null) {
                    throw new IllegalArgumentException("Target object is null");
                }
                Class<?> targetObjectClass = target.getClass();
                if (!Classes.isAssignableFrom(fieldDeclaringClass, targetObjectClass)) {
                    throw new IllegalArgumentException("Target object class " + targetObjectClass + " is not assignable to " + fieldDeclaringClass);
                }
                fieldOffset = unsafe.objectFieldOffset(field);

            }
            Class<?> cls = field.getType();
            if(!cls.isPrimitive()) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putObject(target, fieldOffset, value);
                } else {
                    unsafe.putObjectVolatile(target, fieldOffset, value);
                }
            } else if (cls == short.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putShort(target, fieldOffset, ((Short)value).shortValue());
                } else {
                    unsafe.putShortVolatile(target, fieldOffset, ((Short)value).shortValue());
                }
            } else if (cls == int.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putInt(target, fieldOffset, ((Integer)value).intValue());
                } else {
                    unsafe.putIntVolatile(target, fieldOffset, ((Integer)value).intValue());
                }
            } else if (cls == long.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putLong(target, fieldOffset, ((Long)value).longValue());
                } else {
                    unsafe.putLongVolatile(target, fieldOffset, ((Long)value).longValue());
                }
            } else if (cls == float.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putFloat(target, fieldOffset, ((Float)value).floatValue());
                } else {
                    unsafe.putFloatVolatile(target, fieldOffset, ((Float)value).floatValue());
                }
            } else if (cls == double.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putDouble(target, fieldOffset, ((Double)value).doubleValue());
                } else {
                    unsafe.putDoubleVolatile(target, fieldOffset, ((Double)value).doubleValue());
                }
            } else if (cls == boolean.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putBoolean(target, fieldOffset, ((Boolean)value).booleanValue());
                } else {
                    unsafe.putBooleanVolatile(target, fieldOffset, ((Boolean)value).booleanValue());
                }
            } else if (cls == byte.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putByte(target, fieldOffset, ((Byte)value).byteValue());
                } else {
                    unsafe.putByteVolatile(target, fieldOffset, ((Byte)value).byteValue());
                }
            } else if (cls == char.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    unsafe.putChar(target, fieldOffset, ((Character)value).charValue());
                } else {
                    unsafe.putCharVolatile(target, fieldOffset, ((Character)value).charValue());
                }
            }
        }

    }
}
