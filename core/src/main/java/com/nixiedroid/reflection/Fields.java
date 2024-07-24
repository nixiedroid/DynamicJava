package com.nixiedroid.reflection;

import com.nixiedroid.reflection.toolchain.SharedSecrets;

import java.lang.reflect.Field;

public final class Fields {

    private Fields(){

    }

    public static Field getField(Class<?> clazz, String fieldName) throws Throwable {
        return SharedSecrets.getField(clazz,fieldName);
    }

    public static Object getFieldData(Object obj, Field f) {
        return SharedSecrets.getFieldData(obj,f);
    }

    public static void setFieldData(Object obj, Field f, Object data) {
        SharedSecrets.setFieldData(obj,f,data);
    }
}
