package com.nixiedroid.reflection.toolchain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public final class SharedSecrets {

    private SharedSecrets(){
        System.out.println("Nope =)");
        Context.get(UnsafeSupplier.class).get().getInt(0);
    }

    @SuppressWarnings("BooleanParameter")
    public static Class<?> getClassByName(String name, boolean initialize, ClassLoader loader, Class<?> caller) throws Throwable {
        return Context.get(GetClassByNameFunction.class).apply(
                name
                ,initialize,
                loader,caller);
    }

    public static MethodHandle findStatic(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return Context.get(TrustedLookupSupplier.class).get().findStatic(refc, name, type);
    }

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
