package com.nixiedroid.reflection.toolchain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.function.Supplier;

@SuppressWarnings("unused")
interface PrivateLookupSupplier extends Supplier<MethodHandle> {

    abstract class Default implements PrivateLookupSupplier {
        protected MethodHandle methodHandle;

        @Override
        public MethodHandle get() {
            return this.methodHandle;
        }
    }

    class Java7 extends Default {
        Java7() throws Throwable {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            this.methodHandle = lookup.findSpecial(MethodHandles.Lookup.class, "in", MethodType.methodType(MethodHandles.Lookup.class, Class.class), MethodHandles.Lookup.class);
        }
    }

    class Java9 extends Default {
        Java9() throws Throwable {
            MethodHandles.Lookup lookup = Context.get(TrustedLookupSupplier.class).get();
            this.methodHandle = lookup.findStatic(MethodHandles.class, "privateLookupIn", MethodType.methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class));
        }
    }

}
