package com.nixiedroid.reflection;

/**
 * A utility class that provides methods for working with Java classes.
 *
 * <p>This class provides functionality for checking if one class is assignable from another
 * and for converting primitive types to their corresponding wrapper classes.
 */
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
}