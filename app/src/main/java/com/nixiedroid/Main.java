package com.nixiedroid;


import com.nixiedroid.unsafe.Modules;

public class Main {

    public static void main(String[] args) throws Exception{
//                org.burningwave.core.assembler.StaticComponentContainer
//                       .Modules.exportPackage("java.base","nixiedroid.dynamicJava");
        //io.github.toolfactory.jvm.function.catalog.SetAccessibleFunction.class
        //Objects.getAddressNew();
    }



    private static void modulesTraversal() {
        //        org.burningwave.core.assembler.StaticComponentContainer
        //               .Modules.exportPackage("java.lang.invoke","nixiedroid.dynamicJava");
        //  Object plugin = PluginWrapper.loadPluginFromCP("com.nixiedroid.plugins.Plugin");
        //https://stackoverflow.com/questions/68867895/in-java-17-how-do-i-avoid-resorting-to-add-opens
        //https://github.com/Moderocky/Overlord/tree/master
        //java.lang.ModuleLayer
        //jdk.internal.module.Modules
        //for (String moduleAndPackage : exports.split(" ")) {
        //String[] s = moduleAndPackage.trim().split("/");
        //if (s.length != 2) continue;
        //jdk.internal.module.Modules.addExports(ModuleLayer.boot().findModule(s[0]).orElseThrow(), s[1]);
        //}
        Modules.ping();
    }


}