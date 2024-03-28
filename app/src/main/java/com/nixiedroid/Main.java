package com.nixiedroid;

import com.nixiedroid.unsafe.Modules;

public class Main {
    public static void main(String[] args) throws Exception{

    }



    private static void modulesTraversal() {
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