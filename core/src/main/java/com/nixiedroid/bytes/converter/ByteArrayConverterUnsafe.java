package com.nixiedroid.bytes.converter;

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
    public byte readByte(byte[] b, int start, Endianness e) {
        return U.getByte(b, OFFSET + start);
    }

    @Override
    public short readShort(byte[] b, int start, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            return Short.reverseBytes(readShort(b, start, Endianness.LITTLE_ENDIAN));
        }
        return U.getShort(b, OFFSET + start);
    }

    @Override
    public int readInteger(byte[] b, int start, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            return Integer.reverseBytes(readInteger(b, start, Endianness.LITTLE_ENDIAN));
        }
        return U.getInt(b, OFFSET + start);
    }

    @Override
    public long readLong(byte[] b, int start, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            return Long.reverseBytes(readLong(b, start, Endianness.LITTLE_ENDIAN));
        }
        return U.getLong(b, OFFSET + start);
    }

    @Override
    public float readFloat(byte[] b, int start, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            return Float.intBitsToFloat(readInteger(b, start, Endianness.BIG_ENDIAN));
        }
        return U.getFloat(b, OFFSET + start);
    }

    @Override
    public double readDouble(byte[] b, int start, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            return Double.longBitsToDouble(readLong(b, start, Endianness.BIG_ENDIAN));
        }
        return U.getDouble(b, OFFSET + start);
    }

    @Override
    public void writeByte(byte[] b, int start, byte by, Endianness e) {
        U.putByte(b, OFFSET + start, by);
    }

    @Override
    public void writeShort(byte[] b, int start, short s, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            writeShort(b, start, Short.reverseBytes(s), Endianness.LITTLE_ENDIAN);
            return;
        }
        U.putShort(b, OFFSET + start, s);
    }

    @Override
    public void writeInteger(byte[] b, int start, int i, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            writeInteger(b, start, Integer.reverseBytes(i), Endianness.LITTLE_ENDIAN);
            return;
        }
        U.putInt(b, OFFSET + start, i);
    }

    @Override
    public void writeLong(byte[] b, int start, long l, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            writeLong(b, start, Long.reverseBytes(l), Endianness.LITTLE_ENDIAN);
            return;
        }
        U.putLong(b, OFFSET + start, l);
    }

    @Override
    public void writeFloat(byte[] b, int start, float f, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            writeInteger(b, start, Integer.reverseBytes(Float.floatToIntBits(f)), Endianness.LITTLE_ENDIAN);
            return;
        }
        U.putFloat(b, OFFSET + start, f);
    }

    @Override
    public void writeDouble(byte[] b, int start, double d, Endianness e) {
        if (e == Endianness.BIG_ENDIAN) {
            writeLong(b, start, Long.reverseBytes(Double.doubleToLongBits(d)), Endianness.LITTLE_ENDIAN);
            return;
        }
        U.putDouble(b, OFFSET + start, d);
    }
}
