package com.nixiedroid.modules.toolchain;

import com.nixiedroid.function.ThrowableQuadFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class GetClassByNameFunction implements ThrowableQuadFunction<String, Boolean, ClassLoader, Class<?>, Class<?>> {

    private final MethodHandle classFinder;

    public GetClassByNameFunction() throws Throwable {
        MethodHandles.Lookup l = Context.get(LookupCtorFunction.class).apply(Class.class);
        this.classFinder = l.findStatic(Class.class, "forName0", MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class));
    }

    @Override
    public Class<?> apply(String name, Boolean initialize, ClassLoader loader, Class<?> caller) throws Throwable {
        return (Class<?>) this.classFinder.invoke(name, initialize, loader, caller);
    }
}
