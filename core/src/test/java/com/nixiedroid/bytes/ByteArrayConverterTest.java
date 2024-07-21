package com.nixiedroid.bytes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ByteArrayConverterTest {

    final ByteArrayConverter converter = new ByteArrayConverterDefault();
    final static int GLOBAL_STEP = 4;
    final static long STEP_L = 1L << (Long.SIZE - GLOBAL_STEP);
    final static int STEP_I = 1 << (Integer.SIZE - GLOBAL_STEP);
    final static short STEP_S = 1 << (Short.SIZE - GLOBAL_STEP);
    private static final int FF = 0xFF;
    private static final int FFFF = 0xFFFF;

    @Test
    void toBytesTest() {
        byte[] array = new byte[1];
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            this.converter.int8ToBytes(array, 0, i);
            Assertions.assertArrayEquals(StringArrayUtils.fromHexString(Integer.toHexString(i & FF)), array);
        }
        array = new byte[2];
        for (short i = Short.MIN_VALUE; i < (Short.MAX_VALUE - STEP_S + 1); i += STEP_S) {
            byte[] expected = fromHexStringShortPadded(i);
            this.converter.int16ToBytesB(array, 0, i);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.int16ToBytesL(array, 0, i);
            Assertions.assertArrayEquals(expected, array);
        }
        array = new byte[4];
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE - STEP_I) + 1; i += STEP_I) {
            byte[] expected = fromHexStringIntPadded(i);
            this.converter.int32ToBytesB(array, 0, i);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.int32ToBytesL(array, 0, i);
            Assertions.assertArrayEquals(expected, array);
        }
        array = new byte[8];
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE - STEP_L) + 1; i += STEP_L) {
            byte[] expected = fromHexStringLongPadded(i);
            this.converter.int64ToBytesB(array, 0, i);
            Assertions.assertArrayEquals(expected, array);
            ByteArrays.reverse(expected);
            this.converter.int64ToBytesL(array, 0, i);
            Assertions.assertArrayEquals(expected, array);
        }
    }


    @Test
    void fromBytesTest() {
        byte[] array = new byte[1];
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            this.converter.int8ToBytes(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt8(array, 0));
        }
        array = new byte[2];
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            this.converter.int16ToBytesL(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt16L(array, 0));
            this.converter.int16ToBytesB(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt16B(array, 0));
        }
        array = new byte[4];
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE - STEP_I) + 1; i += STEP_I) {
            this.converter.int32ToBytesL(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt32L(array, 0));
            this.converter.int32ToBytesB(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt32B(array, 0));
        }
        array = new byte[8];
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE - STEP_L) + 1; i += STEP_L) {
            this.converter.int64ToBytesL(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt64L(array, 0));
            this.converter.int64ToBytesB(array, 0, i);
            Assertions.assertEquals(i, this.converter.toInt64B(array, 0));
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


}