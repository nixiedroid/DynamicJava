package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.runtime.Properties;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class that provides a context for dynamically loading and managing instances
 * of classes based on Java version and class names with specific suffixes.
 *
 * <p>This class is designed to manage class instances that vary based on the Java version
 * and includes functionality to dynamically find and instantiate classes with names that
 * include version-specific suffixes.
 */
final class Context {

    private static final String SJAVA = "$Java";
    private static final List<Integer> importantJavaVersions = List.of(7, 9, 14, 17, 21, 22);
    private static final int JavaVersion = Properties.getVersion();
    private static final List<String> classNames = importantJavaVersions
            .stream()
            .filter(ver -> JavaVersion >= ver)
            .sorted((a, b) -> b - a)
            .map(integer -> SJAVA + integer)
            .collect(Collectors.toList());
    private static final Map<Class<?>, Object> classMap = new HashMap<>();

    /**
     * Private constructor to prevent instantiation.
     * This class is intended to be used as a utility class.
     */
    private Context() {
        throw new Error("Cannot instantiate Context");
    }

    /**
     * Retrieves an instance of the specified class, creating it if necessary.
     *
     * <p>If the instance of the specified class does not exist, this method attempts
     * to find a suitable class based on the current Java version and then creates
     * a new instance using the class's no-argument constructor.
     *
     * @param <T>      the type of the class to retrieve
     * @param forClass the class to retrieve or create
     * @return an instance of the specified class
     * @throws RuntimeException if an error occurs during instantiation
     */
    static <T> T get(Class<T> forClass) {
        T obj = forClass.cast(classMap.get(forClass));
        if (obj == null) {
            Class<T> target = findClass(forClass);
            try {
                obj = target.getDeclaredConstructor().newInstance();
                classMap.put(forClass, obj);
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

    /**
     * Finds a suitable class by appending version-specific suffixes to the base class name.
     *
     * <p>This method attempts to locate a class whose name is derived from the base class name
     * and the suffixes based on important Java versions. If a suitable class cannot be found,
     * an error is thrown.
     *
     * @param <T>      the type of the class to find
     * @param forClass the base class to which suffixes are appended
     * @return the found class
     * @throws Error if no suitable class can be found
     */
    @SuppressWarnings("unchecked")
    private static <T> Class<T> findClass(Class<T> forClass) {
        String parentClassName = forClass.getName();
        for (String className : classNames) {
            try {
                return (Class<T>) Class.forName(parentClassName + className);
            } catch (ClassNotFoundException ignored) {

                // Continue searching
            }
        }
        return Thrower.throwExceptionWithReturn(new Error("Unable to find suitable class for " + parentClassName));
    }
}