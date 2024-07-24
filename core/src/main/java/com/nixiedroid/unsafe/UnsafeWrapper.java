package com.nixiedroid.unsafe;

import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * A utility class that provides access to the {@code sun.misc.Unsafe} API.
 * <p>
 * This class uses reflection and method handles to obtain the {@code Unsafe} instance and
 * offers various methods to interact with it. The {@code Unsafe} class is a powerful but
 * dangerous tool, and using it requires caution.
 * </p>
 * <p>
 * The methods provided here are intended for advanced use cases and should be used carefully
 * as they can bypass Java's safety checks.
 * </p>
 */
@SuppressWarnings("unused")
public final class UnsafeWrapper {

    private static final sun.misc.Unsafe theUnsafe;

    static {
        sun.misc.Unsafe unsafe = null;
        try {
            ClassLoader classLoader = ClassLoader.getSystemClassLoader();
            Class<?> unsafeClass = classLoader.loadClass("sun.misc.Unsafe");

            // Use MethodHandles to access the private Unsafe instance
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            lookup = MethodHandles.privateLookupIn(unsafeClass, lookup);

            MethodHandle methodHandle;
            try {
                // Attempt to get the static field "theUnsafe"
                methodHandle = lookup.findStaticGetter(unsafeClass, "theUnsafe", unsafeClass);
            } catch (NoSuchFieldException e) {
                // Fallback to finding the constructor
                methodHandle = lookup.findConstructor(unsafeClass, MethodType.methodType(void.class));
            }

            // Invoke the method handle to get the Unsafe instance
            try {
                unsafe = (Unsafe) methodHandle.invoke();
            } catch (Throwable ignored) {
            }
        } catch (ReflectiveOperationException e) {
            throw new Error("Failed to obtain Unsafe instance", e);
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

    /**
     * Causes the Java Virtual Machine (JVM) to crash by accessing invalid memory.
     * <p>
     * This method will typically cause a segmentation fault or similar JVM crash.
     * </p>
     */
    public static void crashVM() {
        getUnsafe().getByte(0);
    }

    /**
     * Throws an exception without requiring the caller to declare it in the method's throws clause.
     *
     * @param e The exception to be thrown.
     */
    public static void throwException(Throwable e) {
        getUnsafe().throwException(e);
    }

    /**
     * Sets the module of a class to the base module, effectively making it part of the Java base module.
     *
     * @param cl The class whose module is to be set to the base module.
     * @throws RuntimeException If the module field cannot be accessed.
     */
    @SuppressWarnings("deprecation")
    public static void moveToJavaBase(Class<?> cl) {
        try {
            Field moduleField = Class.class.getDeclaredField("module");
            long offset = getUnsafe().objectFieldOffset(moduleField);
            getUnsafe().putObject(cl, offset, Object.class.getModule());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Failed to access module field", e);
        }
    }

    /**
     * Creates a dummy instance of a class without invoking its constructor.
     *
     * @param clazz The class to create an instance of.
     * @param <T> The type of the class.
     * @return A new instance of the specified class.
     * @throws InstantiationException If the class cannot be instantiated.
     */
    public static <T> T createDummyInstance(Class<T> clazz) throws InstantiationException {
        return clazz.cast(getUnsafe().allocateInstance(clazz));
    }
}