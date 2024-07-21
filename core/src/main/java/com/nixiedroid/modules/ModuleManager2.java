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
        this.brokenLookup = thisWillBreakSoon();
        this.forName = getForName0();
        this.unsafe = UnsafeWrapper.getUnsafe();
        this.internalUnsafe = getInternalUnsafe();
        this.objectFieldOffset = getDynamicFieldOffset();
        this.staticFieldOffset = getStaticFieldOffset();
        this.moduleClass = forName0("java.lang.Module");
        this.getDeclaredFields0 = declaredFieldsUnsafe();
        this.nameToModule = getNameToModule();
        this.allSet.add(
                getFieldData(this.moduleClass,getField(this.moduleClass,"ALL_UNNAMED_MODULE"))
        );
        this.allSet.add(
                getFieldData(this.moduleClass,getField(this.moduleClass,"EVERYONE_MODULE"))
        );
    }

    public Class<?> forName0(String name) throws Throwable {
        return (Class<?>) this.forName.invoke(name, false, this.getClass().getClassLoader(), this.getClass());
    }
    private MethodHandles.Lookup thisWillBreakSoon() {
        int TRUSTED = -1;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        final long allowedModesFieldMemoryOffset = Info.is64Bit() ? 12L : 8L;
        UnsafeWrapper.getUnsafe().putInt(lookup, allowedModesFieldMemoryOffset, TRUSTED);
        return lookup;
    }
    public MethodHandles.Lookup getBrokenLookup(){
        return thisWillBreakSoon();
    }
    private Object getInternalUnsafe() throws Throwable {
        Class<?> intUnsafeClass = forName0("jdk.internal.misc.Unsafe");
        return this.brokenLookup.findStaticVarHandle(sun.misc.Unsafe.class, "theInternalUnsafe", intUnsafeClass).get();
    }
    private MethodHandle getDynamicFieldOffset() throws Throwable {
        MethodType mt = MethodType.methodType(long.class, Field.class);
        return this.brokenLookup.findVirtual(this.internalUnsafe.getClass(), "objectFieldOffset", mt);
    }
    private MethodHandle getStaticFieldOffset() throws Throwable {
        MethodType mt = MethodType.methodType(long.class, Field.class);
        return this.brokenLookup.findVirtual(this.internalUnsafe.getClass(), "staticFieldOffset", mt);
    }
    private MethodHandle getForName0() throws NoSuchMethodException, IllegalAccessException {
        MethodType forName0mt = MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class);
        return this.brokenLookup.findStatic(Class.class, "forName0", forName0mt);
    }
    private MethodHandle declaredFieldsUnsafe() throws NoSuchMethodException, IllegalAccessException {
        return this.brokenLookup.findSpecial(Class.class, "getDeclaredFields0", MethodType.methodType(Field[].class, boolean.class), Class.class);
    }
    @SuppressWarnings("unchecked")
    private Map<String, Module> getNameToModule() throws Throwable {
       // Class<?> moduleLayerClass = forName0("java.lang.ModuleLayer");
        ModuleLayer boot = ModuleLayer.boot();
        return (Map<String, Module>) getFieldData(boot,getField(boot.getClass(),"nameToModule"));
    }
    private Field getField(Class<?> clazz, String fieldName) throws Throwable {
        Field[] fields = (Field[]) this.getDeclaredFields0.invokeWithArguments(clazz, false);
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
             offset = (Long) this.staticFieldOffset.invoke(this.internalUnsafe, f);
        } else {
             offset = (Long) this.objectFieldOffset.invoke(this.internalUnsafe, f);
        }
        return this.unsafe.getObject(obj, offset);
    }
    private void setFieldData(Object obj, Field f, Object data) throws Throwable {
        Long offset;
        if (Modifier.isStatic(f.getModifiers())){
            offset = (Long) this.staticFieldOffset.invoke(this.internalUnsafe, f);
        } else {
            offset = (Long) this.objectFieldOffset.invoke(this.internalUnsafe, f);
        }
        this.unsafe.putObject(obj,offset,data);
    }
    public void exportAllToAll() throws Throwable {
        for (Map.Entry<String, Module> entry : this.nameToModule.entrySet()) {
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
        pckgForModule.put(pkgName, this.allSet);
        if (fieldName.startsWith("exported")) {
            MethodType mt = MethodType.methodType(void.class,Module.class, String.class);
            MethodHandle mh = this.brokenLookup.findStatic(Module.class, "addExportsToAll0", mt);
            mh.invoke(module, pkgName);
        }
    }
    public void exportPackage(String moduleFromName, String moduleToName, String... packageNames) throws Throwable {
        Module moduleFrom = checkAndGetModule(moduleFromName);
        Module moduleTo = checkAndGetModule(moduleToName);
        exportPackage(moduleFrom, moduleTo, packageNames);
    }
    private Module checkAndGetModule(String name) {
        Module module = this.nameToModule.get(name);
        if (module == null) {
            throw new RuntimeException(name);
        }
        return module;
    }
    private void exportPackage(Module moduleFrom, Module moduleTo, String... packageNames) throws Throwable {
        Set<String> modulePackages = moduleFrom.getPackages();
        for (String pkgName : packageNames) {
            if (!modulePackages.contains(pkgName)) {
                throw new IllegalArgumentException( moduleFrom + " does not contains " + pkgName);
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
            MethodHandle mh = this.brokenLookup.findStatic(Module.class, "addExports0", mt);
            mh.invoke(moduleFrom, pkgName, moduleTo);
        }
    }
}
