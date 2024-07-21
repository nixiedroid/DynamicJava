package com.nixiedroid.bytes;

import java.lang.reflect.Field;

public class ByteArrayConverterUnsafe implements ByteArrayConverter {

    private static final sun.misc.Unsafe U;
    private static final long OFFSET;

    static {
        try {
            Field theUnsafe = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            U = (sun.misc.Unsafe) theUnsafe.get(null);
            OFFSET = U.arrayBaseOffset(byte[].class);
        } catch (ReflectiveOperationException e) {
            throw new Error("Unable to populate static fields");
        }
    }


    @Override
    public byte toInt8(byte[] b, int start) {
        return U.getByte(b, OFFSET + start);
    }

    @Override
    public short toInt16B(byte[] b, int start) {
        return Short.reverseBytes(toInt16L(b, start));
    }

    @Override
    public short toInt16L(byte[] b, int start) {
        return U.getShort(b, OFFSET + start);
    }

    @Override
    public int toInt32B(byte[] b, int start) {
        return Integer.reverseBytes(toInt32L(b, start));
    }

    @Override
    public int toInt32L(byte[] b, int start) {
        return U.getInt(b, OFFSET + start);
    }

    @Override
    public long toInt64B(byte[] b, int start) {
        return Long.reverseBytes(toInt64L(b, start));
    }

    @Override
    public long toInt64L(byte[] b, int start) {
        return U.getLong(b, OFFSET + start);
    }

    @Override
    public void int8ToBytes(byte[] b, int start, byte by) {
        U.putByte(b, OFFSET + start, by);
    }

    @Override
    public void int16ToBytesB(byte[] b, int start, short s) {
        int16ToBytesL(b, start, Short.reverseBytes(s));
    }

    @Override
    public void int16ToBytesL(byte[] b, int start, short s) {
        U.putShort(b, OFFSET + start, s);
    }

    @Override
    public void int32ToBytesB(byte[] b, int start, int i) {
        int32ToBytesL(b, start, Integer.reverseBytes(i));
    }

    @Override
    public void int32ToBytesL(byte[] b, int start, int i) {
        U.putInt(b, OFFSET + start, i);
    }

    @Override
    public void int64ToBytesB(byte[] b, int start, long l) {
        int64ToBytesL(b, start, Long.reverseBytes(l));
    }

    @Override
    public void int64ToBytesL(byte[] b, int start, long l) {
        U.putLong(b, OFFSET + start, l);
    }
}
