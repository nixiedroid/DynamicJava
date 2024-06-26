package com.nixiedroid.modules.util;

import com.nixiedroid.classloaders.parser.JavaClassParser;

public class Classes {
    public static Class<?> retrieveFrom(Object object) {
        return object != null ? object.getClass() : null;
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
    public static String getRealClassName(byte[] classBytes) {
        return JavaClassParser.create(classBytes).getSimpleName();
    }
}
