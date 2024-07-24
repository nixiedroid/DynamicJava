package com.nixiedroid.modules;

public class Classes {
    public static boolean isAssignableFrom(Class<?> a, Class<?> b) {
        return getWrapper(a).isAssignableFrom(getWrapper(b));
    }

    public static Class<?> getWrapper(Class<?> cls) {
        if (cls.isPrimitive()) {
            if (cls == short.class) {
                return Short.class;
            } else if (cls == int.class) {
                return Integer.class;
            } else if (cls == long.class) {
                return Long.class;
            } else if (cls == float.class) {
                return Float.class;
            } else if (cls == double.class) {
                return Double.class;
            } else if (cls == boolean.class) {
                return Boolean.class;
            } else if (cls == byte.class) {
                return Byte.class;
            } else if (cls == char.class) {
                return Character.class;
            }
        }
        return cls;
    }
}
