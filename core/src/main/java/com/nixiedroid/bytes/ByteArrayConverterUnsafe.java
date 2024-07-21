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
    public byte toByte(byte[] b, int start) {
        return U.getByte(b, OFFSET + start);
    }

    @Override
    public short toShortB(byte[] b, int start) {
        return Short.reverseBytes(toShortL(b, start));
    }

    @Override
    public short toShortL(byte[] b, int start) {
        return U.getShort(b, OFFSET + start);
    }

    @Override
    public int toIntegerB(byte[] b, int start) {
        return Integer.reverseBytes(toIntegerL(b, start));
    }

    @Override
    public int toIntegerL(byte[] b, int start) {
        return U.getInt(b, OFFSET + start);
    }

    @Override
    public long toLongB(byte[] b, int start) {
        return Long.reverseBytes(toLongL(b, start));
    }

    @Override
    public long toLongL(byte[] b, int start) {
        return U.getLong(b, OFFSET + start);
    }

    @Override
    public float toFloatB(byte[] b, int start) {
        return Float.intBitsToFloat(toIntegerB(b,start));
    }

    @Override
    public float toFloatL(byte[] b, int start) {
        return U.getFloat(b,OFFSET+start);
    }

    @Override
    public double toDoubleB(byte[] b, int start) {
        return Double.longBitsToDouble(toLongB(b,start));
    }

    @Override
    public double toDoubleL(byte[] b, int start) {
        return U.getDouble(b,OFFSET+start);
    }

    @Override
    public void fromByte(byte[] b, int start, byte by) {
        U.putByte(b, OFFSET + start, by);
    }

    @Override
    public void fromShortB(byte[] b, int start, short s) {
        fromShortL(b, start, Short.reverseBytes(s));
    }

    @Override
    public void fromShortL(byte[] b, int start, short s) {
        U.putShort(b, OFFSET + start, s);
    }

    @Override
    public void fromIntegerB(byte[] b, int start, int i) {
        fromIntegerL(b, start, Integer.reverseBytes(i));
    }

    @Override
    public void fromIntegerL(byte[] b, int start, int i) {
        U.putInt(b, OFFSET + start, i);
    }

    @Override
    public void fromLongB(byte[] b, int start, long l) {
        fromLongL(b, start, Long.reverseBytes(l));
    }

    @Override
    public void fromLongL(byte[] b, int start, long l) {
        U.putLong(b, OFFSET + start, l);
    }
}
