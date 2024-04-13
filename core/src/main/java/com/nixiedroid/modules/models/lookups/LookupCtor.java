package com.nixiedroid.modules.models.lookups;

import com.nixiedroid.interfaces.ThrowableFunction;
import com.nixiedroid.modules.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;

public abstract class LookupCtor  implements ThrowableFunction<Class<?>,MethodHandles.Lookup> {
    protected MethodHandle methodHandle;
    public static class ForJava7 extends LookupCtor {
        public ForJava7() throws Throwable {
            Context.i().getField(MethodHandles.Lookup.class, "allowedModes");
        }

        @Override
        public MethodHandles.Lookup apply(Class<?> input) throws Throwable {
            return  Context.i().targetedLookup(input);
        }

    }

    public static class ForJava9 extends LookupCtor {

        public ForJava9() throws Throwable {
            Context.i().getField(MethodHandles.Lookup.class, "allowedModes");
            Constructor<MethodHandles.Lookup> lookupCtor;
            lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, int.class);
            Context.i().setAccessible(lookupCtor, true);
            MethodType mt = MethodType.methodType(void.class, Class.class, int.class);
            this.methodHandle = lookupCtor.newInstance(MethodHandles.Lookup.class, Lookup.ForJava7.TRUSTED)
                    .findConstructor(MethodHandles.Lookup.class, mt);
        }
        @Override
        public MethodHandles.Lookup apply(Class<?> input) throws Throwable{
            return (MethodHandles.Lookup) this.methodHandle.invokeWithArguments(input, Lookup.ForJava7.TRUSTED);

        }
    }

    public static class ForJava14 extends LookupCtor {
        public ForJava14() throws Throwable {
            Context.i().getField(MethodHandles.Lookup.class, "allowedModes");
            Constructor<MethodHandles.Lookup> lookupCtor;
            lookupCtor = MethodHandles.Lookup.class.getDeclaredConstructor(Class.class, Class.class, int.class);
            Context.i().setAccessible(lookupCtor, true);
            MethodType mt = MethodType.methodType(void.class, Class.class, Class.class, int.class);
            this.methodHandle = lookupCtor.newInstance(MethodHandles.Lookup.class, null, Lookup.ForJava7.TRUSTED)
                    .findConstructor(MethodHandles.Lookup.class,mt);
        }
        @Override
        public MethodHandles.Lookup apply(Class<?> input) throws Throwable {
            return (MethodHandles.Lookup) this.methodHandle.invokeWithArguments(input, null, Lookup.ForJava7.TRUSTED);
        }
    }
}
