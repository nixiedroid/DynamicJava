package com.nixiedroid.modules.util;

import com.nixiedroid.modules.Context;
import org.jetbrains.annotations.NotNull;
import java.lang.reflect.Field;

public class Fields {

    public static <T> T getDirect(Object target, Field field) {
        return Context.i().getFieldValue(target, field);
    }

    public static <T> T getDirect(@NotNull Object target, String fieldName) throws Throwable {
        Field f = Context.i().getField(target.getClass(), fieldName);
        Context.i().setAccessible(f, true);
        return getDirect(target, f);
    }

    public static <T> T getStaticDirect(Class<?> targetClass, String fieldName) throws Throwable {
        Field f = Context.i().getField(targetClass, fieldName);
        Context.i().setAccessible(f, true);
        return getDirect(null, f);
    }

    public static void setDirect(Object target, Field field, Object value) {
        Context.i().setFieldValue(target, field, value);
    }

    private static void setDirect(Class<?> targetClass, Object target, String fieldName, Object value) throws Throwable {
        Field f = Context.i().getField(targetClass, fieldName);
        Context.i().setAccessible(f, true);
        setDirect(target, f, value);
    }

    public static void setDirect(Object target, String fieldName, Object value) throws Throwable {
        setDirect(Classes.retrieveFrom(target), target, fieldName, value);
    }
}
