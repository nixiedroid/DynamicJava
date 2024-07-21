package com.nixiedroid.bytes;

public class ByteArrayConverterGenerics implements ByteArrayConverter {

    public <T extends Number> T fromBytesL(final byte[] bytes, int start, Class<T> targetClass) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        int count = bytes.length - 1 + start;
        long out = 0;
        if (count >= Long.BYTES) throw new IllegalArgumentException();
        for (int i = 0; i <= count; i++) {
            out |= ((long) bytes[start + i] & FF) << (i << 3);
        }
        switch (bytes.length - start) {
            case 1:
                return targetClass.cast((byte) out);
            case 2:
                return targetClass.cast((short) out);
            case 4:
                return targetClass.cast((int) out);
            default:
                return targetClass.cast(out);
        }
    }

    public <T extends Number> T fromBytesB(final byte[] bytes, int start,  Class<T> targetClass) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        int count = bytes.length - 1;
        long out = 0;
        if (count >= Long.BYTES) throw new IllegalArgumentException();
        for (int i = 0; i <= count; i++) {
            out |= ((long) bytes[i] & FF) << (count - i << 3);
        }
        switch (bytes.length) {
            case 1:
                return targetClass.cast((byte) out);
            case 2:
                return targetClass.cast((short) out);
            case 4:
                return targetClass.cast((int) out);
            default:
                return targetClass.cast(out);
        }
    }

    public <T extends Number> byte[] toBytesL(byte[] b, int start, final T object) {
        if (object == null) throw new IllegalArgumentException("Object is null");
        int count;
        if (object instanceof Integer) {
            count = Integer.BYTES - 1;
        } else if (object instanceof Byte) {
            count = Byte.BYTES - 1;
        } else if (object instanceof Short) {
            count = Short.BYTES - 1;
        } else if (object instanceof Long) {
            count = Long.BYTES - 1;
        } else throw new IllegalArgumentException();
        byte[] out = new byte[count + 1];
        for (int i = 0; i <= count; i++) {
            out[i] = (byte) ((object.longValue() >> (i << 3)) & FF);
        }
        return out;
    }

    public <T extends Number> byte[] toBytesB(byte[] b, int start, final T object) {
        if (object == null) throw new IllegalArgumentException("Object is null");
        int count;
        if (object instanceof Integer) {
            count = Integer.BYTES - 1;
        } else if (object instanceof Byte) {
            count = Byte.BYTES - 1;
        } else if (object instanceof Short) {
            count = Short.BYTES - 1;
        } else if (object instanceof Long) {
            count = Long.BYTES - 1;
        } else throw new IllegalArgumentException();
        byte[] out = new byte[count + 1];
        for (int i = 0; i <= count; i++) {
            out[i] = (byte) ((object.longValue() >> ((count - i) << 3)) & FF);
        }
        return out;
    }

    @Override
    public byte toInt8(byte[] b, int start) {
        return 0;
    }

    @Override
    public short toInt16B(byte[] b, int start) {
        return 0;
    }

    @Override
    public short toInt16L(byte[] b, int start) {
        return 0;
    }

    @Override
    public int toInt32B(byte[] b, int start) {
        return 0;
    }

    @Override
    public int toInt32L(byte[] b, int start) {
        return 0;
    }

    @Override
    public long toInt64B(byte[] b, int start) {
        return 0;
    }

    @Override
    public long toInt64L(byte[] b, int start) {
        return 0;
    }

    @Override
    public void int8ToBytes(byte[] b, int start, byte by) {

    }

    @Override
    public void int16ToBytesB(byte[] b, int start, short s) {

    }

    @Override
    public void int16ToBytesL(byte[] b, int start, short s) {

    }

    @Override
    public void int32ToBytesB(byte[] b, int start, int i) {

    }

    @Override
    public void int32ToBytesL(byte[] b, int start, int i) {

    }

    @Override
    public void int64ToBytesB(byte[] b, int start, long l) {

    }

    @Override
    public void int64ToBytesL(byte[] b, int start, long l) {

    }
}
