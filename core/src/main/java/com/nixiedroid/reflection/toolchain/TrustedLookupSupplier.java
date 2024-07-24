package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.runtime.Properties;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.function.Supplier;

@SuppressWarnings("unused")
interface TrustedLookupSupplier extends Supplier<MethodHandles.Lookup>  {

    abstract class Default implements TrustedLookupSupplier{
        protected final MethodHandles.Lookup lookup = MethodHandles.lookup();
        protected static final int TRUSTED = -1;
        @Override
        public MethodHandles.Lookup get() {
            return this.lookup;
        }
    }

    class Java7 extends Default {
        Java7() throws Throwable {
            Field modes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            modes.setAccessible(true);
            modes.setInt(this.lookup, TRUSTED);
        }
    }

    class Java17 extends Default {
        Java17() {
            sun.misc.Unsafe unsafe = Context.get(UnsafeSupplier.class).get();
            final long allowedModesFieldMemoryOffset = Properties.is64Bit() ? 12L : 8L;
            unsafe.putInt(this.lookup, allowedModesFieldMemoryOffset, TRUSTED);
        }
    }
}
