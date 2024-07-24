package com.nixiedroid.modules;

import com.nixiedroid.modules.toolchain.Context;
import com.nixiedroid.modules.toolchain.GetClassByNameFunction;
import com.nixiedroid.modules.toolchain.TrustedLookupSupplier;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.nixiedroid.modules.Fields.*;

public class Modules {

    public static void main(String[] args) throws Throwable {
        new Modules().exportAllToAll();
    }

    private final Set<Object> allSet = new HashSet<>();
    private final Map<String, Module> nameToModule;
    @SuppressWarnings("FieldCanBeLocal")
    private final Class<?> moduleClass;

    public Modules() throws Throwable {
        this.moduleClass = Context.get(GetClassByNameFunction.class).apply(
                "java.lang.Module",
                false, Modules.class.getClassLoader(),
                Class.class
        );
        this.nameToModule = getNameToModule();
        this.allSet.add(
                getFieldData(this.moduleClass, getField(this.moduleClass, "ALL_UNNAMED_MODULE"))
        );
        this.allSet.add(
                getFieldData(this.moduleClass, getField(this.moduleClass, "EVERYONE_MODULE"))
        );
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Module> getNameToModule() throws Throwable {
        // Class<?> moduleLayerClass = forName0("java.lang.ModuleLayer");
        ModuleLayer boot = ModuleLayer.boot();
        return (Map<String, Module>) getFieldData(boot, getField(boot.getClass(), "nameToModule"));
    }


    public  void exportAllToAll() throws Throwable {
        for (Map.Entry<String, Module> entry : this.nameToModule.entrySet()) {
            Module module = entry.getValue();
            for (String pkgName : module.getPackages()) {
                exportToAll("exportedPackages", module, pkgName);
                exportToAll("openPackages", module, pkgName);
            }
        }
    }


    @SuppressWarnings("unchecked")
    void exportToAll(String fieldName, Module module, String pkgName) throws Throwable {
        Map<String, Set<?>> pckgForModule = (Map<String, Set<?>>) getFieldData(
                module, getField(module.getClass(), fieldName));
        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            setFieldData(module, getField(module.getClass(), fieldName), pckgForModule);
        }
        pckgForModule.put(pkgName, this.allSet);
        if (fieldName.startsWith("exported")) {
            MethodType mt = MethodType.methodType(void.class, Module.class, String.class);
            MethodHandle mh = Context.get(TrustedLookupSupplier.class).get()
                    .findStatic(Module.class, "addExportsToAll0", mt);
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
                throw new IllegalArgumentException(moduleFrom + " does not contains " + pkgName);
            }
            export("exportedPackages", moduleFrom, pkgName, moduleTo);
            export("openPackages", moduleFrom, pkgName, moduleTo);
        }
    }


    @SuppressWarnings("unchecked")
    void export(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) throws Throwable {
        Map<String, Set<?>> pckgForModule = (Map<String, Set<?>>) getFieldData(
                moduleFrom, getField(moduleFrom.getClass(), fieldName));

        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            setFieldData(moduleFrom, getField(moduleFrom.getClass(), fieldName), pckgForModule);
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
            MethodType mt = MethodType.methodType(void.class, Module.class, String.class, Module.class);
            MethodHandle mh = Context.get(TrustedLookupSupplier.class)
                    .get().findStatic(Module.class, "addExports0", mt);
            mh.invoke(moduleFrom, pkgName, moduleTo);
        }
    }
}
