package com.nixiedroid.modules.toolchain.fields;


import com.nixiedroid.modules.toolchain.Context;
import com.nixiedroid.modules.toolchain.UnsafeSupplier;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.function.BiFunction;

public class GetFieldValueFunction implements BiFunction<Object, Field, Object> {

    protected sun.misc.Unsafe unsafe;

    public GetFieldValueFunction() {
        this.unsafe = Context.get(UnsafeSupplier.class).get();
    }

    @Override
    public Object apply(Object target, Field field) {
        target = Modifier.isStatic(field.getModifiers()) ? field.getDeclaringClass() : target;
        long fieldOffset = Modifier.isStatic(field.getModifiers()) ? this.unsafe.staticFieldOffset(field) : this.unsafe.objectFieldOffset(field);
        Class<?> cls = field.getType();
        if (!cls.isPrimitive()) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getObject(target, fieldOffset);
            } else {
                return this.unsafe.getObjectVolatile(target, fieldOffset);
            }
        } else if (cls == short.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getShort(target, fieldOffset);
            } else {
                return this.unsafe.getShortVolatile(target, fieldOffset);
            }
        } else if (cls == int.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getInt(target, fieldOffset);
            } else {
                return this.unsafe.getIntVolatile(target, fieldOffset);
            }
        } else if (cls == long.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getLong(target, fieldOffset);
            } else {
                return this.unsafe.getLongVolatile(target, fieldOffset);
            }
        } else if (cls == float.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getFloat(target, fieldOffset);
            } else {
                return this.unsafe.getFloatVolatile(target, fieldOffset);
            }
        } else if (cls == double.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getDouble(target, fieldOffset);
            } else {
                return this.unsafe.getDoubleVolatile(target, fieldOffset);
            }
        } else if (cls == boolean.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getBoolean(target, fieldOffset);
            } else {
                return this.unsafe.getBooleanVolatile(target, fieldOffset);
            }
        } else if (cls == byte.class) {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getByte(target, fieldOffset);
            } else {
                return this.unsafe.getByteVolatile(target, fieldOffset);
            }
        } else {
            if (!Modifier.isVolatile(field.getModifiers())) {
                return this.unsafe.getChar(target, fieldOffset);
            } else {
                return this.unsafe.getCharVolatile(target, fieldOffset);
            }
        }
    }
}
