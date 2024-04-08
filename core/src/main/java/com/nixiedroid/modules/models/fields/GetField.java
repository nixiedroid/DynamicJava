package com.nixiedroid.modules.models.fields;

import com.nixiedroid.modules.Context;

import java.lang.reflect.Field;

public abstract class GetField {
    public abstract Field getField(Class<?> cls, String name);

    public static class ForJava7 extends GetField {
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
