package com.nixiedroid.modules;

import com.nixiedroid.runtime.Info;
import com.nixiedroid.unsafe.UnsafeWrapper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ModuleManager2 {
    private final Object internalUnsafe;
    private final sun.misc.Unsafe unsafe;
    private final MethodHandle objectFieldOffset;
    private final MethodHandle staticFieldOffset;
    private final MethodHandles.Lookup brokenLookup;
    private final MethodHandle forName;
    private final MethodHandle getDeclaredFields0;
    @SuppressWarnings("FieldCanBeLocal")
    private final Class<?> moduleClass;
    private final Map<String, Module> nameToModule;
    private final Set<Object> allSet = new HashSet<>();

    public ModuleManager2() throws Throwable {
        brokenLookup = thisWillBreakSoon();
        forName = getForname0();
        unsafe = UnsafeWrapper.getUnsafe();
        internalUnsafe = getInternalUnsafe();
        objectFieldOffset = getDynamicFieldOffset();
        staticFieldOffset = getStaticFieldOffset();
        moduleClass = forName0("java.lang.Module");
        getDeclaredFields0 = declaredFieldsUnsafe();
        nameToModule = getNameToModule();
        allSet.add(
                getFieldData(moduleClass,getField(moduleClass,"ALL_UNNAMED_MODULE"))
        );
        allSet.add(
                getFieldData(moduleClass,getField(moduleClass,"EVERYONE_MODULE"))
        );
        exportAllToAll();
    }

    public Class<?> forName0(String name) throws Throwable {
        return (Class<?>) forName.invoke(name, false, this.getClass().getClassLoader(), this.getClass());
    }
    private MethodHandles.Lookup thisWillBreakSoon() {
        int TRUSTED = -1;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        final long allowedModesFieldMemoryOffset = Info.isIs64Bit() ? 12L : 8L;
        UnsafeWrapper.getUnsafe().putInt(lookup, allowedModesFieldMemoryOffset, TRUSTED);
        return lookup;
    }
    private Object getInternalUnsafe() throws Throwable {
        Class<?> intUnsafeClass = forName0("jdk.internal.misc.Unsafe");
        return brokenLookup.findStaticVarHandle(sun.misc.Unsafe.class, "theInternalUnsafe", intUnsafeClass).get();
    }
    private MethodHandle getDynamicFieldOffset() throws Throwable {
        MethodType mt = MethodType.methodType(long.class, Field.class);
        return brokenLookup.findVirtual(internalUnsafe.getClass(), "objectFieldOffset", mt);
    }
    private MethodHandle getStaticFieldOffset() throws Throwable {
        MethodType mt = MethodType.methodType(long.class, Field.class);
        return brokenLookup.findVirtual(internalUnsafe.getClass(), "staticFieldOffset", mt);
    }
    private MethodHandle getForname0() throws NoSuchMethodException, IllegalAccessException {
        MethodType forName0mt = MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class);
        return brokenLookup.findStatic(Class.class, "forName0", forName0mt);
    }
    private MethodHandle declaredFieldsUnsafe() throws NoSuchMethodException, IllegalAccessException {
        return brokenLookup.findSpecial(Class.class, "getDeclaredFields0", MethodType.methodType(Field[].class, boolean.class), Class.class);
    }
    @SuppressWarnings("unchecked")
    private Map<String, Module> getNameToModule() throws Throwable {
       // Class<?> moduleLayerClass = forName0("java.lang.ModuleLayer");
        ModuleLayer boot = ModuleLayer.boot();
        return (Map<String, Module>) getFieldData(boot,getField(boot.getClass(),"nameToModule"));
    }
    private Field getField(Class<?> clazz, String fieldName) throws Throwable {
        Field[] fields = (Field[]) getDeclaredFields0.invokeWithArguments(clazz, false);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        throw new NoSuchFieldException();
    }
    private Object getFieldData(Object obj, Field f) throws Throwable {
        Long offset;
        if (Modifier.isStatic(f.getModifiers())){
             offset = (Long) staticFieldOffset.invoke(internalUnsafe, f);
        } else {
             offset = (Long) objectFieldOffset.invoke(internalUnsafe, f);
        }
        return unsafe.getObject(obj, offset);
    }
    private void setFieldData(Object obj, Field f, Object data) throws Throwable {
        Long offset;
        if (Modifier.isStatic(f.getModifiers())){
            offset = (Long) staticFieldOffset.invoke(internalUnsafe, f);
        } else {
            offset = (Long) objectFieldOffset.invoke(internalUnsafe, f);
        }
        unsafe.putObject(obj,offset,data);
    }
    public void exportAllToAll() throws Throwable {
        for (Map.Entry<String, Module> entry : nameToModule.entrySet()) {
            Module module = entry.getValue();
            for (String pkgName :  module.getPackages()) {
                exportToAll("exportedPackages", module, pkgName);
                exportToAll("openPackages", module, pkgName);
            }
        }
    }
    @SuppressWarnings("unchecked")
    void exportToAll(String fieldName, Module module, String pkgName) throws Throwable {
        Map<String, Set<?>> pckgForModule = (Map<String, Set<?>>) getFieldData(
                module,getField(module.getClass(),fieldName));
        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            setFieldData(module, getField(module.getClass(),fieldName), pckgForModule);
        }
        pckgForModule.put(pkgName, allSet);
        if (fieldName.startsWith("exported")) {
            MethodType mt = MethodType.methodType(void.class,Module.class, String.class);
            MethodHandle mh = brokenLookup.findStatic(Module.class, "addExportsToAll0", mt);
            mh.invoke(module, pkgName);
        }
    }
    public void exportPackage(String moduleFromName, String moduleToName, String... packageNames) throws Throwable {
        Module moduleFrom = checkAndGetModule(moduleFromName);
        Module moduleTo = checkAndGetModule(moduleToName);
        exportPackage(moduleFrom, moduleTo, packageNames);
    }
    private Module checkAndGetModule(String name) {
        Module module = nameToModule.get(name);
        if (module == null) {
            throw new RuntimeException(name);
        }
        return module;
    }
    private void exportPackage(Module moduleFrom, Module moduleTo, String... packageNames) throws Throwable {
        Set<String> modulePackages = moduleFrom.getPackages();
        for (String pkgName : packageNames) {
            if (!modulePackages.contains(pkgName)) {
                throw new RuntimeException(pkgName);
            }
            export("exportedPackages", moduleFrom, pkgName, moduleTo);
            export("openPackages", moduleFrom, pkgName, moduleTo);
        }
    }
    @SuppressWarnings("unchecked")
    void export(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) throws Throwable {
        Map<String, Set<?>> pckgForModule = (Map<String, Set<?>>) getFieldData(
                moduleFrom,getField(moduleFrom.getClass(),fieldName));

        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            setFieldData(moduleFrom, getField(moduleFrom.getClass(),fieldName), pckgForModule);
        }
        Set<Object> moduleSet = (Set<Object>) pckgForModule.get(pkgName);
        if (!(moduleSet instanceof HashSet)) {
            if (moduleSet != null) {
                moduleSet = new HashSet<>(moduleSet);
            } else {
                moduleSet = new HashSet<>();
            }
            pckgForModule.put(pkgName, moduleSet);
        }
        moduleSet.add(moduleTo);
        if (fieldName.startsWith("exported")) {
            MethodType mt = MethodType.methodType(void.class,Module.class, String.class,Module.class);
            MethodHandle mh = brokenLookup.findStatic(Module.class, "addExports0", mt);
            mh.invoke(moduleFrom, pkgName, moduleTo);
        }
    }
}
