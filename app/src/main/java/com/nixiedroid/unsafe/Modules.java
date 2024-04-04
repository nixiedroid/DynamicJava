package com.nixiedroid.unsafe;

import sun.reflect.ReflectionFactory;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.*;
import java.security.ProtectionDomain;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Modules {
    static final Method[] METHODS = new Method[16];
    private static Class<?> reflectAccessClass;
    private static Class<?> methodAccessorClass;
    private static Object reflectAccess;

    static {
        try {
            //moveMeToJavaBase();
            final Method method = AccessibleObject.class.getDeclaredMethod("setAccessible0", boolean.class);
            method.setAccessible(true);
            METHODS[5] = Module.class
                    .getDeclaredMethod("implAddExportsOrOpens", String.class, Module.class, boolean.class, boolean.class);
            method.invoke(METHODS[5], true);
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("Could not expose implAddExportsOrOpens.");
        }
    }

    public static void ping() {
    }
    public static void peekInaccessibleObject(){
        try {
            Class<?> cl = Class.forName( "java.util.prefs.Base64",
                    false,ClassLoader.getSystemClassLoader());
            Field f = cl.getDeclaredField("intToBase64");
            f.setAccessible(true);
            System.out.println("Access Permitted!");
        } catch (ClassNotFoundException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (InaccessibleObjectException e){
            System.out.println("Access Denied!");
        }
    }

    private static void moveMeToJavaBase(){
        try {
            System.out.println("Class module name: " + Class.class.getModule().getName());
            System.out.println("My module name: " + Modules.class.getModule().getName());
            peekInaccessibleObject();
            final Field field = Class.class.getDeclaredField("module");
            @SuppressWarnings("deprecation")
            final long offset = Unsafe.getUnsafe().objectFieldOffset(field);
            Unsafe.getUnsafe().putObject(Modules.class, offset, Object.class.getModule());
            System.out.println("My module name: " + Modules.class.getModule().getName());
            peekInaccessibleObject();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    static void doMagic() {
        try {
            breakEncapsulation(Class.class, true);
            allowAccess(Class.class, true);
            METHODS[0] = Class.class.getDeclaredMethod("getDeclaredConstructors0", boolean.class);
            METHODS[0].setAccessible(true);
        } catch (NoSuchMethodException e) {
            System.out.println("Could not expose getDeclaredConstructors0.");
        }
        try {
            allowAccess(Unsafe.class);
            breakEncapsulation(Unsafe.getUnsafe().getClass(), true);
            allowAccess(Unsafe.getUnsafe().getClass(), true);
            MethodHandles.class.getModule().addOpens(MethodHandles.class.getPackageName(), Modules.class.getModule());
            METHODS[1] = Unsafe.getUnsafe().getClass()
                    .getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class, ClassLoader.class, ProtectionDomain.class);
            METHODS[1].setAccessible(true);
        } catch (NoSuchMethodException e) {
            System.out.println("Could not expose defineClass.");
        }
        try {
            METHODS[2] = Class.class.getDeclaredMethod("protectionDomain");
            METHODS[2].setAccessible(true);
        } catch (NoSuchMethodException e) {
            System.out.println("Could not expose protectionDomain.");
        }
        try {
            allowAccess(MethodHandles.class);
            allowAccess(MethodHandles.Lookup.class);
            METHODS[3] = MethodHandles.Lookup.class.getDeclaredMethod("makeClassDefiner", byte[].class);
            METHODS[3].setAccessible(true);
        } catch (NoSuchMethodException e) {
            System.out.println("Could not expose makeClassDefiner.");
        }
        try {
            Class<?> cls = Class.forName("java.lang.reflect.ReflectAccess");
            allowAccess(cls);
            reflectAccessClass = cls;
        } catch (ClassNotFoundException e) {
            reflectAccessClass = null;
            System.out.println("Could not expose ReflectAccess class.");
        }
        try {
            Class<?> cls = Class.forName("jdk.internal.reflect.MethodAccessor");
            breakEncapsulation(cls, true);
            allowAccess(cls, true);
            methodAccessorClass = cls;
        } catch (ClassNotFoundException e) {
            methodAccessorClass = null;
            System.out.println("Could not expose MethodAccessor class.");
        }
        try {
            Field delegate = ReflectionFactory.class.getDeclaredField("delegate");
            delegate.setAccessible(true);
            Object internal = delegate.get(null);
            breakEncapsulation(internal.getClass(), true);
            allowAccess(internal.getClass(), true);
            Field langReflectAccess = internal.getClass().getDeclaredField("langReflectAccess");
            langReflectAccess.setAccessible(true);
            reflectAccess = langReflectAccess.get(internal);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            reflectAccess = null;
            System.out.println("Could not expose JavaLangReflectAccess instance.");
        }
        try {
            METHODS[4] = reflectAccessClass.getDeclaredMethod("newConstructor",
                    Class.class,
                    Class[].class,
                    Class[].class,
                    int.class,
                    int.class,
                    String.class,
                    byte[].class,
                    byte[].class);
            METHODS[4].setAccessible(true);
        } catch (NoSuchMethodException | IllegalStateException e) {
            System.out.println("Could not expose newConstructor.");
        }
        try {
            METHODS[6] = reflectAccessClass.getDeclaredMethod("getMethodAccessor", Method.class);
            METHODS[6].setAccessible(true);
        } catch (NoSuchMethodException | IllegalStateException e) {
            System.out.println("Could not expose getMethodAccessor.");
        }
        try {
            METHODS[7] = reflectAccessClass.getDeclaredMethod("setMethodAccessor", Method.class, methodAccessorClass);
            METHODS[7].setAccessible(true);
        } catch (NoSuchMethodException | IllegalStateException e) {
            System.out.println("Could not expose setMethodAccessor.");
        }
        try {
            breakEncapsulation(methodAccessorClass, true);
            allowAccess(methodAccessorClass, true);
            METHODS[8] = methodAccessorClass.getDeclaredMethod("invoke", Object.class, Object[].class);
            METHODS[8].setAccessible(true);
        } catch (NoSuchMethodException | IllegalStateException e) {
            System.out.println("Could not expose invoke (MethodAccessor).");
        }
        try {
            allowAccess(Method.class);
            METHODS[9] = Method.class.getDeclaredMethod("getRoot");
            METHODS[9].setAccessible(true);
        } catch (NoSuchMethodException | IllegalStateException e) {
            System.out.println("Could not expose getRoot.");
        }
        try {
            allowAccess(Class.class);
            METHODS[10] = Class.class.getDeclaredMethod("getMethod0", String.class, Class[].class);
            METHODS[10].setAccessible(true);
        } catch (NoSuchMethodException | IllegalStateException e) {
            System.out.println("Could not expose getMethod0.");
        }
    }

    static void allowAccess(Class<?> target, boolean accessNamedModules) {
        if (!accessNamedModules) allowAccess(target);
        else {
            try {
                METHODS[5].invoke(target.getModule(),
                        target.getPackageName(),
                        Modules.class.getModule(),
                        true,
                        true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }


    static void breakEncapsulation(Class<?> target, boolean accessNamedModules) {
        if (!accessNamedModules) {
            target.getModule().addExports(target.getPackageName(), Modules.class.getModule());
        } else {
            try {
                METHODS[5].invoke(target.getModule(),
                        target.getPackageName(),
                        Modules.class.getModule(),
                        false,
                        true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    static void allowAccess(Class<?> target) {
        target.getModule()
                .addOpens(target.getPackageName(), Modules.class.getModule());
    }
}
