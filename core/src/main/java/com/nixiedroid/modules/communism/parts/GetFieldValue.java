package com.nixiedroid.modules.communism.parts;

import com.nixiedroid.modules.communism.Context;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;

public abstract class GetFieldValue {
    public abstract Object accept(Object target, Field field);
    public static class ForJava7 extends GetFieldValue {
        final sun.misc.Unsafe unsafe;

        public ForJava7() {
            unsafe = Context.i().getUnsafe();
        }

        @Override
        public Object accept(Object target, Field field) {
            target = Modifier.isStatic(field.getModifiers())?
                    field.getDeclaringClass() :
                    target;
            long fieldOffset = Modifier.isStatic(field.getModifiers())?
                    unsafe.staticFieldOffset(field) :
                    unsafe.objectFieldOffset(field);
            Class<?> cls = field.getType();
            if(!cls.isPrimitive()) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return unsafe.getObject(target, fieldOffset);
                } else {
                    return unsafe.getObjectVolatile(target, fieldOffset);
                }
            } else if (cls == short.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Short.valueOf(unsafe.getShort(target, fieldOffset));
                } else {
                    return Short.valueOf(unsafe.getShortVolatile(target, fieldOffset));
                }
            } else if (cls == int.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Integer.valueOf(unsafe.getInt(target, fieldOffset));
                } else {
                    return Integer.valueOf(unsafe.getIntVolatile(target, fieldOffset));
                }
            } else if (cls == long.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Long.valueOf(unsafe.getLong(target, fieldOffset));
                } else {
                    return Long.valueOf(unsafe.getLongVolatile(target, fieldOffset));
                }
            } else if (cls == float.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Float.valueOf(unsafe.getFloat(target, fieldOffset));
                } else {
                    return Float.valueOf(unsafe.getFloatVolatile(target, fieldOffset));
                }
            } else if (cls == double.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Double.valueOf(unsafe.getDouble(target, fieldOffset));
                } else {
                    return Double.valueOf(unsafe.getDoubleVolatile(target, fieldOffset));
                }
            } else if (cls == boolean.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Boolean.valueOf(unsafe.getBoolean(target, fieldOffset));
                } else {
                    return Boolean.valueOf(unsafe.getBooleanVolatile(target, fieldOffset));
                }
            } else if (cls == byte.class) {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Byte.valueOf(unsafe.getByte(target, fieldOffset));
                } else {
                    return Byte.valueOf(unsafe.getByteVolatile(target, fieldOffset));
                }
            } else {
                if (!Modifier.isVolatile(field.getModifiers())) {
                    return Character.valueOf(unsafe.getChar(target, fieldOffset));
                } else {
                    return Character.valueOf(unsafe.getCharVolatile(target, fieldOffset));
                }
            }
        }
    }
}