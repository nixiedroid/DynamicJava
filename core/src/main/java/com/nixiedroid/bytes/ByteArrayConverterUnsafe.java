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
    public byte toByte(byte[] b, int start, Endiannes e) {
        return U.getByte(b, OFFSET + start);
    }

    @Override
    public short toShort(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Short.reverseBytes(toShort(b, start, Endiannes.LITTLE));
        }
        return U.getShort(b, OFFSET + start);
    }

    @Override
    public int toInteger(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Integer.reverseBytes(toInteger(b, start, Endiannes.LITTLE));
        }
        return U.getInt(b, OFFSET + start);
    }

    @Override
    public long toLong(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Long.reverseBytes(toLong(b, start, Endiannes.LITTLE));
        }
        return U.getLong(b, OFFSET + start);
    }

    @Override
    public float toFloat(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Float.intBitsToFloat(toInteger(b, start, Endiannes.BIG));
        }
        return U.getFloat(b, OFFSET + start);
    }

    @Override
    public double toDouble(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Double.longBitsToDouble(toLong(b, start, Endiannes.BIG));
        }
        return U.getDouble(b, OFFSET + start);
    }

    @Override
    public void fromByte(byte[] b, int start, byte by, Endiannes e) {
        U.putByte(b, OFFSET + start, by);
    }

    @Override
    public void fromShort(byte[] b, int start, short s, Endiannes e) {
        if (e == Endiannes.BIG) {
            fromShort(b, start, Short.reverseBytes(s), Endiannes.LITTLE);
            return;
        }
        U.putShort(b, OFFSET + start, s);
    }

    @Override
    public void fromInteger(byte[] b, int start, int i, Endiannes e) {
        if (e == Endiannes.BIG) {
            fromInteger(b, start, Integer.reverseBytes(i), Endiannes.LITTLE);
            return;
        }
        U.putInt(b, OFFSET + start, i);
    }

    @Override
    public void fromLong(byte[] b, int start, long l, Endiannes e) {
        if (e == Endiannes.BIG) {
            fromLong(b, start, Long.reverseBytes(l), Endiannes.LITTLE);
            return;
        }
        U.putLong(b, OFFSET + start, l);
    }

    @Override
    public void fromFloat(byte[] b, int start, float f, Endiannes e) {
        if (e == Endiannes.BIG) {
            fromInteger(b, start, Integer.reverseBytes(Float.floatToIntBits(f)), Endiannes.LITTLE);
            return;
        }
        U.putFloat(b, OFFSET + start, f);
    }

    @Override
    public void fromDouble(byte[] b, int start, double d, Endiannes e) {
        if (e == Endiannes.BIG) {
            fromLong(b, start, Long.reverseBytes(Double.doubleToLongBits(d)), Endiannes.LITTLE);
            return;
        }
        U.putDouble(b, OFFSET + start, d);
    }
}
