package com.nixiedroid.bytes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static com.nixiedroid.bytes.Endiannes.BIG;
import static com.nixiedroid.bytes.Endiannes.LITTLE;

class ByteArrayConverterTest {

    private final static int GLOBAL_STEP = 8;
    private final static long STEP_L = 1L << (Long.SIZE - GLOBAL_STEP);
    private final static int STEP_I = 1 << (Integer.SIZE - GLOBAL_STEP);
    private final static short STEP_S = 1 << (Short.SIZE - GLOBAL_STEP);
    private static final int FF = 0xFF;
    private static final int FFFF = 0xFFFF;
    final ByteArrayConverter converter = new ByteArrayConverterUnsafe();

    @Test
    void fromByteTest() {
        byte[] array = new byte[Byte.BYTES];
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            this.converter.writeByte(array, 0, i, BIG);
            Assertions.assertArrayEquals(StringArrayUtils.fromHexString(Integer.toHexString(i & FF)), array);
        }
    }

    @Test
    void fromShortTest() {
        byte[] array = new byte[Short.BYTES];
        for (short i = Short.MIN_VALUE; i < (Short.MAX_VALUE - STEP_S + 1); i += STEP_S) {
            byte[] expected = fromHexStringShortPadded(i);
            this.converter.writeShort(array, 0, i, BIG);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.writeShort(array, 0, i, LITTLE);
            Assertions.assertArrayEquals(expected, array);
        }
    }

    @Test
    void fromIntegerTest() {
        byte[] array = new byte[Integer.BYTES];
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE - STEP_I) + 1; i += STEP_I) {
            byte[] expected = fromHexStringIntPadded(i);
            this.converter.writeInteger(array, 0, i, BIG);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.writeInteger(array, 0, i, LITTLE);
            Assertions.assertArrayEquals(expected, array);
        }
    }

    @Test
    void fromFloatTest() {
        byte[] array = new byte[Float.BYTES];
        float f;
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE - STEP_I) + 1; i += STEP_I) {
            f = Float.intBitsToFloat(i);
            byte[] expected = fromHexStringFloatPadded(f);
            this.converter.writeFloat(array, 0, f, BIG);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.writeFloat(array, 0, f, LITTLE);
            Assertions.assertArrayEquals(expected, array);
        }
    }

    @Test
    void fromDoubleTest() {
        byte[] array = new byte[Double.BYTES];
        double d;
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE - STEP_L) + 1; i += STEP_L) {
            d = Double.longBitsToDouble(i);
            byte[] expected = fromHexStringDoublePadded(d);
            this.converter.writeDouble(array, 0, d, BIG);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.writeDouble(array, 0, d, LITTLE);
            Assertions.assertArrayEquals(expected, array);
        }
    }



    @Test
    void fromLongTest() {
        byte[] array = new byte[8];
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE - STEP_L) + 1; i += STEP_L) {
            byte[] expected = fromHexStringLongPadded(i);
            this.converter.writeLong(array, 0, i, BIG);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.writeLong(array, 0, i, LITTLE);
            Assertions.assertArrayEquals(expected, array);
        }
    }

    @Test
    void toByteTest() {
        byte[] array = new byte[1];
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            this.converter.writeByte(array, 0, i, LITTLE);
            Assertions.assertEquals(i, this.converter.readByte(array, 0, BIG));
        }
    }

    @Test
    void toShortTest() {
        byte[] array = new byte[2];
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            this.converter.writeShort(array, 0, i, LITTLE);
            Assertions.assertEquals(i, this.converter.readShort(array, 0, LITTLE));
            this.converter.writeShort(array, 0, i, BIG);
            Assertions.assertEquals(i, this.converter.readShort(array, 0, BIG));
        }
    }

    @Test
    void toIntegerTest() {
        byte[] array = new byte[4];
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE - STEP_I) + 1; i += STEP_I) {
            this.converter.writeInteger(array, 0, i, LITTLE);
            Assertions.assertEquals(i, this.converter.readInteger(array, 0, LITTLE));
            this.converter.writeInteger(array, 0, i, BIG);
            Assertions.assertEquals(i, this.converter.readInteger(array, 0, BIG));
        }
    }

    @Test
    void toLongTest() {
        byte[] array = new byte[8];
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE - STEP_L) + 1; i += STEP_L) {
            this.converter.writeLong(array, 0, i, LITTLE);
            Assertions.assertEquals(i, this.converter.readLong(array, 0, LITTLE));
            this.converter.writeLong(array, 0, i, BIG);
            Assertions.assertEquals(i, this.converter.readLong(array, 0, BIG));
        }
    }


    @Test
    void toFloatTest() {
        float f;
        byte[] array = new byte[4];
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE - STEP_I) + 1; i += STEP_I) {
            f = Float.intBitsToFloat(i);
            this.converter.writeFloat(array, 0, f, LITTLE);
            Assertions.assertEquals(f, this.converter.readFloat(array, 0, LITTLE));
            this.converter.writeFloat(array, 0, f, BIG);
            Assertions.assertEquals(f, this.converter.readFloat(array, 0, BIG));
        }
    }

    @Test
    void toDoubleTest() {
        byte[] array = new byte[8];
        double d;
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE - STEP_L) + 1; i += STEP_L) {
            d = Double.longBitsToDouble(i);
            this.converter.writeDouble(array, 0, d, LITTLE);
            Assertions.assertEquals(d, this.converter.readDouble(array, 0, LITTLE));
            this.converter.writeDouble(array, 0, d, BIG);
            Assertions.assertEquals(d, this.converter.readDouble(array, 0, BIG));
        }
    }

    private byte[] fromHexStringShortPadded(short i) {
        return StringArrayUtils.fromHexString(
                String.format("%1$" + Integer.BYTES + "s",
                        Integer.toHexString(i & FFFF)).replace(' ', '0')
        );
    }

    private byte[] fromHexStringIntPadded(int i) {
        return StringArrayUtils.fromHexString(
                String.format("%1$" + Integer.BYTES * 2 + "s",
                        Integer.toHexString(i)).replace(' ', '0')
        );
    }

    private byte[] fromHexStringLongPadded(long i) {
        return StringArrayUtils.fromHexString(
                String.format("%1$" + Long.BYTES * 2 + "s",
                        Long.toHexString(i)).replace(' ', '0')
        );
    }


    private byte[] fromHexStringFloatPadded(float f) {
        return StringArrayUtils.fromHexString(
                String.format("%1$" + Float.BYTES * 2 + "s",
                        Integer.toHexString(Float.floatToIntBits(f))).replace(' ', '0')
        );
    }

    private byte[] fromHexStringDoublePadded(double d) {
        return StringArrayUtils.fromHexString(
                String.format("%1$" + Double.BYTES * 2 + "s",
                        Long.toHexString(Double.doubleToLongBits(d))).replace(' ', '0')
        );
    }

}