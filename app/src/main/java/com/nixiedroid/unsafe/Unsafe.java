package com.nixiedroid.unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
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

    /**
     * crash java VM, showing new awesome type of Exception
     */
    public static void crashVM() {
        getUnsafe().getByte(0);
    }


    /**
     * throw Exception without requiring to add throws to class
     *
     * @param e Exception to be thrown
     */
    public static void throwException(Throwable e) {
        getUnsafe().throwException(e);
    }
    public static long getAddress(Object o){
        return 1;
    }

    public static void moveToJavaBase(Class cl){
        try {
            final Field field = Class.class.getDeclaredField("module");
            @SuppressWarnings("deprecation")
            final long offset = getUnsafe().objectFieldOffset(field);
            Unsafe.getUnsafe().putObject(cl, offset, Object.class.getModule());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createDummyInstance(Class<T> clazz) throws InstantiationException {
        return clazz.cast(getUnsafe().allocateInstance(clazz));
    }

}
