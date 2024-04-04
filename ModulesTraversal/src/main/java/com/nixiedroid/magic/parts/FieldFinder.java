package com.nixiedroid.magic.parts;

import com.nixiedroid.magic.Context;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

public abstract class FieldFinder {
    protected MethodHandle getField;
    public MethodHandle get(){
        return getField;
    };
    public static class ForJava7 extends FieldFinder {
        public ForJava7() throws NoSuchMethodException, IllegalAccessException {
            MethodType type = MethodType.methodType(Field.class, Class.class, String.class);
            getField = Context.i().lookup().findStatic(ForJava7.class, "getField",type);
        }
        public static Field getField(Class<?> cls, String name){
            try {
                Field[] fields = (Field[]) Context.i().getFields().invokeWithArguments(cls, false);
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
