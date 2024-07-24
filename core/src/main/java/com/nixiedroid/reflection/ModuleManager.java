package com.nixiedroid.reflection;

import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.unsafe.UnsafeWrapper;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings("unused")
public final class ModuleManager {
    private static final sun.misc.Unsafe unsafe;
    private static final long moduleFieldOffset;
    private static Method setAccessible;
    private static Method setAccessibleModule;

    static {
        unsafe = UnsafeWrapper.getUnsafe();
        moduleFieldOffset = getModuleFieldOffset();
        getAccessible();
        getAccessibleModule();

    }


    private ModuleManager(){

    }

    private static long getModuleFieldOffset() {
        final Field field;
        try {
            field = Class.class.getDeclaredField("module");
            return unsafe.objectFieldOffset(field);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private static void moveToJavaBase(Class<?> cl) {
        unsafe.putObject(cl, moduleFieldOffset, Object.class.getModule());
    }

    private static void getAccessible() {
        try {
            setAccessible = AccessibleObject.class
                    .getDeclaredMethod("setAccessible0", boolean.class);
            setAccessible.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private static void getAccessibleModule() {
        try {
            setAccessibleModule = Module.class
                    .getDeclaredMethod("implAddExportsOrOpens", String.class, Module.class, boolean.class, boolean.class);
            setAccessible.invoke(setAccessibleModule, true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            System.out.println("Could not expose implAddExportsOrOpens. Invocation Exception");
        } catch (IllegalAccessException e) {
            System.out.println("Could not expose implAddExportsOrOpens. Access denied");
        }
    }

    public static void allowAccess(
            final Class<?> accessor,
            final String target,
            final boolean accessNamedModules) {
        try {
            Class<?> cl = Class.forName(target);
            if (!accessNamedModules) {
                cl.getModule().addOpens(cl.getPackageName(), accessor.getModule());
            } else {
                setAccessibleModule.invoke(
                        cl.getModule(),
                        cl.getPackageName(),
                        accessor.getModule(),
                        true,
                        true);
            }
        } catch (ReflectiveOperationException e) {
            Thrower.throwException(e);
        }
    }

    public static void breakEncapsulation(
            final Class<?> accessor,
            final Class<?> target,
            final boolean accessNamedModules) {
        if (!accessNamedModules) target.getModule()
                .addExports(target.getPackageName(), accessor.getModule());
        else {
            try {
                setAccessibleModule.invoke(
                        target.getModule(),
                        target.getPackageName(),
                        accessor.getModule(),
                        false,
                        true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                Thrower.throwException(e);
            }
        }
    }
}
