package com.nixiedroid.waytwo.magic.parts;

import com.nixiedroid.waytwo.magic.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public abstract class FieldsFinder {
    protected MethodHandle getDeclaredFields0;

    public MethodHandle get() {
        return getDeclaredFields0;
    }

    public static class ForJava7 extends FieldsFinder {

        public ForJava7() throws NoSuchMethodException, IllegalAccessException {
            MethodType type = MethodType.methodType(Field[].class, boolean.class);

            getDeclaredFields0 = Context.i().lookup().findSpecial(Class.class, "getDeclaredFields0", type, Class.class

            );
        }
    }
}
