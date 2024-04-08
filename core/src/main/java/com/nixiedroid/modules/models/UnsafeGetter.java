package com.nixiedroid.modules.models;

import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public abstract class UnsafeGetter {
    protected sun.misc.Unsafe unsafe;

    public sun.misc.Unsafe get(){
        return unsafe;
    }
    public static class ForJava7 extends UnsafeGetter {
        public ForJava7() {
           unsafe = getUnsafe();
        }
    }
    private static Unsafe getUnsafe(){
        sun.misc.Unsafe unsafe;
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            try {
                Constructor<Unsafe> unsafeConstructor = sun.misc.Unsafe.class.getDeclaredConstructor();
                unsafeConstructor.setAccessible(true);
                unsafe = unsafeConstructor.newInstance();
                unsafeConstructor.setAccessible(false);
            } catch (Exception ex) {
                throw new RuntimeException(e + " : " + ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return unsafe;
    }
}
