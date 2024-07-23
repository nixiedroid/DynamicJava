package com.nixiedroid.modules.toolchain;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class Context {

    private static final Map<Class<?>,Object> classMap = new HashMap<>();


    public static <T> T get(Class<T> forClass){
        T obj = forClass.cast(classMap.get(forClass));
        if(obj == null){
            try {
                obj = forClass.getConstructor().newInstance();
                classMap.put(forClass,obj);
                return obj;
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }
}
