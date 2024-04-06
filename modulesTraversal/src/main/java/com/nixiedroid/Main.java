package com.nixiedroid;


import com.nixiedroid.wayone.ModuleManager;

import java.lang.reflect.Field;


public class Main {
    static final String FLOAT_CONSTANTS_CLASS_NAME="jdk.internal.math.FloatConsts";
    static final String THREAD_SLEEPING_EVENT_CLASS_NAME = "jdk.internal.event.ThreadSleepEvent";
    public static void main(String[] args) throws ClassNotFoundException {
        System.out.println(Info.getVersion());
        ModuleManager.poke();
        System.out.println(Main.class.getModule());
        System.out.println(ModuleManager.class.getModule());
        Class<?> cl = Class.forName(FLOAT_CONSTANTS_CLASS_NAME);
        System.out.println(cl.getModule());
        ModuleManager.allowAccess(Main.class,cl,false);
        try {
            Field field = cl.getDeclaredField("SIGNIFICAND_WIDTH");
            System.out.println(field.get(null));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }


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
    }
}