package com.nixiedroid.unsafe;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class UnsafeWrapper {
    private static final sun.misc.Unsafe theUnsafe;

    static {
        sun.misc.Unsafe unsafe = null;
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            Class<?> Uclass = cl.loadClass("sun.misc.Unsafe");
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            lookup = MethodHandles.privateLookupIn(Uclass, lookup);
            MethodHandle mh;
            try {
                mh = lookup.findStaticGetter(Uclass, "theUnsafe", Uclass);
            } catch (NoSuchFieldException e) {
                mh = lookup.findConstructor(Uclass, MethodType.methodType(void.class));
            }
            try {
                unsafe = (Unsafe) mh.invoke();
            } catch (Throwable ignored) {
            }
        } catch (ReflectiveOperationException e) {
            throw new Error("Unsafe is removed" + e);
        }
        theUnsafe = unsafe;
    }

    private UnsafeWrapper() {
        throw new UnsupportedOperationException("Unable to create instance of utility class");
    }

    public static sun.misc.Unsafe getUnsafe() {
        return theUnsafe;
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

    public static long getAddress(Object o) {
        return 1;
    }

    public static void moveToJavaBase(Class<?> cl) {
        try {
            final Field field = Class.class.getDeclaredField("module");
            @SuppressWarnings("deprecation") final long offset = getUnsafe().objectFieldOffset(field);
            UnsafeWrapper.getUnsafe().putObject(cl, offset, Object.class.getModule());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T createDummyInstance(Class<T> clazz) throws InstantiationException {
        return clazz.cast(getUnsafe().allocateInstance(clazz));
    }

    public static class Hook {
        public void whereAmI() {
            System.out.println(this.getClass().getModule().getName());
        }

        public MethodHandles.Lookup getLookup(Class<?> cl) throws Throwable {
            ClassLoader loader = ClassLoader.getSystemClassLoader();
            Class<?> intUnsafeClass = loader.loadClass(cl.getName());
            return MethodHandles.privateLookupIn(intUnsafeClass, MethodHandles.lookup());
        }

    }

}

