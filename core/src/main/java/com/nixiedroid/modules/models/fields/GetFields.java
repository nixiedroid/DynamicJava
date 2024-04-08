package com.nixiedroid.modules.models.fields;

import com.nixiedroid.modules.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public abstract class GetFields {
    protected MethodHandle getDeclaredFields0;

    public abstract MethodHandle getFields();

    public static class ForJava7 extends GetFields {

        public ForJava7() throws Throwable {
            MethodType type = MethodType.methodType(Field[].class, boolean.class);
            MethodHandles.Lookup sLookup = Context.i().targetedLookup(Class.class);
            getDeclaredFields0 = sLookup.findSpecial(Class.class, "getDeclaredFields0", type, Class.class);
        }

        @Override
        public MethodHandle getFields() {
            return getDeclaredFields0;
        }
    }
}
