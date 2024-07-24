package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

@SuppressWarnings({"unused"})

interface LookupCtorFunction extends ThrowableFunction<Class<?>, MethodHandles.Lookup> {

    abstract class Default implements LookupCtorFunction {
        protected ThrowableFunction<Class<?>, MethodHandles.Lookup> func;

        @Override
        public MethodHandles.Lookup apply(Class<?> clazz) throws Throwable {
            return this.func.apply(clazz);
        }
    }

    class Java7 extends Default {
        Java7() throws Throwable {
            Context.get(GetFieldFunction.class).apply(MethodHandles.Lookup.class, "allowedModes");
            this.func = var1 -> Context.get(LookupHook.class).apply(var1);
        }
    }

    class Java9 extends Default {
        Java9() throws Throwable {
            Constructor<MethodHandles.Lookup> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            Context.get(SetAccessibleFunction.class).accept(lookupCtor, true);
            final MethodHandle methodHandle = lookupCtor.newInstance(MethodHandles.Lookup.class, -1).findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, int.class));
            this.func = clazz -> (MethodHandles.Lookup) methodHandle.invokeWithArguments(clazz, -1);
        }
    }

    class Java14 extends Default {
        @SuppressWarnings({"JavaReflectionMemberAccess", "JavaLangInvokeHandleSignature"})
        Java14() throws Throwable {
            Constructor<?> lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
            Context.get(SetAccessibleFunction.class).accept(lookupCtor, true);
            final MethodHandle mthHandle = ((MethodHandles.Lookup) lookupCtor.newInstance(MethodHandles.Lookup.class, null, -1)).findConstructor(MethodHandles.Lookup.class, MethodType.methodType(void.class, Class.class, Class.class, int.class));
            this.func = clazz -> (MethodHandles.Lookup) mthHandle.invokeWithArguments(clazz, null, -1);
        }
    }
}
