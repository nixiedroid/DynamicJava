package com.nixiedroid.modules.toolchain;

import com.nixiedroid.function.ThrowableFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class GetDeclaredFieldsFunction implements ThrowableFunction<Class<?>, Field[]>, Tool {

    private final MethodHandle getFields0;

    public GetDeclaredFieldsFunction() throws Throwable {
        LookupHook getConsulterFunction = Context.get(LookupHook.class);
        MethodHandles.Lookup lookup = getConsulterFunction.apply(Class.class);
        this.getFields0 = lookup.findSpecial(Class.class, "getDeclaredFields0", MethodType.methodType(Field[].class, boolean.class), Class.class);
    }

    @Override
    public Field[] apply(Class<?> clazz) throws Throwable {
        return (Field[]) this.getFields0.invokeWithArguments(clazz, false);
    }
}
