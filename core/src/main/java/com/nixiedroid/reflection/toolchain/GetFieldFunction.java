package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableBiFunction;

import java.lang.reflect.Field;

interface GetFieldFunction extends ThrowableBiFunction<Class<?>, String, Field> {

    class Java7 implements GetFieldFunction {
        private final GetDeclaredFieldsFunction fun;

        Java7() {
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
}
