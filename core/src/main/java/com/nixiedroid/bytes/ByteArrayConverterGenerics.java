package com.nixiedroid.bytes;

public class ByteArrayConverterGenerics implements ByteArrayConverter {

    public <T extends Number> T toValue(final byte[] bytes, int start, Class<T> targetClass, Endiannes e) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        if (start < 0 || start >= bytes.length) throw new IllegalArgumentException("Invalid start index");
        int remaining = bytes.length - start;
        long out = 0;

        if (e == Endiannes.BIG) {
            if (targetClass == Byte.class) {
                out = bytes[start];
            } else if (targetClass == Short.class && remaining >= 2) {
                for (int i = 0; i < 2; i++) {
                    out |= ((long) bytes[start + i] & FF) << ((1 - i) << 3);
                }
                return targetClass.cast((short) out);
            } else if (targetClass == Integer.class && remaining >= 4) {
                for (int i = 0; i < 4; i++) {
                    out |= ((long) bytes[start + i] & FF) << ((3 - i) << 3);
                }
                return targetClass.cast((int) out);
            } else if (targetClass == Long.class && remaining >= 8) {
                for (int i = 0; i < 8; i++) {
                    out |= ((long) bytes[start + i] & FF) << ((7 - i) << 3);
                }
                return targetClass.cast(out);
            } else if (targetClass == Float.class && remaining >= 4) {
                int intBits = 0;
                for (int i = 0; i < 4; i++) {
                    intBits |= (bytes[start + i] & FF) << ((3 - i) * 8);
                }
                return targetClass.cast(Float.intBitsToFloat(intBits));
            } else if (targetClass == Double.class && remaining >= 8) {
                long longBits = 0;
                for (int i = 0; i < 8; i++) {
                    longBits |= (long) (bytes[start + i] & FF) << ((7 - i) * 8);
                }
                return targetClass.cast(Double.longBitsToDouble(longBits));
            } else {
                throw new IllegalArgumentException("Unsupported target class or insufficient bytes");
            }
        } else {
            if (targetClass == Byte.class) {
                out = bytes[start];
            } else if (targetClass == Short.class && remaining >= 2) {
                for (int i = 0; i < 2; i++) {
                    out |= ((long) bytes[start + i] & FF) << (i << 3);
                }
                return targetClass.cast((short) out);
            } else if (targetClass == Integer.class && remaining >= 4) {
                for (int i = 0; i < 4; i++) {
                    out |= ((long) bytes[start + i] & FF) << (i << 3);
                }
                return targetClass.cast((int) out);
            } else if (targetClass == Long.class && remaining >= 8) {
                for (int i = 0; i < 8; i++) {
                    out |= ((long) bytes[start + i] & FF) << (i << 3);
                }
                return targetClass.cast(out);
            } else if (targetClass == Float.class && remaining >= 4) {
                int intBits = 0;
                for (int i = 0; i < 4; i++) {
                    intBits |= (bytes[start + i] & FF) << (i * 8);
                }
                return targetClass.cast(Float.intBitsToFloat(intBits));
            } else if (targetClass == Double.class && remaining >= 8) {
                long longBits = 0;
                for (int i = 0; i < 8; i++) {
                    longBits |= (long) (bytes[start + i] & FF) << (i * 8);
                }
                return targetClass.cast(Double.longBitsToDouble(longBits));
            } else {
                throw new IllegalArgumentException("Unsupported target class or insufficient bytes");
            }
        }

        return targetClass.cast((byte) out);
    }

    public <T extends Number> void fromValue(byte[] b, int start, final T object, Endiannes e) {
        if (object == null) throw new IllegalArgumentException("Object is null");
        if (b == null) throw new IllegalArgumentException("Output array is null");
        if (start < 0 || start >= b.length) throw new IllegalArgumentException("Invalid start index");

        int byteCount;
        long value;

        if (object instanceof Integer) {
            byteCount = Integer.BYTES;
            value = object.intValue();
        } else if (object instanceof Byte) {
            byteCount = Byte.BYTES;
            value = object.byteValue();
        } else if (object instanceof Short) {
            byteCount = Short.BYTES;
            value = object.shortValue();
        } else if (object instanceof Long) {
            byteCount = Long.BYTES;
            value = object.longValue();
        } else if (object instanceof Float) {
            byteCount = Float.BYTES;
            value = Float.floatToIntBits(object.floatValue());
        } else if (object instanceof Double) {
            byteCount = Double.BYTES;
            value = Double.doubleToLongBits(object.doubleValue());
        } else {
            throw new IllegalArgumentException("Unsupported object type");
        }

        if (start + byteCount > b.length) throw new IllegalArgumentException("Output array is too small");

        if (e == Endiannes.BIG) {
            for (int i = 0; i < byteCount; i++) {
                b[start + i] = (byte) ((value >> ((byteCount - 1 - i) << 3)) & FF);
            }
            return;
        }
        for (int i = 0; i < byteCount; i++) {
            b[start + i] = (byte) ((value >> (i << 3)) & FF);
        }
    }

    @Override
    public byte readByte(byte[] b, int start, Endiannes e) {
        return toValue(b, start, Byte.class, e);
    }

    @Override
    public short readShort(byte[] b, int start, Endiannes e) {
        return toValue(b, start, Short.class, e);
    }

    @Override
    public int readInteger(byte[] b, int start, Endiannes e) {
        return toValue(b, start, Integer.class, e);
    }

    @Override
    public long readLong(byte[] b, int start, Endiannes e) {
        return toValue(b, start, Long.class, e);
    }

    @Override
    public float readFloat(byte[] b, int start, Endiannes e) {
        return toValue(b, start, Float.class, e);
    }

    @Override
    public double readDouble(byte[] b, int start, Endiannes e) {
        return toValue(b, start, Double.class, e);
    }

    @Override
    public void writeByte(byte[] b, int start, byte by, Endiannes e) {
        fromValue(b, start, by, e);
    }

    @Override
    public void writeShort(byte[] b, int start, short s, Endiannes e) {
        fromValue(b, start, s, e);
    }

    @Override
    public void writeInteger(byte[] b, int start, int i, Endiannes e) {
        fromValue(b, start, i, e);
    }

    @Override
    public void writeLong(byte[] b, int start, long l, Endiannes e) {
        fromValue(b, start, l, e);
    }

    @Override
    public void writeFloat(byte[] b, int start, float f, Endiannes e) {
        fromValue(b, start, f, e);
    }

    @Override
    public void writeDouble(byte[] b, int start, double d, Endiannes e) {
        fromValue(b, start, d, e);
    }
}
