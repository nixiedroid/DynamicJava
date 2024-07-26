package com.nixiedroid.reflection;

import com.nixiedroid.reflection.toolchain.SharedSecrets;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

@SuppressWarnings("unused")
public final class Methods {
    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * <p>Throws an {@link Error} to indicate that instantiation is not allowed.
     */
    private Methods() {
        throw new Error("Cannot instantiate utility class");
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
        return SharedSecrets.findStatic(refc,name,type);
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
        return SharedSecrets.findVirtual(refc,name,type);
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
        return SharedSecrets.findSpecial(refc,name,type,specialCaller);
    }

}
