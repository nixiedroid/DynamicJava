package com.nixiedroid.modules;

import java.lang.reflect.*;
@SuppressWarnings("unused")
public class ModuleManager {
    private static sun.misc.Unsafe unsafe;
    private static Method setAccessible;
    private static Method setAccesibleModule;
    static {
        getUnsafe();
        moveMeToJavaBase();
        getAccessible();
        getAccessibleModule();
    }

    private static void getUnsafe(){
        try {
            Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (sun.misc.Unsafe) field.get(null);
            field.setAccessible(false);
        } catch (NoSuchFieldException e) {
            try {
                Constructor<sun.misc.Unsafe> unsafeConstructor = sun.misc.Unsafe.class.getDeclaredConstructor();
                unsafeConstructor.setAccessible(true);
                unsafe = unsafeConstructor.newInstance();
                unsafeConstructor.setAccessible(false);
            } catch (Exception ex) {
                throw new RuntimeException(e + " : " + ex);
            }
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static void moveMeToJavaBase() {
        final Field field;
        try {
            field = Class.class.getDeclaredField("module");
            final long offset = unsafe.objectFieldOffset(field);
            unsafe.putObject(ModuleManager.class, offset, Object.class.getModule());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }
    private static void getAccessible(){
        try {
            setAccessible = AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);
            setAccessible.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getAccessibleModule() {
        try {
            setAccesibleModule = Module.class.getDeclaredMethod("implAddExportsOrOpens", String.class, Module.class, boolean.class, boolean.class);
            setAccessible.invoke(setAccesibleModule, true);
        }
        catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            System.out.println("Could not expose implAddExportsOrOpens. Invocation Exception");
        } catch (IllegalAccessException e) {
            System.out.println("Could not expose implAddExportsOrOpens. Access denied");
        }
    }

    public static void allowAccess(final Class<?> accessor, final Class<?> target, final boolean accessNamedModules) {
        if (!accessNamedModules) target.getModule().addOpens(target.getPackageName(), accessor.getModule());
        else {
            try {
                setAccessible.invoke(target.getModule(), target.getPackageName(), accessor.getModule(), true, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
    public static void poke(){}

    public static void breakEncapsulation(final Class<?> accessor, final Class<?> target, final boolean accessNamedModules) {
        if (!accessNamedModules) target.getModule().addExports(target.getPackageName(), accessor.getModule());
        else {
            try {
                setAccessible.invoke(target.getModule(), target.getPackageName(), accessor.getModule(), false, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e.getMessage());
            }
        }
    }
}
