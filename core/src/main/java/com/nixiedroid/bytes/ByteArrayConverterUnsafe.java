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
    public byte readByte(byte[] b, int start, Endiannes e) {
        return U.getByte(b, OFFSET + start);
    }

    @Override
    public short readShort(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Short.reverseBytes(readShort(b, start, Endiannes.LITTLE));
        }
        return U.getShort(b, OFFSET + start);
    }

    @Override
    public int readInteger(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Integer.reverseBytes(readInteger(b, start, Endiannes.LITTLE));
        }
        return U.getInt(b, OFFSET + start);
    }

    @Override
    public long readLong(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Long.reverseBytes(readLong(b, start, Endiannes.LITTLE));
        }
        return U.getLong(b, OFFSET + start);
    }

    @Override
    public float readFloat(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Float.intBitsToFloat(readInteger(b, start, Endiannes.BIG));
        }
        return U.getFloat(b, OFFSET + start);
    }

    @Override
    public double readDouble(byte[] b, int start, Endiannes e) {
        if (e == Endiannes.BIG) {
            return Double.longBitsToDouble(readLong(b, start, Endiannes.BIG));
        }
        return U.getDouble(b, OFFSET + start);
    }

    @Override
    public void writeByte(byte[] b, int start, byte by, Endiannes e) {
        U.putByte(b, OFFSET + start, by);
    }

    @Override
    public void writeShort(byte[] b, int start, short s, Endiannes e) {
        if (e == Endiannes.BIG) {
            writeShort(b, start, Short.reverseBytes(s), Endiannes.LITTLE);
            return;
        }
        U.putShort(b, OFFSET + start, s);
    }

    @Override
    public void writeInteger(byte[] b, int start, int i, Endiannes e) {
        if (e == Endiannes.BIG) {
            writeInteger(b, start, Integer.reverseBytes(i), Endiannes.LITTLE);
            return;
        }
        U.putInt(b, OFFSET + start, i);
    }

    @Override
    public void writeLong(byte[] b, int start, long l, Endiannes e) {
        if (e == Endiannes.BIG) {
            writeLong(b, start, Long.reverseBytes(l), Endiannes.LITTLE);
            return;
        }
        U.putLong(b, OFFSET + start, l);
    }

    @Override
    public void writeFloat(byte[] b, int start, float f, Endiannes e) {
        if (e == Endiannes.BIG) {
            writeInteger(b, start, Integer.reverseBytes(Float.floatToIntBits(f)), Endiannes.LITTLE);
            return;
        }
        U.putFloat(b, OFFSET + start, f);
    }

    @Override
    public void writeDouble(byte[] b, int start, double d, Endiannes e) {
        if (e == Endiannes.BIG) {
            writeLong(b, start, Long.reverseBytes(Double.doubleToLongBits(d)), Endiannes.LITTLE);
            return;
        }
        U.putDouble(b, OFFSET + start, d);
    }
}
