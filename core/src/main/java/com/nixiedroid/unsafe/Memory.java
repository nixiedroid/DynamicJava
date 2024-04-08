package com.nixiedroid.unsafe;

import com.nixiedroid.unsafe.type.Pointer;
import com.nixiedroid.unsafe.type.Size;

import java.util.HashMap;
import java.util.Map;

public class Memory {
    private static final HashMap<Pointer, Size> allocated;

    static {
        allocated = new HashMap<>();
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                int amount = 0;
                int size = 0;
                for (Map.Entry<Pointer, Size> entry : allocated.entrySet()) {
                    size += entry.getValue().size();
                    amount++;
                    Memory.free(entry.getKey());
                }
                if (amount != 0) {
                    System.out.println("You are a terrible person");
                    System.out.println("Freed " + amount + " pointers");
                    System.out.println("Equally  " + size + " bytes");
                }
            }
        }));
    }

    /**
     * Reflection proof private constructor
     */
    private Memory() {
        throw new UnsupportedOperationException("Unable to create instance of utility class");
    }

    public static Pointer malloc(int bytes) {
        if (bytes <= 0) throw new IllegalArgumentException("Malloc size is wrong");
        Pointer p = new Pointer(UnsafeWrapper.getUnsafe().allocateMemory(bytes));
        allocated.put(p, new Size(bytes));
        return p;
    }

    public static void fill(Pointer p, int size, byte value) {
        UnsafeWrapper.getUnsafe().setMemory(p.address(), size, value);
    }

    public static void empty(Pointer p, int size) {
        fill(p, size, (byte) 0);
    }

    public static Pointer realloc(Pointer pointer, int bytes) {
        if (bytes <= 0) throw new IllegalArgumentException("Realloc size is wrong");
        Pointer.validate(pointer);
        if (allocated.containsKey(pointer)) {
            if (allocated.get(pointer).size() <= bytes)
                throw new IllegalArgumentException("Shrinking memory size is not possible");
            UnsafeWrapper.getUnsafe().freeMemory(pointer.address());
        } else {
            throw new IllegalArgumentException("Trying to reallocate dangling pointer");
        }
        Pointer p = new Pointer(UnsafeWrapper.getUnsafe().reallocateMemory(pointer.address(), bytes));
        allocated.put(p, new Size(bytes));
        return p;
    }

    public static synchronized void free(Pointer pointer) {
        Pointer.validate(pointer);
        if (allocated.remove(pointer) != null) {
            UnsafeWrapper.getUnsafe().freeMemory(pointer.address());
        } else {
            throw new IllegalArgumentException("Trying to free dangling pointer");
        }
    }

    /**
     * Perfomance equals to System.arraycopy
     *
     * @param scr  Pointer to beginning of source array
     * @param dst  Pointer to beginning of destination array
     * @param size size in bytes
     * @see System#arraycopy(Object, int, Object, int, int)
     */

    @SuppressWarnings("SpellCheckingInspection")
    public static void memcpy(Pointer scr, Pointer dst, long size) {
        Pointer.validate(scr);
        Pointer.validate(dst);
        UnsafeWrapper.getUnsafe().copyMemory(scr.address(), dst.address(), size);

    }

    @SuppressWarnings("SpellCheckingInspection")
    public static void memcpy(Pointer scr, int offsetSrc, Pointer dst, int offsetDst, long size) {
        Pointer.validate(scr);
        Pointer.validate(dst);
        UnsafeWrapper.getUnsafe().copyMemory(scr.address() + offsetSrc, dst.address() + offsetDst, size);
    }

    public static byte peek(Pointer pointer) {
        Pointer.validate(pointer);
        return UnsafeWrapper.getUnsafe().getByte(pointer.address());
    }

    public static byte peek(Pointer pointer, int offset) {
        Pointer.validate(pointer);
        return UnsafeWrapper.getUnsafe().getByte(pointer.address() + offset);
    }

    public static void poke(Pointer pointer, byte value) {
        Pointer.validate(pointer);
        UnsafeWrapper.getUnsafe().putByte(pointer.address(), value);
    }

    public static void poke(Pointer pointer, int offset, byte value) {
        Pointer.validate(pointer);
        UnsafeWrapper.getUnsafe().putByte(pointer.address() + offset, value);
    }
}

    