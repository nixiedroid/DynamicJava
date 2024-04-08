package com.nixiedroid.modules.util;

import org.apache.bcel.classfile.ClassParser;
import org.apache.bcel.classfile.JavaClass;

import java.io.ByteArrayInputStream;
import java.io.IOException;

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
    public static String getRealClassName(byte[] classBytes) throws ClassNotFoundException {
        try {
            ClassParser classParser = new ClassParser(new ByteArrayInputStream(classBytes), "");
            JavaClass jc = classParser.parse();
            return jc.getClassName();
        } catch (IOException e) {
            throw new ClassNotFoundException(e.getMessage());
        }
    }
}
