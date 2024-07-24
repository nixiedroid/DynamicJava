package com.nixiedroid.reflection;

import com.nixiedroid.reflection.toolchain.SharedSecrets;

import java.lang.reflect.Field;

/**
 * Utility class providing methods for accessing and manipulating fields via reflection.
 *
 * <p>This class serves as a higher-level abstraction for working with fields in Java classes.
 * It provides methods to retrieve field objects and get or set their values. The actual
 * implementation details are delegated to the {@link SharedSecrets} class.</p>
 *
 * <p>The constructor of this class is private to prevent instantiation, as it only provides
 * static utility methods.</p>
 */
public final class Fields {

    // Private constructor to prevent instantiation
    private Fields() {
        // No-op
    }

    /**
     * Retrieves a {@link Field} object for the specified class and field name.
     *
     * @param clazz The class containing the field.
     * @param fieldName The name of the field to retrieve.
     * @return The {@link Field} object representing the specified field.
     * @throws Throwable If there is an error retrieving the field.
     */
    public static Field getField(Class<?> clazz, String fieldName) throws Throwable {
        return SharedSecrets.getField(clazz, fieldName);
    }

    /**
     * Retrieves the value of a specified field from an object.
     *
     * @param obj The object from which to retrieve the field value.
     * @param f The {@link Field} object representing the field.
     * @return The value of the field.
     */
    public static Object getFieldData(Object obj, Field f) {
        return SharedSecrets.getFieldData(obj, f);
    }

    /**
     * Sets the value of a specified field in an object.
     *
     * @param obj The object in which to set the field value.
     * @param f The {@link Field} object representing the field.
     * @param data The value to set in the field.
     */
    public static void setFieldData(Object obj, Field f, Object data) {
        SharedSecrets.setFieldData(obj, f, data);
    }
}
