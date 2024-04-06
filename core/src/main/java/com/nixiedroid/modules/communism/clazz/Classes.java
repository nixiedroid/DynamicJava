package com.nixiedroid.modules.clazz;

public class Classes {
    public static <T> Class<T> retrieveFrom(Object object) {
        return object != null ? (Class<T>)object.getClass() : null;
    }

    public static Class<?>[] retrieveFrom(Object... objects) {
        Class<?>[] classes = null;
        if (objects != null) {
            classes = new Class[objects.length];
            for (int i = 0; i < objects.length; i++) {
                if (objects[i] != null) {
                    classes[i] = retrieveFrom(objects[i]);
                }
            }
        } else {
            classes = new Class[]{null};
        }
        return classes;
    }
}
