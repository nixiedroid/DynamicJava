package com.nixiedroid.bytes;

public class ByteArrayConverterGenerics implements ByteArrayConverter {

    public <T extends Number> T toValueL(final byte[] bytes, int start, Class<T> targetClass) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        if (start < 0 || start >= bytes.length) throw new IllegalArgumentException("Invalid start index");

        int remaining = bytes.length - start;
        long out = 0;

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

        return targetClass.cast((byte) out);
    }

    public <T extends Number> T toValueB(final byte[] bytes, int start, Class<T> targetClass) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        if (start < 0 || start >= bytes.length) throw new IllegalArgumentException("Invalid start index");
        int remaining = bytes.length - start;
        long out = 0;

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

        // For byte (special case handled separately)
        return targetClass.cast((byte) out);
    }

    public <T extends Number> void fromValueL(byte[] b, int start, final T object) {
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

        for (int i = 0; i < byteCount; i++) {
            b[start + i] = (byte) ((value >> (i << 3)) & FF);
        }
    }

    public <T extends Number> void fromValueB(byte[] b, int start, final T object) {
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

        for (int i = 0; i < byteCount; i++) {
            b[start + i] = (byte) ((value >> ((byteCount - 1 - i) << 3)) & FF);
        }
    }

    @Override
    public byte toByte(byte[] b, int start) {
        return toValueB(b,start,Byte.class);
    }

    @Override
    public short toShortB(byte[] b, int start) {
        return toValueB(b,start,Short.class);
    }

    @Override
    public short toShortL(byte[] b, int start) {
        return toValueL(b,start,Short.class);
    }

    @Override
    public int toIntegerB(byte[] b, int start) {
        return toValueB(b,start,Integer.class);
    }

    @Override
    public int toIntegerL(byte[] b, int start) {
        return toValueL(b,start,Integer.class);
    }

    @Override
    public long toLongB(byte[] b, int start) {
        return toValueB(b,start,Long.class);
    }

    @Override
    public long toLongL(byte[] b, int start) {
        return toValueL(b,start,Long.class);
    }

    @Override
    public float toFloatB(byte[] b, int start) {
        return toValueB(b,start,Float.class);
    }

    @Override
    public float toFloatL(byte[] b, int start) {
        return toValueL(b,start,Float.class);
    }

    @Override
    public double toDoubleB(byte[] b, int start) {
        return toValueB(b,start,Double.class);
    }

    @Override
    public double toDoubleL(byte[] b, int start) {
        return toValueL(b,start,Double.class);
    }

    @Override
    public void fromByte(byte[] b, int start, byte by) {
            fromValueB(b,start,by);
    }

    @Override
    public void fromShortB(byte[] b, int start, short s) {
        fromValueB(b,start,s);
    }

    @Override
    public void fromShortL(byte[] b, int start, short s) {
        fromValueL(b,start,s);
    }

    @Override
    public void fromIntegerB(byte[] b, int start, int i) {
        fromValueB(b,start,i);
    }

    @Override
    public void fromIntegerL(byte[] b, int start, int i) {
        fromValueL(b,start,i);
    }

    @Override
    public void fromLongB(byte[] b, int start, long l) {
        fromValueB(b,start,l);
    }

    @Override
    public void fromLongL(byte[] b, int start, long l) {
        fromValueL(b,start,l);
    }
}
