package com.nixiedroid.bytes.converter;

/**
 * BIG
 * b[start + 0] =  (byte) ((l >> 56) & FF);
 * b[start + 1] =  (byte) ((l >> 48) & FF);
 * b[start + 2] =  (byte) ((l >> 40) & FF);
 * b[start + 3] =  (byte) ((l >> 32) & FF);
 * b[start + 4] =  (byte) ((l >> 24) & FF);
 * b[start + 5] =  (byte) ((l >> 16) & FF);
 * b[start + 6] =  (byte) ((l >>  8) & FF);
 * b[start + 7] =  (byte) ((l >>  0) & FF);
 * LITTLE:
 * b[start + 0] =  (byte) ((l >>  0) & FF);
 * b[start + 1] =  (byte) ((l >>  8) & FF);
 * b[start + 2] =  (byte) ((l >> 16) & FF);
 * b[start + 3] =  (byte) ((l >> 24) & FF);
 * b[start + 4] =  (byte) ((l >> 32) & FF);
 * b[start + 5] =  (byte) ((l >> 40) & FF);
 * b[start + 6] =  (byte) ((l >> 48) & FF);
 * b[start + 7] =  (byte) ((l >> 56) & FF);
 */
// @formatter:off
@SuppressWarnings({"unused", "MagicNumber", "DuplicatedCode"})
public class ByteArrayConverterDefault implements ByteArrayConverter {

    @Override
    public byte readByte(byte[] b, int start, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + BYTE) throw new IllegalArgumentException();
        return (byte) (b[start] & FF);
    }

    @Override
    public short readShort(byte[] b, int start, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + SHORT) throw new IllegalArgumentException();
        if (e == Endianness.BIG_ENDIAN) {
            return (short) ((b[start + 1] & FF) |
                    (b[start] & FF) << 8);
        }
        return (short) ((b[start] & FF) |
                (b[start + 1] & FF) << 8);
    }

    @Override
    public int readInteger(byte[] b, int start, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + INTEGER) throw new IllegalArgumentException();
        if (e == Endianness.BIG_ENDIAN) {
            return (b[start + 3] & FF) |
                    (b[start + 2] & FF) << 8 |
                    (b[start + 1] & FF) << 16 |
                    (b[start] & FF) << 24;
        }
        return (b[start] & FF) |
                (b[start + 1] & FF) << 8 |
                (b[start + 2] & FF) << 16 |
                (b[start + 3] & FF) << 24;
    }

    @Override
    public long readLong(byte[] b, int start, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + LONG) throw new IllegalArgumentException();
        if (e == Endianness.BIG_ENDIAN) {
            return ((long) b[start] & FF) << 56 |
                    ((long) b[start + 1] & FF) << 48 |
                    ((long) b[start + 2] & FF) << 40 |
                    ((long) b[start + 3] & FF) << 32 |
                    ((long) b[start + 4] & FF) << 24 |
                    ((long) b[start + 5] & FF) << 16 |
                    ((long) b[start + 6] & FF) << 8 |
                    ((long) b[start + 7] & FF);
        }
        return ((long) b[start + 7] & FF) << 56 |
                ((long) b[start + 6] & FF) << 48 |
                ((long) b[start + 5] & FF) << 40 |
                ((long) b[start + 4] & FF) << 32 |
                ((long) b[start + 3] & FF) << 24 |
                ((long) b[start + 2] & FF) << 16 |
                ((long) b[start + 1] & FF) << 8 |
                ((long) b[start] & FF);
    }

    @Override
    public float readFloat(byte[] b, int start, Endianness e) {
        return Float.intBitsToFloat(readInteger(b, start, e));
    }

    @Override
    public double readDouble(byte[] b, int start, Endianness e) {
        return Double.longBitsToDouble(readLong(b, start, e));
    }

    @Override
    public void writeByte(byte[] b, int start, byte by, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + BYTE) throw new IllegalArgumentException();
        b[start] = (byte) (by & FF);
    }

    @Override
    public void writeShort(byte[] b, int start, short s, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + SHORT) throw new IllegalArgumentException();
        if (e == Endianness.BIG_ENDIAN) {
            b[start] = (byte) ((s >> 8) & FF);
            b[start + 1] = (byte) (s & FF);
            return;
        }
        b[start] = (byte) (s & FF);
        b[start + 1] = (byte) ((s >> 8) & FF);
    }

    @Override
    public void writeInteger(byte[] b, int start, int i, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + INTEGER) throw new IllegalArgumentException();
        if (e == Endianness.BIG_ENDIAN) {
            b[start] = (byte) ((i >> 24) & FF);
            b[start + 1] = (byte) ((i >> 16) & FF);
            b[start + 2] = (byte) ((i >> 8) & FF);
            b[start + 3] = (byte) (i & FF);
            return;
        }
        b[start] = (byte) (i & FF);
        b[start + 1] = (byte) ((i >> 8) & FF);
        b[start + 2] = (byte) ((i >> 16) & FF);
        b[start + 3] = (byte) ((i >> 24) & FF);
    }

    @Override
    public void writeLong(byte[] b, int start, long l, Endianness e) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length < start + LONG) throw new IllegalArgumentException();
        if (e == Endianness.BIG_ENDIAN) {
            b[start] = (byte) ((l >> 56) & FF);
            b[start + 1] = (byte) ((l >> 48) & FF);
            b[start + 2] = (byte) ((l >> 40) & FF);
            b[start + 3] = (byte) ((l >> 32) & FF);
            b[start + 4] = (byte) ((l >> 24) & FF);
            b[start + 5] = (byte) ((l >> 16) & FF);
            b[start + 6] = (byte) ((l >> 8) & FF);
            b[start + 7] = (byte) (l & FF);
            return;
        }
        b[start] = (byte) (l & FF);
        b[start + 1] = (byte) ((l >> 8) & FF);
        b[start + 2] = (byte) ((l >> 16) & FF);
        b[start + 3] = (byte) ((l >> 24) & FF);
        b[start + 4] = (byte) ((l >> 32) & FF);
        b[start + 5] = (byte) ((l >> 40) & FF);
        b[start + 6] = (byte) ((l >> 48) & FF);
        b[start + 7] = (byte) ((l >> 56) & FF);
    }

    @Override
    public void writeFloat(byte[] b, int start, float f, Endianness e) {
        writeInteger(b,start,Float.floatToIntBits(f),e);
    }

    @Override
    public void writeDouble(byte[] b, int start, double d, Endianness e) {
        writeLong(b,start,Double.doubleToLongBits(d),e);
    }
}
