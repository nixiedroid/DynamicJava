package com.nixiedroid.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;


@SuppressWarnings("unused")
public class Proxy {
    private final Map<MethodDesc, MethodHandle> methodsCache = new HashMap<>();
    private final Class<?> proxyClass;
    private final MethodHandles.Lookup proxyLookup;
    private final Object proxyInstance;

    public Proxy(String className, boolean noInstance)throws ClassNotFoundException {
        this.proxyClass = loadClass(className);
        this.proxyLookup = getLookup(this.proxyClass);
        this.proxyInstance = null;
    }

    public Proxy(String className) throws ClassNotFoundException {
        this.proxyClass = loadClass(className);
        this.proxyLookup = getLookup(this.proxyClass);
        this.proxyInstance = invoke("<init>", void.class);
    }

    public Proxy(String className, Object... ctorArgs) throws ClassNotFoundException {
        this.proxyClass = loadClass(className);
        this.proxyLookup = getLookup(this.proxyClass);
        this.proxyInstance = invoke("<init>", void.class, ctorArgs);
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void throwException(Throwable exc) throws E {
        throw (E) exc; //Throws actual exception
    }

    private Class<?>[] getParamTypes(Object... args) {
        Class<?>[] paramTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i].getClass().isPrimitive());
            paramTypes[i] = args[i].getClass();
        }
        return paramTypes;
    }

    private Class<?> loadClass(String className) throws ClassNotFoundException {
        ClassLoader cl = this.getClass().getClassLoader();
        return cl.loadClass(className);
    }

    private MethodHandles.Lookup getLookup(Class<?> clazz) throws ClassNotFoundException {
        try {
            return MethodHandles.privateLookupIn(clazz, MethodHandles.lookup());
        } catch (IllegalAccessException e) {
            throw new ClassNotFoundException("Failed to create MethodHandles.Lookup for class: " + clazz.getName(), e);
        }
    }

    public void invoke(String methodName, MethodType mt, Object... args) {
        invoke(methodName, void.class, mt, args);
    }

    public void invoke(String methodName, Object... args) {
        invoke(methodName, void.class, args);
    }

    @SuppressWarnings("unchecked")
    public <R> R invoke(String methodName, Class<R> retClass, MethodType mt, Object... args) {
        try {
            MethodHandle mh = getMethodHandle(methodName,mt);
            if (args.length == 0) {
                if (methodName.equals("<init>")) {
                    return (R) mh.invoke();
                } else if (Modifier.isStatic(mh.type().parameterType(0).getModifiers())) {
                    return (R) mh.invoke();
                } else if (this.proxyInstance != null) {
                    return (R) mh.invoke(this.proxyInstance);
                }
            }
            if (methodName.equals("<init>")) {
                return (R) mh.invoke(args);
            } else if (Modifier.isStatic(mh.type().parameterType(0).getModifiers())) {
                return (R) mh.invoke(args);
            } else if (this.proxyInstance != null) {
                return (R) mh.invoke(this.proxyInstance, args);
            }
            throw new IllegalStateException("Instance method called on a null instance.");
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Return type mismatch for method: " + methodName, e);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("No such method found: " + methodName, e);
        } catch (Throwable t) {
            throwException(t);
            return null;
        }
    }

    public <R> R invoke(String methodName, Class<R> retClass, Object... args) {
        MethodType mt = MethodType.methodType(retClass,getParamTypes(args));
        return invoke(methodName,retClass,mt,args);
    }

    private MethodHandle getMethodHandle(String methodName, MethodType mt) throws NoSuchMethodException, IllegalAccessException {
        final MethodDesc desc = new MethodDesc(methodName, mt);
        return this.methodsCache.computeIfAbsent(desc, methodDesc -> {
            MethodHandle mh;
            try {
                if (methodName.equals("<init>")) {
                    mh = this.proxyLookup.findConstructor(this.proxyClass, mt);
                } else {
                    try {
                        mh = this.proxyLookup.findVirtual(this.proxyClass, methodName, mt);
                    } catch (NoSuchMethodException ignored) {
                        mh = this.proxyLookup.findStatic(this.proxyClass, methodName, mt);
                    }
                }
            } catch (NoSuchMethodException | IllegalAccessException e) {
                throw new RuntimeException("Failed to get method handle for method: " + methodName, e);
            }
            return mh;
        });
    }
}