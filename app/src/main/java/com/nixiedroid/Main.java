package com.nixiedroid;

import com.nixiedroid.modules.Context;
import com.nixiedroid.modules.models.lookups.Lookup;
import com.nixiedroid.runtime.Info;
import com.nixiedroid.unsafe.UnsafeWrapper;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Main {

    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        accessHandle();
//        unsafeCode();
        thisWillBreakSoon();
    }
    private MethodHandles.Lookup thisWillBreakSoon(){
        int TRUSTED = -1;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            Field modes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
        } catch (NoSuchFieldException e) {
            System.out.println("No such Field for java 8");
        }

        final long allowedModesFieldMemoryOffset = Info.isIs64Bit() ? 12L : 8L;
        UnsafeWrapper.getUnsafe().putInt(lookup, allowedModesFieldMemoryOffset, -1);
        return lookup;
    }


    private void unsafeCode() {
        int[] arr = new int[]{1};
        sun.misc.Unsafe unsafe = UnsafeWrapper.getUnsafe();
        // unsafe.arrayBaseOffset(arr.getClass());
        System.out.println(arr.getClass().getName());
        int base = unsafe.arrayBaseOffset(arr.getClass());
        System.out.println(base);



    }
    private void accessHandle() {
        var mt = MethodType.methodType(String.class);
        var lookup = MethodHandles.lookup();

        System.out.println(getModifiers(lookup.lookupModes()));
    }
    private String getModifiers(int modifiers){
        StringBuilder sb = new StringBuilder("Modifiers:");
        if ((modifiers & Modifier.PUBLIC) != 0) sb.append(" Public");
        if ((modifiers & Modifier.PRIVATE) != 0) sb.append(" Private");
        if ((modifiers & Modifier.PROTECTED) != 0) sb.append(" Protected");
        if ((modifiers & Modifier.STATIC) != 0) sb.append(" Package");
        if ((modifiers & Modifier.STATIC<<1) != 0) sb.append(" Module");
        if ((modifiers & Modifier.STATIC<<2) != 0) sb.append(" Unconditional");
        if ((modifiers & Modifier.STATIC<<3) != 0) sb.append(" Original");
        if (modifiers == -1) sb.append(" Trusted");
        return sb.toString();
    }


}
