package com.nixiedroid.urlWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class Unsafe {
    private static final sun.misc.Unsafe theUnsafestThingyInJava;


    static {
        sun.misc.Unsafe unsafe;
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            try {
                Constructor<sun.misc.Unsafe> unsafeConstructor = sun.misc.Unsafe.class.getDeclaredConstructor();
                unsafeConstructor.setAccessible(true);
                unsafe = unsafeConstructor.newInstance();
                unsafeConstructor.setAccessible(false);
            } catch (Exception ex) {
                throw new RuntimeException(e + " : " + ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        theUnsafestThingyInJava = unsafe;
    }

    private Unsafe() {
        throw new UnsupportedOperationException("Unable to create instance of utility class");
    }

    static sun.misc.Unsafe getUnsafe() {
        return theUnsafestThingyInJava;
    }


    public static <T> void moveToJavaBase(Class<T> cl){
        try {
            final Field field = Class.class.getDeclaredField("module");
            @SuppressWarnings("deprecation")
            final long offset = getUnsafe().objectFieldOffset(field);
            getUnsafe().putObject(cl, offset, Object.class.getModule());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

}