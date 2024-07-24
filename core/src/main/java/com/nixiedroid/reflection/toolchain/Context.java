package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.runtime.Properties;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class Context {

    private static final String SJAVA = "$Java";
    private static final List<Integer> importantJavaVersions = List.of(7,9,14,17,21,22);
    private static final int JavaVersion = Properties.getVersion();
    private static final List<String> classNames = importantJavaVersions
            .stream()
            .filter(ver -> JavaVersion >= ver)
            .sorted((a, b) -> b-a)
            .map(integer -> SJAVA+integer)
            .collect(Collectors.toList());
    private static final Map<Class<?>,Object> classMap = new HashMap<>();

    private Context(){
        throw new Error();
    }

    static <T> T get(Class<T> forClass){
        T obj = forClass.cast(classMap.get(forClass));
        if(obj == null){
            Class<T> target = findClass(forClass);
            try {
                obj = target.getDeclaredConstructor().newInstance();
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

    @SuppressWarnings("unchecked")
    private static <T> Class<T> findClass(Class<T> forClass){
        String parentClassName = forClass.getName();
        for(String className: classNames){
            try {
                return (Class<T>) Class.forName(parentClassName+className);
            } catch (ClassNotFoundException ignored) {}
        }
        return Thrower.throwExceptionWithReturn(new Error("Unable to find suitable class for" + parentClassName));
    }
}
