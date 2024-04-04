package com.nixiedroid;

import com.nixiedroid.magic.Context;
import io.github.toolfactory.jvm.function.template.TriConsumer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Test {
    private static Class<?> moduleClass;
    static io.github.toolfactory.jvm.Driver driver;
    private static Set<?> allSet = new HashSet<>();
    private static Set<?> everyOneSet = new HashSet<>();
    private static Set<?> allUnnamedSet = new HashSet<>();
    private static Map<String, ?> nameToModule;
    public static void main(String[] args) {

        driver = io.github.toolfactory.jvm.Driver.Factory.getNew();
        try {
            moduleClass = driver.getClassByName(
                    "java.lang.Module", false,
                    Main.class.getClassLoader(),
                    Main.class
            );
            Class<?> moduleLayerClass = driver.getClassByName(
                    "java.lang.ModuleLayer", false,
                    Main.class.getClassLoader(),
                    Main.class
            );
            MethodHandle boot = Context.i().lookup().findStatic(
                    moduleLayerClass,"boot", MethodType.methodType(ModuleLayer.class)
            );

            ModuleLayer moduleLayer = (ModuleLayer) boot.invoke();
            Field modules = (Field) Context.i().getField().invoke(moduleLayerClass, "nameToModule");
            driver.setAccessible(modules,true);
            System.out.println(modules.get(moduleLayer));
        } catch (Throwable t){
            t.printStackTrace();
        }
    }

    public void exportAllToAll() {
//        try {
//            nameToModule.forEach((name, module) -> {
//                MethodHandle boot = null;
//                try {
//                    boot = Context.i().lookup().findStatic(
//                            module.getClass(),"getPackages", MethodType.methodType(Module.class)
//                    );
//                    Set<String> moduleLayer = (ModuleLayer) boot.invoke();
//
//
//                } catch (Throwable e) {
//                    throw new RuntimeException(e);
//                }
//
//
//                ((Set<String>)Methods.invokeDirect(module, "getPackages")).forEach(pkgName -> {
//                    exportToAll("exportedPackages", module, pkgName);
//                    exportToAll("openPackages", module, pkgName);
//                });
//            });
//        } catch (Throwable exc) {
//            org.burningwave.core.assembler.StaticComponentContainer.Driver.throwException(exc);
//        }
    }


//    public void exportToAll(String name) {
//        exportTo(name, this::exportToAll);
//    }
//    Object checkAndGetModule(String name) {
//        Object module = nameToModule.get(name);
//        if (module == null) {
//            throw new RuntimeException();
//        }
//        return module;
//    }
    void exportTo(String name, TriConsumer<String, Object, String> exporter) {
//        try {
//            Object module = checkAndGetModule(name);
//            ((Set<String>)Methods.invokeDirect(module, "getPackages")).forEach(pkgName -> {
//                exporter.accept("exportedPackages", module, pkgName);
//                exporter.accept("openPackages", module, pkgName);
//            });
//        } catch (Throwable exc) {
//            driver.throwException(exc);
//        }
    }
//    void export(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) {
//        Map<String, Set<?>> pckgForModule = Fields.getDirect(moduleFrom, fieldName);
//        if (pckgForModule == null) {
//            pckgForModule = new HashMap<>();
//            Fields.setDirect(moduleFrom, fieldName, pckgForModule);
//        }
//        Set<Object> moduleSet = (Set<Object>)pckgForModule.get(pkgName);
//        if (!(moduleSet instanceof HashSet)) {
//            if (moduleSet != null) {
//                moduleSet = new HashSet<>(moduleSet);
//            } else {
//                moduleSet = new HashSet<>();
//            }
//            pckgForModule.put(pkgName, moduleSet);
//        }
//        moduleSet.add(moduleTo);
//        if (fieldName.startsWith("exported")) {
//            Methods.invokeStaticDirect(moduleClass, "addExports0", moduleFrom, pkgName, moduleTo);
//        }
//    }
}
