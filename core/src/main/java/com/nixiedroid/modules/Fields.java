package com.nixiedroid.modules;

import com.nixiedroid.modules.toolchain.Context;
import com.nixiedroid.modules.toolchain.ForName0Function;
import com.nixiedroid.modules.toolchain.GetDeclaredFields;
import com.nixiedroid.modules.toolchain.TrustedLookupSupplier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Fields {

    private static final MethodHandle objectFieldOffset;
    private static final MethodHandle staticFieldOffset;
    private static final MethodHandle getObject;
    private static final MethodHandle putObject;
    private static final Object internalUnsafe;

    private Fields(){

    }

    static {
        try {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            Class<?> intUnsafeClass = Context.get(ForName0Function.class).apply("jdk.internal.misc.Unsafe");
            internalUnsafe = lookup.findStaticVarHandle(sun.misc.Unsafe.class, "theInternalUnsafe", intUnsafeClass).get();
            MethodType mt = MethodType.methodType(long.class, Field.class);
            objectFieldOffset = lookup.findVirtual(internalUnsafe.getClass(), "objectFieldOffset", mt);
            mt = MethodType.methodType(long.class, Field.class);
            staticFieldOffset = lookup.findVirtual(internalUnsafe.getClass(), "staticFieldOffset", mt);
            mt = MethodType.methodType(long.class, Field.class);
            getObject = lookup.findVirtual(internalUnsafe.getClass(), "getObject", mt);
            mt = MethodType.methodType(long.class, Field.class);
            putObject = lookup.findVirtual(internalUnsafe.getClass(), "putObject", mt);
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Class<?> clazz, String fieldName) throws Throwable {
        Field[] fields = Context.get(GetDeclaredFields.class).apply(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new NoSuchFieldException();
    }

    public static Object getFieldData(Object obj, Field f) throws Throwable {
        Long offset;
        if (Modifier.isStatic(f.getModifiers())) {
            offset = (Long) staticFieldOffset.invoke(internalUnsafe, f);
        } else {
            offset = (Long) objectFieldOffset.invoke(internalUnsafe, f);
        }
        return getObject.invoke(obj, offset);
    }

    public static void setFieldData(Object obj, Field f, Object data) throws Throwable {
        Long offset;
        if (Modifier.isStatic(f.getModifiers())) {
            offset = (Long) staticFieldOffset.invoke(internalUnsafe, f);
        } else {
            offset = (Long) objectFieldOffset.invoke(internalUnsafe, f);
        }
        putObject.invoke(obj, offset, data);
    }


}
