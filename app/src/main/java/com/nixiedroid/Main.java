package com.nixiedroid;

import com.nixiedroid.premain.Premain;
import com.nixiedroid.samples.Cats;
import com.nixiedroid.samples.Cats.DoubleCat;
import com.nixiedroid.unsafe.Memory;
import com.nixiedroid.unsafe.UnsafeWrapper;
import com.nixiedroid.unsafe.type.Pointer;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;

public class Main {
    sun.misc.Unsafe unsafe = UnsafeWrapper.getUnsafe();

    Main() {
        //new ModuleManager2();
        //Stuff.accessInaccessible();
        unsafeCode();

    }

    public static void main(String[] args) throws Throwable {
        new Main();

    }

    private static long normalize(int value) {
        if (value >= 0) return value;
        return (~0L >>> 32) & value;
    }

    private void unsafeCode() {


        Cats.TripleCat cat = new Cats.TripleCat();
        System.out.println(Premain.sizeOf(cat));
        Pointer p = Memory.malloc((int) Premain.sizeOf(cat));
        for (int i = 0; i <= sizeOf(cat); i++) {
            byte b = unsafe.getByte(cat,i);
            unsafe.putByte(p.address()+i,b);
        }
        Cats.TripleCat[] array = new Cats.TripleCat[1];
        long baseOffset = unsafe.arrayBaseOffset(array.getClass());
        unsafe.putAddress(normalize(unsafe.getInt(array,baseOffset)),p.address());
        cat = array[0];
        System.out.println(cat.a);

    }
    public long sizeOf(Object o) {
        HashSet<Field> fields = new HashSet<Field>();
        Class c = o.getClass();
        while (c != Object.class) {
            for (Field f : c.getDeclaredFields()) {
                if ((f.getModifiers() & Modifier.STATIC) == 0) {
                    fields.add(f);
                }
            }
            c = c.getSuperclass();
        }

        // get offset
        long maxSize = 0;
        for (Field f : fields) {
            long offset = unsafe.objectFieldOffset(f);
            if (offset > maxSize) {
                maxSize = offset;
            }
        }

        return ((maxSize/8) + 1) * 8;   // padding
    }

    long toAddress(Object obj) {
        Object[] array = new Object[]{obj};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        return normalize(unsafe.getInt(array, baseOffset));
    }

    Object fromAddress(long address) {
        Object[] array = new Object[]{null};
        long baseOffset = unsafe.arrayBaseOffset(Object[].class);
        unsafe.putLong(array, baseOffset, address);
        return array[0];
    }


}
