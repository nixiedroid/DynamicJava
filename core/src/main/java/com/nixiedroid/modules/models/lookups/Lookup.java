package com.nixiedroid.modules.models.lookups;

import com.nixiedroid.runtime.Info;
import com.nixiedroid.modules.Context;


import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;

public abstract class Lookup {
    protected MethodHandles.Lookup lookup = MethodHandles.lookup();

    public MethodHandles.Lookup getTrustedLookup() {
        return lookup;
    }

    public static class ForJava7 extends Lookup {
        public static final int TRUSTED = -1;

        public ForJava7() throws IllegalAccessException, NoSuchFieldException {
            Field modes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
            modes.setAccessible(true);
            modes.setInt(lookup, TRUSTED);
        }
    }

    public static class ForJava9 extends Lookup {
        public ForJava9() {
        }
    }
    public static class ForJava17 extends Lookup {
        public ForJava17() {
            final long allowedModesFieldMemoryOffset = Info.isIs64Bit() ? 12L : 8L;
            Context.i().getUnsafe().putInt(lookup, allowedModesFieldMemoryOffset, ForJava7.TRUSTED);
        }
    }
}

