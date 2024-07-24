package com.nixiedroid.modules.toolchain;

import com.nixiedroid.exceptions.Thrower;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Context {

    private static final Map<Class<?>,Object> classMap = new HashMap<>();

    private Context(){

    }

    public static <T> T get(Class<T> forClass){
        T obj = forClass.cast(classMap.get(forClass));
        if(obj == null){
            try {
                obj = forClass.getConstructor().newInstance();
                classMap.put(forClass,obj);
                return obj;
            } catch (InvocationTargetException e) {
                System.out.println("Error in constructor of class: " + forClass.getName());
                Thrower.throwException(e.getCause());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
        return obj;
    }
}
