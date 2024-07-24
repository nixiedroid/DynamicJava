package com.nixiedroid.modules.toolchain.fields;

import com.nixiedroid.function.ThrowableBiFunction;
import com.nixiedroid.modules.toolchain.Context;
import com.nixiedroid.modules.toolchain.GetDeclaredFieldsFunction;

import java.lang.reflect.Field;

public class GetFieldFunction implements ThrowableBiFunction<Class<?>,String , Field> {

   private final GetDeclaredFieldsFunction fun;

    public GetFieldFunction() {
        this.fun = Context.get(GetDeclaredFieldsFunction.class);
    }

    @Override
    public Field apply(Class<?> clazz, String fieldName) throws Throwable {
        Field[] fields = this.fun.apply(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new NoSuchFieldException();
    }
}
