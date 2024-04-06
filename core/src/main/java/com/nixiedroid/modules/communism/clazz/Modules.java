package com.nixiedroid.modules.communism.clazz;

import com.nixiedroid.modules.communism.Context;
import com.nixiedroid.modules.communism.clazz.Methods;
import com.nixiedroid.modules.communism.clazz.Fields;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class Modules {
    private final Class<?> moduleClass;
    private final Set<?> allSet = new HashSet<>();
    private final Set<?> everyOneSet = new HashSet<>();
    private final Set<?> allUnnamedSet = new HashSet<>();
    private final Map<String, ?> nameToModule;

    public Modules() {
        try {
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
        } catch (Throwable exc) {
            throw new RuntimeException(exc);
        }
    }

    public void exportAllToAll() {
        try {
            nameToModule.forEach((name, module)
                    -> ((Set<String>)Methods.invokeDirect(module, "getPackages"))
                    .forEach(pkgName -> {
                exportToAll("exportedPackages", module, pkgName);
                exportToAll("openPackages", module, pkgName);
                            }
            ));
        } catch (Throwable exc) {
           throw new RuntimeException(exc);
        }
    }


    void exportToAll(String fieldName, Object module, String pkgName) {
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
    void export(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) {
        Map<String, Set<?>> pckgForModule = Fields.getDirect(moduleFrom, fieldName);
        if (pckgForModule == null) {
            pckgForModule = new HashMap<>();
            Fields.setDirect(moduleFrom, fieldName, pckgForModule);
        }
        Set<Object> moduleSet = (Set<Object>)pckgForModule.get(pkgName);
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
    public void exportPackage(String moduleFromName, String moduleToName, String... packageNames) {
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

    void exportPackage(Object moduleFrom, Object moduleTo, String... packageNames) {
        Set<String> modulePackages = Methods.invokeDirect(moduleFrom, "getPackages");
        Stream.of(packageNames).forEach(pkgName -> {
            if (!modulePackages.contains(pkgName)) {
               throw new RuntimeException(pkgName);
            }
            export("exportedPackages", moduleFrom, pkgName, moduleTo);
            export("openPackages", moduleFrom, pkgName, moduleTo);
        });
    }

    void exportToAllUnnamed(String fieldName, Object module, String pkgName) {
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
