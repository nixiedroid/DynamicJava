package com.nixiedroid;

import com.nixiedroid.plugins.PluginWrapper;
import com.nixiedroid.reflection.Method;
import com.nixiedroid.unsafe.Modules;
import com.nixiedroid.unsafe.Objects;

public class Main {
    public static void main(String[] args) {
        loadPlugin();
    }
    private static void loadPlugin(){
        Object plugin = PluginWrapper.loadPluginFromCP("com.nixiedroid.plugins.Plugin");
        Method.execute(plugin,"method");
        Method.execute(plugin,"staticMethod");
        Method.executeStatic(plugin,"staticMethod");

    }
    private static void modulesTraversal(){
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
    private static void printClass(){
        byte[] objBytes;
        Cat cat = new Cat(12);
        int[] intArray = new int[]{55, 12};
        for (int i = 0; i < 20; i++) {
            System.out.printf("%04x ", Objects.getInt(cat, i - 2) & 0xFF);
        }
        System.out.println("");
        for (int i = 0; i < 20; i++) {
            System.out.printf("  %02x ", Objects.getByte(cat, i - 2) & 0xFF);
        }
    }

    static class Cat {
        int age;

        public Cat(int age) {
            this.age = age;
        }
    }
}