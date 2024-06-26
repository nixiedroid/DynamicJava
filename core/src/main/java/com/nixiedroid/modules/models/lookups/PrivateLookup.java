package com.nixiedroid.modules.models.lookups;

import com.nixiedroid.modules.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public abstract class PrivateLookup {
    protected MethodHandle methodHandle;

    public MethodHandle get() {
        return methodHandle;
    }

    public static class ForJava7 extends PrivateLookup {

        public ForJava7() throws NoSuchMethodException, IllegalAccessException {
            MethodType type = MethodType
                    .methodType(MethodHandles.Lookup.class, Class.class);
            methodHandle = Context.i()
                    .trustedLookup()
                    .findSpecial(MethodHandles.Lookup.class, "in", type, MethodHandles.Lookup.class);
        }
    }


    public static class ForJava9 extends PrivateLookup {

        public ForJava9() throws NoSuchMethodException, IllegalAccessException {
            MethodType type = MethodType
                    .methodType(MethodHandles.Lookup.class, Class.class, MethodHandles.Lookup.class);
            methodHandle = Context.i()
                    .trustedLookup()
                    .findStatic(MethodHandles.class, "privateLookupIn", type);
        }

    }
}
