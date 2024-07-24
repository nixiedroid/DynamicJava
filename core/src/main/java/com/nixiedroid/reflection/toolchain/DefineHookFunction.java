package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.classes.JavaClassParser;
import com.nixiedroid.function.ThrowableBiFunction;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;

@SuppressWarnings({"unused", "ClassTooDeepInInheritanceTree"})
interface DefineHookFunction extends ThrowableBiFunction<Class<?>, byte[], Class<?>> {

    abstract class Default implements DefineHookFunction {

        protected MethodHandle defineHook;

    }

    class Java17 extends Default {

        private final MethodHandle privateLookupIn;
        private final MethodHandles.Lookup lookup;

        Java17() throws Throwable {
            this.lookup = Context.get(TrustedLookupSupplier.class).get();
            this.defineHook = this.lookup.findSpecial(MethodHandles.Lookup.class, "defineClass", MethodType.methodType(Class.class, byte[].class), MethodHandles.Lookup.class);
            this.privateLookupIn = Context.get(PrivateLookupSupplier.class).get();
        }

        @Override
        public Class<?> apply(Class<?> clazz, byte[] bytes) throws Throwable {
            MethodHandles.Lookup targetedLookup = (MethodHandles.Lookup) this.privateLookupIn.invokeWithArguments(clazz, this.lookup);
            try {
                return (Class<?>) this.defineHook.invokeWithArguments(targetedLookup, bytes);
            } catch (LinkageError exc) {
                try {
                    return Class.forName(JavaClassParser.create(ByteBuffer.wrap(bytes)).getName());
                } catch (Throwable excTwo) {
                    throw exc;
                }
            }
        }
    }

    class Java7 extends Default {
        protected final sun.misc.Unsafe U;

        Java7() throws Throwable {
            this.U = Context.get(UnsafeSupplier.class).get();
            this.defineHook = retrieveHook(Context.get(TrustedLookupSupplier.class).get(), Context.get(PrivateLookupSupplier.class).get()).findSpecial(this.U.getClass(), "defineAnonymousClass", MethodType.methodType(Class.class, Class.class, byte[].class, Object[].class), this.U.getClass());
        }

        protected MethodHandles.Lookup retrieveHook(MethodHandles.Lookup lookup, MethodHandle privateLookupIn) throws Throwable {
            return (MethodHandles.Lookup) privateLookupIn.invokeWithArguments(lookup, this.U.getClass());
        }

        @Override
        public Class<?> apply(Class<?> clazz, byte[] bytes) throws Throwable {
            return (Class<?>) this.defineHook.invokeWithArguments(this.U, clazz, bytes, null);
        }
    }


    class Java9 extends Java7 {

        Java9() throws Throwable {
            super();
        }

        @Override
        protected MethodHandles.Lookup retrieveHook(MethodHandles.Lookup lookup, MethodHandle privateLookupIn) throws Throwable {
            return (MethodHandles.Lookup) privateLookupIn.invokeWithArguments(this.U.getClass(), lookup);
        }
    }
}
