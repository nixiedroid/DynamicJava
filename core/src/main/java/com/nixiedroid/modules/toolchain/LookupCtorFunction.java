package com.nixiedroid.modules.toolchain;

import com.nixiedroid.function.ThrowableFunction;
import com.nixiedroid.modules.toolchain.fields.GetFieldFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

public class LookupCtorFunction implements ThrowableFunction<Class<?>, MethodHandles.Lookup>, Tool {

    private final ThrowableFunction<Class<?>, MethodHandles.Lookup> func;

    public LookupCtorFunction() throws Throwable {
        if (thisVersionGEQ(JAVA_14)) {
            Constructor<?> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
            Context.get(SetAccessibleFunction.class).accept(lookupCtor, true);
            final MethodHandle mthHandle = ((MethodHandles.Lookup) lookupCtor.newInstance(MethodHandles.Lookup.class, null, -1)).findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, Class.class, int.class));
            this.func = clazz -> (MethodHandles.Lookup) mthHandle.invokeWithArguments(clazz, null, -1);
            return;
        } else if (thisVersionGEQ(JAVA_9)) {
            Constructor<MethodHandles.Lookup> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            Context.get(SetAccessibleFunction.class).accept(lookupCtor, true);
            final MethodHandle methodHandle = lookupCtor.newInstance(MethodHandles.Lookup.class, -1).findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, int.class));
            this.func = cls -> (MethodHandles.Lookup) methodHandle.invokeWithArguments(cls, -1);
            return;
        } else if (thisVersionGEQ(JAVA_7)) {
            Context.get(GetFieldFunction.class).apply(MethodHandles.Lookup.class, "allowedModes");
            this.func = var1 -> Context.get(LookupHook.class).apply(var1);
            return;
        }
        throw new IllegalArgumentException();
    }

    @Override
    public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
        return this.func.apply(clazz);
    }

}
