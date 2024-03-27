package com.nixiedroid.unsafe;
@SuppressWarnings("Unused")
public class Objects {
    //BOOLEAN
    public static boolean getBoolean(Object o, long offset) {
        return Unsafe.getUnsafe().getBoolean(o, offset);
    }
    public static void putBoolean(Object o, long offset, boolean x) {
        Unsafe.getUnsafe().putBoolean(o, offset, x);
    }
    //BYTE
    public static byte getByte(Object o, long offset) {
        return Unsafe.getUnsafe().getByte(o, offset);
    }
    public static void putByte(Object o, long offset, byte x) {
        Unsafe.getUnsafe().putByte(o, offset, x);
    }
    //INT
    public static int getInt(Object o, long offset) {
        return Unsafe.getUnsafe().getInt(o, offset);
    }
    public static void putInt(Object o, long offset, int x) {
        Unsafe.getUnsafe().putInt(o, offset, x);
    }


    public static Object getObject(Object o, long offset) {
        return Unsafe.getUnsafe().getObject(o, offset);
    }

    public static void putObject(Object o, long offset, Object x) {
        Unsafe.getUnsafe().putObject(o, offset, x);
    }
}
