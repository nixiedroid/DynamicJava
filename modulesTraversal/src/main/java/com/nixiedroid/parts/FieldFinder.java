package com.nixiedroid.parts;

import com.nixiedroid.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public abstract class FieldFinder {
    public abstract Field getField(Class<?> cls, String name);

    public static class ForJava7 extends FieldFinder {
        public ForJava7() throws NoSuchMethodException, IllegalAccessException {

        }
        public Field getField(Class<?> cls, String name){
            try {
                Field[] fields = Context.i().getFields(cls,false);
                for (Field field : fields) {
                    if (field.getName().equals(name)) {
                        return field;
                    }
                }
                return null;
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
    }
}
