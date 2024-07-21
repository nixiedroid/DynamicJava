package com.nixiedroid.bytes;

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
    public byte toInt8(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + BYTE) throw new IllegalArgumentException();
        return (byte) (b[start] & FF);
    }

    @Override
    public short toInt16B(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + SHORT) throw new IllegalArgumentException();
        return (short) ((b[start + 1] & FF) |
                (b[start] & FF) << 8);
    }

    @Override
    public short toInt16L(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + SHORT) throw new IllegalArgumentException();
        return (short) ((b[start] & FF) |
                (b[start + 1] & FF) << 8);
    }

    @Override
    public int toInt32B(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + INTEGER) throw new IllegalArgumentException();
        return (b[start + 3] & FF) |
                (b[start + 2] & FF) << 8 |
                (b[start + 1] & FF) << 16 |
                (b[start] & FF) << 24;
    }

    @Override
    public int toInt32L(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + INTEGER) throw new IllegalArgumentException();
        return (b[start] & FF) |
                (b[start + 1] & FF) << 8 |
                (b[start + 2] & FF) << 16 |
                (b[start + 3] & FF) << 24;
    }

    @Override
    public long toInt64B(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + LONG) throw new IllegalArgumentException();
        return ((long) b[start] & FF) << 56 |
                ((long) b[start + 1] & FF) << 48 |
                ((long) b[start + 2] & FF) << 40 |
                ((long) b[start + 3] & FF) << 32 |
                ((long) b[start + 4] & FF) << 24 |
                ((long) b[start + 5] & FF) << 16 |
                ((long) b[start + 6] & FF) << 8 |
                ((long) b[start + 7] & FF);
    }

    @Override
    public long toInt64L(byte[] b, int start) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + LONG) throw new IllegalArgumentException();
        return ((long) b[start + 7] & FF) << 56 |
                ((long) b[start + 6] & FF) << 48 |
                ((long) b[start + 5] & FF) << 40 |
                ((long) b[start + 4] & FF) << 32 |
                ((long) b[start + 3] & FF) << 24 |
                ((long) b[start + 2] & FF) << 16 |
                ((long) b[start + 1] & FF) << 8 |
                ((long) b[start] & FF);
    }
    
    
    @Override public void int8ToBytes(byte[] b, int start, byte by) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + BYTE) throw new IllegalArgumentException();
        b[start] = (byte) (by & FF);
    }


    @Override
    public void int16ToBytesB(byte[] b, int start, short s) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + SHORT) throw new IllegalArgumentException();
        b[start] = (byte) ((s >> 8) & FF);
        b[start + 1] = (byte) (s & FF);
    }

    @Override
    public void int16ToBytesL(byte[] b, int start, short s) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + SHORT) throw new IllegalArgumentException();
        b[start] = (byte) (s & FF);
        b[start + 1] = (byte) ((s >> 8) & FF);
    }

    @Override
    public void int32ToBytesB(byte[] b, int start, int i) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + INTEGER) throw new IllegalArgumentException();
        b[start] = (byte) ((i >> 24) & FF);
        b[start + 1] = (byte) ((i >> 16) & FF);
        b[start + 2] = (byte) ((i >> 8) & FF);
        b[start + 3] = (byte) (i & FF);
    }

    @Override
    public void int32ToBytesL(byte[] b, int start, int i) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + INTEGER) throw new IllegalArgumentException();
        b[start] = (byte) (i & FF);
        b[start + 1] = (byte) ((i >> 8) & FF);
        b[start + 2] = (byte) ((i >> 16) & FF);
        b[start + 3] = (byte) ((i >> 24) & FF);
    }


    @Override
    public void int64ToBytesB(byte[] b, int start, long l) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + LONG) throw new IllegalArgumentException();
        b[start] = (byte) ((l >> 56) & FF);
        b[start + 1] = (byte) ((l >> 48) & FF);
        b[start + 2] = (byte) ((l >> 40) & FF);
        b[start + 3] = (byte) ((l >> 32) & FF);
        b[start + 4] = (byte) ((l >> 24) & FF);
        b[start + 5] = (byte) ((l >> 16) & FF);
        b[start + 6] = (byte) ((l >> 8) & FF);
        b[start + 7] = (byte) (l & FF);
    }

    @Override
    public void int64ToBytesL(byte[] b, int start, long l) {
        if (b == null) throw new IllegalArgumentException();
        if (b.length <start + LONG) throw new IllegalArgumentException();
        b[start] = (byte) (l & FF);
        b[start + 1] = (byte) ((l >> 8) & FF);
        b[start + 2] = (byte) ((l >> 16) & FF);
        b[start + 3] = (byte) ((l >> 24) & FF);
        b[start + 4] = (byte) ((l >> 32) & FF);
        b[start + 5] = (byte) ((l >> 40) & FF);
        b[start + 6] = (byte) ((l >> 48) & FF);
        b[start + 7] = (byte) ((l >> 56) & FF);
    }
}
