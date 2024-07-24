package com.nixiedroid.unsafe;

import com.nixiedroid.exceptions.Thrower;
import sun.misc.Unsafe;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

/**
 * A utility class that provides access to the {@code sun.misc.Unsafe} API.
 * <p>
 * This class uses reflection and method handles to obtain the {@code Unsafe} instance
 * </p>
 */

public final class UnsafeWrapper {

    private static final sun.misc.Unsafe theUnsafe;

    static {
        sun.misc.Unsafe unsafe = null;
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> unsafeClass = classLoader.loadClass("sun.misc.Unsafe");
            try {
                Field f = unsafeClass.getDeclaredField("theUnsafe");
                f.setAccessible(true);
                unsafe = (Unsafe) f.get(null);
            } catch (NoSuchFieldException e) {
                Constructor<?> ctor = unsafeClass.getDeclaredConstructor();
                ctor.setAccessible(true);
                unsafe = (Unsafe) ctor.newInstance();
            }
        } catch (ReflectiveOperationException e) {
            Thrower.throwExceptionAndDie(e);
        }
        theUnsafe = unsafe;
    }

    // Private constructor to prevent instantiation
    private UnsafeWrapper() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Returns the {@code Unsafe} instance.
     *
     * @return The {@code Unsafe} instance.
     */
    public static sun.misc.Unsafe getUnsafe() {
        return theUnsafe;
    }
}