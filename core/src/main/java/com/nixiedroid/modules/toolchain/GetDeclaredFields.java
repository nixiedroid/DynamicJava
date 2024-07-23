package com.nixiedroid.modules.toolchain;

import com.nixiedroid.function.ThrowableFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public class GetDeclaredFields  implements ThrowableFunction<Class<?>, Field[]>,Tool {

    private final MethodHandle getFields0;

    public GetDeclaredFields() throws NoSuchMethodException, IllegalAccessException {
        LookupHook getConsulterFunction = Context.get(LookupHook.class);
        MethodHandles.Lookup consulter = getConsulterFunction.apply(Class.class);
        this.getFields0 = consulter.findSpecial(
                Class.class,
                "getDeclaredFields0",
                MethodType.methodType(Field[].class, boolean.class),
                Class.class
        );
    }

    @Override
    public Field[] apply(Class<?> clazz) throws Throwable {
        return (Field[]) this.getFields0.invokeWithArguments(clazz, false);
    }
}
