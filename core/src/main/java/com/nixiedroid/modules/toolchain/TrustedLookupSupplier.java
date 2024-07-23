package com.nixiedroid.modules.toolchain;

import com.nixiedroid.runtime.Properties;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.function.Supplier;

public class TrustedLookupSupplier implements Supplier<MethodHandles.Lookup> {

    public static final int TRUSTED = -1;
    private final MethodHandles.Lookup lookup = MethodHandles.lookup();;

    public TrustedLookupSupplier() throws NoSuchFieldException, IllegalAccessException {
        int javaVersion = Properties.getVersion();
        if (javaVersion >= 17) {
            sun.misc.Unsafe unsafe = Context.get(UnsafeSupplier.class).get();
            final long allowedModesFieldMemoryOffset =
                    Properties.is64Bit() ? 12L : 8L;
            unsafe.putInt(this.lookup, allowedModesFieldMemoryOffset, TRUSTED);
        } else if (javaVersion >=9){

        } else if (javaVersion >=7){
            Field modes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            modes.setAccessible(true);
            modes.setInt(this.lookup, TRUSTED);
        }
    }

    @Override
    public MethodHandles.Lookup get() {
        return this.lookup;
    }
}
