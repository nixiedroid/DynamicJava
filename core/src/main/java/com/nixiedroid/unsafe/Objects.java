package com.nixiedroid.unsafe;

import com.nixiedroid.unsafe.type.Pointer;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;

@SuppressWarnings("unused")
public class Objects {
    private static final Objects instance = new Objects();
    private static long dummyObjectOffset;
    static {
        getAddress();
        getAddressNew();
    }

    private Object dummyObject;

    private Objects() {
    }

    public static void getAddressNew() {
        try {
            MethodHandles.Lookup l = MethodHandles.lookup();
            VarHandle handler = l.findVarHandle(Objects.class, "dummyObject", Object.class);
            System.out.println(handler.get(instance));
            Field f = handler.getClass().getSuperclass().getDeclaredField("fieldOffset");
            f.setAccessible(true); //Not exported module exception
            System.out.println(f.get(handler));
            f.setAccessible(false);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private static void getAddress() {
        try {
            dummyObjectOffset = UnsafeWrapper.getUnsafe()
                    .objectFieldOffset(Objects.class.getDeclaredField("dummyObject"));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    //BOOLEAN
    public static boolean getBoolean(Object o, long offset) {
        return UnsafeWrapper.getUnsafe().getBoolean(o, offset);
    }

    public static void putBoolean(Object o, long offset, boolean x) {
        UnsafeWrapper.getUnsafe().putBoolean(o, offset, x);
    }

    //BYTE
    public static byte getByte(Object o, long offset) {
        return UnsafeWrapper.getUnsafe().getByte(o, offset);
    }

    public static void putByte(Object o, long offset, byte x) {
        UnsafeWrapper.getUnsafe().putByte(o, offset, x);
    }

    //INT
    public static int getInt(Object o, long offset) {
        return UnsafeWrapper.getUnsafe().getInt(o, offset);
    }

    public static void putInt(Object o, long offset, int x) {
        UnsafeWrapper.getUnsafe().putInt(o, offset, x);
    }


    public static Object getObject(Object o, long offset) {
        return UnsafeWrapper.getUnsafe().getObject(o, offset);
    }

    public static void putObject(Object o, long offset, Object x) {
        UnsafeWrapper.getUnsafe().putObject(o, offset, x);
    }

    public static Pointer ObjectToAddress(Object obj) {
        instance.dummyObject = obj; //store address of obj to instance.obj
        long pointer = UnsafeWrapper.getUnsafe().getLong(instance, dummyObjectOffset);
        return new Pointer(pointer);
    }

    public static Object AddressToObject(Pointer p) {
        UnsafeWrapper.getUnsafe().putLong(instance, dummyObjectOffset, p.address());
        return instance.dummyObject;
    }
}
