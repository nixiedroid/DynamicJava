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
public class Modules {

    private final Set<Object> allModules = new HashSet<>();
    private final Map<String, Module> nameToModuleMap;
    private final Class<?> moduleClass;

    /**
     * Constructs a {@code Modules} instance and initializes module-related fields.
     *
     * <p>
     * Retrieves the {@code Module} class and initializes the mappings and sets for module
     * exports and visibility.
     * </p>
     *
     * @throws Throwable If there is an error accessing or initializing module information.
     */
    public Modules() throws Throwable {
        this.moduleClass = SharedSecrets.getClassByName(
                "java.lang.Module",
                false, Modules.class.getClassLoader(),
                Class.class
        );
        this.nameToModuleMap = retrieveNameToModuleMap();
        initializeAllModulesSet();
    }

    /**
     * Retrieves the mapping from module names to module instances.
     *
     * @return A map where the keys are module names and the values are module instances.
     * @throws Throwable If there is an error retrieving the module information.
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Module> retrieveNameToModuleMap() throws Throwable {
        ModuleLayer boot = ModuleLayer.boot();
        return (Map<String, Module>) getFieldData(boot, getField(boot.getClass(), "nameToModule"));
    }

    /**
     * Initializes the set of all modules, including the unnamed and everyone modules.
     *
     * @throws Throwable If there is an error accessing module fields.
     */
    private void initializeAllModulesSet() throws Throwable {
        this.allModules.add(getFieldData(this.moduleClass, getField(this.moduleClass, "ALL_UNNAMED_MODULE")));
        this.allModules.add(getFieldData(this.moduleClass, getField(this.moduleClass, "EVERYONE_MODULE")));
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
    public void exportAllToAll() throws Throwable {
        for (Module module : this.nameToModuleMap.values()) {
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
    private void exportPackageToAll(String fieldName, Module module, String pkgName) throws Throwable {
        Map<String, Set<?>> packageMap = getOrCreateFieldMap(module, fieldName);
        packageMap.put(pkgName, this.allModules);

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
    public void exportPackage(String moduleFromName, String moduleToName, String... packageNames) throws Throwable {
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
    private Module getModuleByName(String name) {
        Module module = this.nameToModuleMap.get(name);
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
    private void exportPackages(Module moduleFrom, Module moduleTo, String... packageNames) throws Throwable {
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
    private void exportPackage(String fieldName, Object moduleFrom, String pkgName, Object moduleTo) throws Throwable {
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
    private Map<String, Set<?>> getOrCreateFieldMap(Object module, String fieldName) throws Throwable {
        Map<String, Set<?>> packageMap = (Map<String, Set<?>>) getFieldData(module, getField(module.getClass(), fieldName));
        if (packageMap == null) {
            packageMap = new HashMap<>();
            setFieldData(module, getField(module.getClass(), fieldName), packageMap);
        }
        return packageMap;
    }
}
