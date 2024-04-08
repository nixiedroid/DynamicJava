package com.nixiedroid.modules.util;

import com.nixiedroid.modules.Context;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Modules {
    private final Class<?> moduleClass;
    private final Set<?> allSet = new HashSet<>();
    private final Set<?> everyOneSet = new HashSet<>();
    private final Set<?> allUnnamedSet = new HashSet<>();
    private final Map<String, ?> nameToModule;

    public Modules() throws Throwable {
        moduleClass = Context.i().getClassByName(
                "java.lang.Module", false,
                this.getClass().getClassLoader(),
                this.getClass()
        );
        Class<?> moduleLayerClass = Context.i().getClassByName(
                "java.lang.ModuleLayer", false,
                this.getClass().getClassLoader(),
                this.getClass()
        );
        Object moduleLayer = Methods.invokeStaticDirect(moduleLayerClass, "boot");
        nameToModule = Fields.getDirect(moduleLayer, "nameToModule");
        allSet.add(Fields.getStaticDirect(moduleClass, "ALL_UNNAMED_MODULE"));
        allSet.add(Fields.getStaticDirect(moduleClass, "EVERYONE_MODULE"));
        everyOneSet.add(Fields.getStaticDirect(moduleClass, "EVERYONE_MODULE"));
        allUnnamedSet.add(Fields.getStaticDirect(moduleClass, "ALL_UNNAMED_MODULE"));

    }

    public void exportAllToAll() throws Throwable {
        for (Map.Entry<String, ?> entry : nameToModule.entrySet()) {
            Object module = entry.getValue();
            for (String pkgName : ((Set<String>) Methods.invokeDirect(module, "getPackages"))) {
                exportToAll("exportedPackages", module, pkgName);
                exportToAll("openPackages", module, pkgName);
            }
        }
    }


    void exportToAll(String fieldName, Object module, String pkgName) throws Throwable {
        Map<String, Set<?>> pckgForModule = Fields.getDirect(module, fieldName);
        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            Fields.setDirect(module, fieldName, pckgForModule);
        }
        pckgForModule.put(pkgName, allSet);
        if (fieldName.startsWith("exported")) {
            Methods.invokeStaticDirect(moduleClass, "addExportsToAll0", module, pkgName);
        }
    }

    void export(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) throws Throwable {
        Map<String, Set<?>> pckgForModule = Fields.getDirect(moduleFrom, fieldName);
        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            Fields.setDirect(moduleFrom, fieldName, pckgForModule);
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
            Methods.invokeStaticDirect(moduleClass, "addExports0", moduleFrom, pkgName, moduleTo);
        }
    }

    public void exportPackage(String moduleFromName, String moduleToName, String... packageNames) throws Throwable {
        Object moduleFrom = checkAndGetModule(moduleFromName);
        Object moduleTo = checkAndGetModule(moduleToName);
        exportPackage(moduleFrom, moduleTo, packageNames);
    }

    Object checkAndGetModule(String name) {
        Object module = nameToModule.get(name);
        if (module == null) {
            throw new RuntimeException(name);
        }
        return module;
    }

    void exportPackage(Object moduleFrom, Object moduleTo, String... packageNames) throws Throwable {
        Set<String> modulePackages = Methods.invokeDirect(moduleFrom, "getPackages");
        for (String pkgName : packageNames) {
            if (!modulePackages.contains(pkgName)) {
                throw new RuntimeException(pkgName);
            }
            Modules.this.export("exportedPackages", moduleFrom, pkgName, moduleTo);
            Modules.this.export("openPackages", moduleFrom, pkgName, moduleTo);
        }
    }

    void exportToAllUnnamed(String fieldName, Object module, String pkgName) throws Throwable {
        Map<String, Set<?>> pckgForModule = Fields.getDirect(module, fieldName);
        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            Fields.setDirect(module, fieldName, pckgForModule);
        }
        pckgForModule.put(pkgName, allUnnamedSet);
        if (fieldName.startsWith("exported")) {
            Methods.invokeStaticDirect(moduleClass, "addExportsToAllUnnamed0", module, pkgName);
        }
    }
}
