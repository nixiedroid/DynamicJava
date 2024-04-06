package com.nixiedroid.clazz;

import com.nixiedroid.Context;

import java.lang.reflect.Field;

public class Fields {
    public static <T> T getStaticDirect(Field field) {
        return getDirect(null, field);
    }

    public static <T> T getDirect(Object target, Field field) {
        return Context.i().getFieldValue(target, field);
    }

    public static <T> T getStaticDirect(Class<?> targetClass, String fieldName) {
        Field f = Context.i().getField(targetClass, fieldName);
        Context.i().setAccessible(f, true);
        return getStaticDirect(f);
    }

    public static void setDirect(Object target, Field field, Object value) {
        Context.i().setFieldValue(target, field, value);
    }

    private static void setDirect(Class<?> targetClass, Object target, String fieldName, Object value) {
        Field f = Context.i().getField(targetClass, fieldName);
        Context.i().setAccessible(f, true);
        setDirect(target, f, value);
    }

    public static void setDirect(Object target, String fieldName, Object value) {
        setDirect(Classes.retrieveFrom(target), target, fieldName, value);
    }

    public static <T> T getDirect(Object target, String fieldName) {
        Field f = Context.i().getField(target.getClass(), fieldName);
        Context.i().setAccessible(f, true);
        return getDirect(target, f);
    }

}
