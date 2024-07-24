package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableQuadFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

interface GetClassByNameFunction extends ThrowableQuadFunction<String, Boolean, ClassLoader, Class<?>, Class<?>> {

    class Java7 implements GetClassByNameFunction{
        private final MethodHandle classFinder;

        Java7() throws Throwable {
            MethodHandles.Lookup l = Context.get(LookupCtorFunction.class).apply(Class.class);
            this.classFinder = l.findStatic(Class.class, "forName0", MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class));
        }
        @Override
        public Class<?> apply(String name, Boolean initialize, ClassLoader loader, Class<?> caller) throws Throwable {
            return (Class<?>) this.classFinder.invoke(name, initialize, loader, caller);
        }
    }
}
