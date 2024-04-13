package com.nixiedroid.modules.models;

import com.nixiedroid.interfaces.ThrowableBiFunction;
import com.nixiedroid.modules.Context;
import com.nixiedroid.modules.util.Classes;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public abstract class HookClassFromBytes  implements ThrowableBiFunction<Class<?>,byte[],Class<?>> {
    protected MethodHandle defineHookClassMethodHandle;

    public static class ForJava7 extends HookClassFromBytes {
        protected sun.misc.Unsafe unsafe;

        public ForJava7() throws Throwable {
            this.unsafe = Context.i().getUnsafe();
            MethodHandles.Lookup l = Context.i().trustedLookup();
            MethodHandle p = Context.i().privateLookup();
            MethodType type = MethodType.methodType(Class.class, Class.class, byte[].class, Object[].class);
            this.defineHookClassMethodHandle = getLookup(l, p)
                    .findSpecial(this.unsafe.getClass(), "defineAnonymousClass", type, this.unsafe.getClass());
        }

        protected MethodHandles.Lookup getLookup(
                MethodHandles.Lookup lookup,
                MethodHandle privateLookupInMethodHandle
        ) throws Throwable {
            return (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(lookup, this.unsafe.getClass());
        }

        @Override
        public Class<?> apply(Class<?> clientClass, byte[] byteCode) throws Throwable {
            return (Class<?>) this.defineHookClassMethodHandle.invokeWithArguments(this.unsafe, clientClass, byteCode, null);
        }
    }

    public static class ForJava9 extends ForJava7 {

        public ForJava9() throws Throwable {
            super();
        }

        @Override
        protected MethodHandles.Lookup getLookup(MethodHandles.Lookup lookup, MethodHandle lookupMethod
        ) throws Throwable {
            return (MethodHandles.Lookup) lookupMethod.invokeWithArguments(this.unsafe.getClass(), lookup);
        }
    }

    public static class ForJava17 extends HookClassFromBytes {
        protected MethodHandle privateLookupInMethodHandle;
        protected MethodHandles.Lookup lookup;

        public ForJava17() throws NoSuchMethodException, IllegalAccessException {
            this.lookup = Context.i().trustedLookup();
            this.privateLookupInMethodHandle = Context.i().privateLookup();
            MethodType type = MethodType.methodType(Class.class, byte[].class);
            this.defineHookClassMethodHandle = this.lookup.findSpecial(MethodHandles.Lookup.class, "defineClass", type, MethodHandles.Lookup.class);
        }


        @Override
        public Class<?> apply(Class<?> clientClass, byte[] byteCode) throws Throwable {
            MethodHandles.Lookup pLookup = (MethodHandles.Lookup) privateLookupInMethodHandle.invokeWithArguments(clientClass, lookup);
            try {
                return (Class<?>) defineHookClassMethodHandle.invokeWithArguments(pLookup, byteCode);
            } catch (LinkageError exc) {
                try {
                    return Class.forName(Classes.getRealClassName(byteCode));
                } catch (Throwable excTwo) {
                    throw exc;
                }
            }
        }
    }
}
