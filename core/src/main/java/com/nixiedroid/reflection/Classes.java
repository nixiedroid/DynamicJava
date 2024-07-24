package com.nixiedroid.reflection;

public final class Classes {

    private Classes(){
        throw new Error();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isAssignableFrom(Class<?> a, Class<?> b) {
        return getWrapper(a).isAssignableFrom(getWrapper(b));
    }

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
