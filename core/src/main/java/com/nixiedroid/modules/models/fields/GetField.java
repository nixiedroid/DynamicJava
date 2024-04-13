package com.nixiedroid.modules.models.fields;

import com.nixiedroid.interfaces.ThrowableBiFunction;
import com.nixiedroid.modules.Context;

import java.lang.reflect.Field;

public abstract class GetField implements ThrowableBiFunction<Class<?>,String,Field> {

    public static class ForJava7 extends GetField {
        @Override
        public Field  apply(Class<?> cls, String name) throws Throwable {
                Field[] fields = Context.i().getFields(cls,false);
                for (Field field : fields) {
                    if (field.getName().equals(name)) {
                        return field;
                    }
                }
                return null;
        }
    }
}
