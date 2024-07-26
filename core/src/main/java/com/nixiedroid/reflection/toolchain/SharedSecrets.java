package com.nixiedroid.reflection.toolchain;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;

/**
 * Utility class that provides access to various reflection and method handle operations.
 *
 * <p>This class is designed to encapsulate operations that involve accessing and manipulating
 * internal Java features via reflection and method handles. It uses the {@link Context} to
 * retrieve necessary functions for these operations.</p>
 *
 * <p>Note: The constructor of this class is private and will throw an error if invoked,
 * indicating that instances of this class should not be created.</p>
 */
public final class SharedSecrets {

    /**
     * Private constructor to prevent instantiation
     */
    private SharedSecrets() {
        // This will crash VM if executed
        Context.get(UnsafeSupplier.class).get().getInt(0);
    }

    /**
     * Retrieves a {@link Class} object by its name, with options to initialize it and specify
     * the class loader and caller class.
     *
     * @param name       The name of the class to retrieve.
     * @param initialize If true, the class will be initialized.
     * @param loader     The class loader to use.
     * @param caller     The caller class.
     * @return The {@link Class} object corresponding to the specified name.
     * @throws ClassNotFoundException If there is an error retrieving the class.
     */
    @SuppressWarnings("BooleanParameter")
    public static Class<?> getClassByName(String name, boolean initialize, ClassLoader loader, Class<?> caller) throws ClassNotFoundException {
        return Context.get(GetClassByNameFunction.class).apply(name, initialize, loader, caller);
    }

    /**
     * Retrieves a caller {@link Class}.
     */
    public static Class<?> getCallerClass() {
        return Context.get(GetCallerClassSupplier.class).get();
    }

    /**
     * Defines a class to the same class loader and in the same runtime package and
     * {@linkplain java.security.ProtectionDomain protection domain} as this lookup's
     *
     * The {@code data} parameter is the class bytes of a valid class file (as defined
     * by the <em>The Java Virtual Machine Specification</em>) with a class name in the
     * same package as the lookup class. </p>
     */
    public static Class<?> defineClass(Class<?> hostClass, byte[] data) throws IllegalAccessException {
        return Context.get(DefineHookFunction.class).apply(hostClass,data);
    }

    /**
     * Allocates an instance of the specified class
     * *
     *
     * @param clazz the class of which to allocate an instance
     * @return a new instance of the specified class
     * @throws InstantiationException if the class cannot be instantiated
     */
    public static Object allocateInstance(Class<?> clazz) throws InstantiationException {
        return Context.get(AllocateInstanceFunction.class).apply(clazz);
    }

    /**
     * Finds a static method in the specified class.
     *
     * @param refc The class in which to find the method.
     * @param name The name of the method to find.
     * @param type The method type.
     * @return A {@link MethodHandle} for the static method.
     * @throws NoSuchMethodException  If the method cannot be found.
     * @throws IllegalAccessException If access to the method is denied.
     */
    public static MethodHandle findStatic(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return Context.get(TrustedLookupSupplier.class).get().findStatic(refc, name, type);
    }


    /**
     * Finds a virtual method in the specified class.
     *
     * @param refc The class in which to find the method.
     * @param name The name of the method to find.
     * @param type The method type.
     * @return A {@link MethodHandle} for the virtual method.
     * @throws NoSuchMethodException  If the method cannot be found.
     * @throws IllegalAccessException If access to the method is denied.
     */
    public static MethodHandle findVirtual(Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        return Context.get(TrustedLookupSupplier.class).get().findVirtual(refc, name, type);
    }

    /**
     * Finds a special method in the specified class.
     *
     * @param refc The class in which to find the method.
     * @param name The name of the method to find.
     * @param type The method type.
     * @return A {@link MethodHandle} for the special method.
     * @throws NoSuchMethodException  If the method cannot be found.
     * @throws IllegalAccessException If access to the method is denied.
     */
    public static MethodHandle findSpecial(Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        return Context.get(TrustedLookupSupplier.class).get().findSpecial(refc,name,type,specialCaller);
    }

    /**
     * Retrieves a {@link Field} object for the specified class and field name.
     *
     * @param clazz     The class containing the field.
     * @param fieldName The name of the field to retrieve.
     * @return The {@link Field} object representing the specified field.
     * @throws NoSuchFieldException If there is an error retrieving the field.
     */
    public static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
        return Context.get(GetFieldFunction.class).apply(clazz, fieldName);
    }

    /**
     * Retrieves the value of a specified field from an object.
     *
     * @param obj The object from which to retrieve the field value.
     * @param f   The {@link Field} object representing the field.
     * @return The value of the field.
     */
    public static Object getFieldData(Object obj, Field f) {
        return Context.get(GetFieldValueFunction.class).apply(obj, f);
    }

    /**
     * Sets the value of a specified field in an object.
     *
     * @param obj  The object in which to set the field value.
     * @param f    The {@link Field} object representing the field.
     * @param data The value to set in the field.
     */
    public static void setFieldData(Object obj, Field f, Object data) {
        Context.get(SetFieldValueFunction.class).accept(obj, f, data);
    }
}
