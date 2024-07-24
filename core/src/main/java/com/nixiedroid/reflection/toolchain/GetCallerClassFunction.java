package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableSupplier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

@SuppressWarnings("unused")
interface GetCallerClassFunction extends ThrowableSupplier<Class<?>> {

    class Java7 implements GetCallerClassFunction {

        private final MethodHandle getCaller;

        Java7() throws Throwable {
            Class<?> reflectionClass = Context.get(GetClassByNameFunction.class).apply(
                    "jdk.internal.reflect.Reflection",
                    false,
                    Java7.class.getClassLoader(),
                    Class.class
            );
            MethodHandles.Lookup l = Context.get(LookupCtorFunction.class).apply(reflectionClass);
            this.getCaller = l.findStatic(
                    reflectionClass,
                    "getCallerClass",
                    MethodType.methodType(Class.class));
        }


        @Override
        public Class<?> get() throws Throwable {
            return (Class<?>) this.getCaller.invoke();
        }
    }

}
