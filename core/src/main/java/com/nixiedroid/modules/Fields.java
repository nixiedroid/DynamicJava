package com.nixiedroid.modules;

import com.nixiedroid.modules.toolchain.Context;
import com.nixiedroid.modules.toolchain.fields.GetFieldFunction;
import com.nixiedroid.modules.toolchain.fields.GetFieldValueFunction;
import com.nixiedroid.modules.toolchain.fields.SetFieldValueFunction;

import java.lang.reflect.Field;

public class Fields {

    public static Field getField(Class<?> clazz, String fieldName) throws Throwable {
        return Context.get(GetFieldFunction.class).apply(clazz, fieldName);
    }

    public static Object getFieldData(Object obj, Field f) {
        return Context.get(GetFieldValueFunction.class).apply(obj, f);
    }

    public static void setFieldData(Object obj, Field f, Object data) {
        Context.get(SetFieldValueFunction.class).accept(obj, f, data);
    }


}
