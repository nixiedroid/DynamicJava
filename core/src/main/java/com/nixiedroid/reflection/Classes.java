package com.nixiedroid.reflection;

import com.nixiedroid.reflection.toolchain.SharedSecrets;

/**
 * A utility class that provides methods for working with Java classes.
 *
 * <p>This class provides functionality for checking if one class is assignable from another
 * and for converting primitive types to their corresponding wrapper classes.
 */
@SuppressWarnings("unused")
public final class Classes {

    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * <p>Throws an {@link Error} to indicate that instantiation is not allowed.
     */
    private Classes() {
        throw new Error("Cannot instantiate utility class");
    }

    /**
     * Checks if the class represented by {@code a} is assignable from the class represented by {@code b}.
     *
     * <p>This method first converts the classes to their corresponding wrapper types if they are primitive,
     * and then uses the {@link Class#isAssignableFrom(Class)} method to perform the check.
     *
     * @param a the class to check if it is assignable from {@code b}
     * @param b the class to check against {@code a}
     * @return {@code true} if {@code a} is assignable from {@code b}; {@code false} otherwise
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isAssignableFrom(Class<?> a, Class<?> b) {
        return getWrapper(a).isAssignableFrom(getWrapper(b));
    }

    /**
     * Returns the wrapper class for a given primitive type, or the class itself if it is not primitive.
     *
     * <p>If the given class is a primitive type (e.g., {@code int.class}), the method returns the corresponding
     * wrapper class (e.g., {@link java.lang.Integer}). If the given class is not a primitive type, it is returned as is.
     *
     * @param clazz the class to convert
     * @return the wrapper class if {@code clazz} is a primitive type; otherwise, {@code clazz}
     */
    public static Class<?> getWrapper(Class<?> clazz) {
        if (clazz.isPrimitive()) {
            if (clazz == short.class) {
                return Short.class;
            } else if (clazz == int.class) {
                return Integer.class;
            } else if (clazz == long.class) {
                return Long.class;
            } else if (clazz == float.class) {
                return Float.class;
            } else if (clazz == double.class) {
                return Double.class;
            } else if (clazz == boolean.class) {
                return Boolean.class;
            } else if (clazz == byte.class) {
                return Byte.class;
            } else if (clazz == char.class) {
                return Character.class;
            }
        }
        return clazz;
    }

    /**
     * Allocates an instance of the specified class
     * *
     * @param clazz the class of which to allocate an instance
     * @return a new instance of the specified class
     * @throws InstantiationException if the class cannot be instantiated
     */
    public static Object allocateInstance(Class<?> clazz) throws InstantiationException{
        return SharedSecrets.allocateInstance(clazz);
    }

    /**
     * Defines a class to the same class loader and in the same runtime package and
     * {@linkplain java.security.ProtectionDomain protection domain} as this lookup's
     * <p>
     * The {@code data} parameter is the class bytes of a valid class file (as defined
     * by the <em>The Java Virtual Machine Specification</em>) with a class name in the
     * same package as the lookup class. </p>
     */
    public static Class<?> defineClass(Class<?> hostClass, byte[] data) throws IllegalAccessException {
        return SharedSecrets.defineClass(hostClass,data);
    }


    /**
     * Retrieves a {@link Class} object by its name, with options to initialize it and specify
     * the class loader and caller class.
     *
     * @param name       The name of the class to retrieve.
     * @param initialize If true, the class will be initialized.
     * @param loader     The class loader to use.
     * @param caller     The caller class.
     * @return The {@link Class} object corresponding to the specified name.
     * @throws ClassNotFoundException If there is an error retrieving the class.
     */
    @SuppressWarnings("BooleanParameter")
    public static Class<?> getClassByName(String name, boolean initialize, ClassLoader loader, Class<?> caller) throws ClassNotFoundException {
        return SharedSecrets.getClassByName(name, initialize, loader, caller);
    }

    public static String getClassLoaderOf(Class<?> cl) {
        String clLdrName;
        if (cl.getClassLoader() == null) {
            clLdrName = "BootstrapClassLoader";
        } else {
            clLdrName = cl.getClassLoader().getClass().getSimpleName();
        }
        return "Classloader of " + cl.getName() + " : " + clLdrName;
    }


}