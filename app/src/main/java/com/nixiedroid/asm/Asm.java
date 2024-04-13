package com.nixiedroid.asm;

import com.nixiedroid.exceptions.Thrower;
import com.nixiedroid.modules.util.Modules;

public class Asm {
    public Asm(){
        try {
            Modules.exportAllToAll();
        } catch (Throwable e) {
            Thrower.throwExceptionAndDie(e);
        }


        Class<?> cl = null;
        try {
            cl = Class.forName("jdk.internal.org.objectweb.asm.Type");
            cl = Class.forName("jdk.internal.misc.Unsafe");
            cl.getName();
            // ModuleManager.allowAccess(Main.class, "jdk.internal.org.objectweb.asm.Type", false);
          //  ModuleManager.allowAccess(Main.class, "jdk.internal.misc.UnsafeConstants", false);
        } catch (Throwable e) {
            Thrower.throwException(e);
        }

        //System.out.println( jdk.internal.org.objectweb.asm.Type.SHORT);
    }
}

