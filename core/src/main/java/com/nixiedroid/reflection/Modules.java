package com.nixiedroid.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import com.nixiedroid.reflection.toolchain.SharedSecrets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.nixiedroid.reflection.Fields.*;

/**
 * Utility class for manipulating Java modules and their exports.
 * <p>
 * This class provides methods to query and modify module visibility and exports using reflection
 * and method handles. It accesses internal Java APIs and should be used with caution.
 * </p>
 */
@SuppressWarnings("unused")
public final class Modules {

    private static final Set<Object> allModules = new HashSet<>();
    private static final Map<String, Module> nameToModuleMap;
    private static final Class<?> moduleClass;


    /**
     * Private constructor to prevent instantiation of this utility class.
     *
     * <p>Throws an {@link Error} to indicate that instantiation is not allowed.
     */
    private Modules() {
        throw new Error("Cannot instantiate utility class");
    }

    static {
        try {
            moduleClass = SharedSecrets.getClassByName(
                    "java.lang.Module",
                    false, Modules.class.getClassLoader(),
                    Class.class
            );
            nameToModuleMap = retrieveNameToModuleMap();
            initializeAllModulesSet();
        } catch (ReflectiveOperationException e){
            throw new Error(e);
        }
    }


    /**
     * Retrieves the mapping from module names to module instances.
     *
     * @return A map where the keys are module names and the values are module instances.
     * @throws NoSuchFieldException If there is an error retrieving the module information.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Module> retrieveNameToModuleMap() throws NoSuchFieldException {
        ModuleLayer boot = ModuleLayer.boot();
        return (Map<String, Module>) getFieldData(boot, getField(boot.getClass(), "nameToModule"));
    }

    /**
     * Initializes the set of all modules, including the unnamed and everyone modules.
     *
     * @throws NoSuchFieldException If there is an error accessing module fields.
     */
    private static void initializeAllModulesSet() throws NoSuchFieldException {
        allModules.add(getFieldData(moduleClass, getField(moduleClass, "ALL_UNNAMED_MODULE")));
        allModules.add(getFieldData(moduleClass, getField(moduleClass, "EVERYONE_MODULE")));
    }

    /**
     * Exports all packages from all modules to all other modules.
     *
     * <p>
     * Iterates over all modules and exports each of their packages to all other modules,
     * modifying the module visibility and export settings accordingly.
     * </p>
     *
     * @throws Throwable If there is an error performing the export operations.
     */
    public static void exportAllToAll() throws Throwable {
        for (Module module : nameToModuleMap.values()) {
            for (String pkgName : module.getPackages()) {
                exportPackageToAll("exportedPackages", module, pkgName);
                exportPackageToAll("openPackages", module, pkgName);
            }
        }
    }

    /**
     * Exports a package from a module to all other modules.
     *
     * @param fieldName The name of the field to update ("exportedPackages" or "openPackages").
     * @param module The module whose packages are being exported.
     * @param pkgName The name of the package to export.
     * @throws Throwable If there is an error performing the export operation.
     */
    private static void exportPackageToAll(String fieldName, Module module, String pkgName) throws Throwable {
        Map<String, Set<?>> packageMap = getOrCreateFieldMap(module, fieldName);
        packageMap.put(pkgName, allModules);

        if (fieldName.startsWith("exported")) {
            MethodType methodType = MethodType.methodType(void.class, Module.class, String.class);
            MethodHandle methodHandle = SharedSecrets.findStatic(Module.class, "addExportsToAll0", methodType);
            methodHandle.invoke(module, pkgName);
        }
    }

    /**
     * Exports specified packages from one module to another.
     *
     * @param moduleFromName The name of the module from which to export packages.
     * @param moduleToName The name of the module to which to export packages.
     * @param packageNames The names of the packages to export.
     * @throws Throwable If there is an error performing the export operations.
     */
    public static void exportPackage(String moduleFromName, String moduleToName, String... packageNames) throws Throwable {
        Module moduleFrom = getModuleByName(moduleFromName);
        Module moduleTo = getModuleByName(moduleToName);
        exportPackages(moduleFrom, moduleTo, packageNames);
    }

    /**
     * Retrieves a module by name and verifies its existence.
     *
     * @param name The name of the module.
     * @return The {@code Module} instance corresponding to the given name.
     * @throws RuntimeException If the module does not exist.
     */
    private static Module getModuleByName(String name) {
        Module module = nameToModuleMap.get(name);
        if (module == null) {
            throw new RuntimeException("Module not found: " + name);
        }
        return module;
    }

    /**
     * Exports specified packages from one module to another.
     *
     * @param moduleFrom The module from which to export packages.
     * @param moduleTo The module to which to export packages.
     * @param packageNames The names of the packages to export.
     * @throws Throwable If there is an error performing the export operations.
     */
    private static void exportPackages(Module moduleFrom, Module moduleTo, String... packageNames) throws Throwable {
        Set<String> modulePackages = moduleFrom.getPackages();
        for (String pkgName : packageNames) {
            if (!modulePackages.contains(pkgName)) {
                throw new IllegalArgumentException(moduleFrom + " does not contain package " + pkgName);
            }
            exportPackage("exportedPackages", moduleFrom, pkgName, moduleTo);
            exportPackage("openPackages", moduleFrom, pkgName, moduleTo);
        }
    }

    /**
     * Exports a package from one module to another.
     *
     * @param fieldName The name of the field to update ("exportedPackages" or "openPackages").
     * @param moduleFrom The module from which to export the package.
     * @param pkgName The name of the package to export.
     * @param moduleTo The module to which to export the package.
     * @throws Throwable If there is an error performing the export operation.
     */
    @SuppressWarnings("unchecked")
    private static void exportPackage(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) throws Throwable {
        Map<String, Set<?>> packageMap = getOrCreateFieldMap(moduleFrom, fieldName);
        Set<Object> moduleSet = (Set<Object>)
                packageMap.computeIfAbsent(pkgName, k -> new HashSet<>());
        moduleSet.add(moduleTo);

        if (fieldName.startsWith("exported")) {
            MethodType methodType = MethodType.methodType(void.class, Module.class, String.class, Module.class);
            MethodHandle methodHandle = SharedSecrets.findStatic(Module.class, "addExports0", methodType);
            methodHandle.invoke(moduleFrom, pkgName, moduleTo);
        }
    }

    /**
     * Retrieves or creates the map of packages for a module.
     *
     * @param module The module whose package map is to be retrieved or created.
     * @param fieldName The name of the field holding the package map.
     * @return The map of packages for the module.
     * @throws Throwable If there is an error accessing or modifying the field.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Set<?>> getOrCreateFieldMap(Object module, String fieldName) throws Throwable {
        Map<String, Set<?>> packageMap = (Map<String, Set<?>>) getFieldData(module, getField(module.getClass(), fieldName));
        if (packageMap == null) {
            packageMap = new HashMap<>();
            setFieldData(module, getField(module.getClass(), fieldName), packageMap);
        }
        return packageMap;
    }
}
