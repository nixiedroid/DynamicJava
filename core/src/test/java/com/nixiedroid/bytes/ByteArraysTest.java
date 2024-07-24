package com.nixiedroid.bytes;

import com.nixiedroid.bytes.converter.ByteArrays;
import com.nixiedroid.bytes.converter.StringArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class ByteArraysTest {

    @Test
    void equalsTest() {
        Assertions.assertTrue(null == null);
        Assertions.assertTrue(ByteArrays.equals(null, null));
        Assertions.assertFalse(ByteArrays.equals(null, new byte[0]));
        Assertions.assertFalse(ByteArrays.equals(new byte[0], null));
        Assertions.assertTrue(ByteArrays.equals(new byte[0], new byte[0]));
        Assertions.assertTrue(ByteArrays.equals(new byte[]{0, 1, 2}, new byte[]{0, 1, 2}));
        Assertions.assertFalse(ByteArrays.equals(new byte[]{0, 1, 2}, new byte[]{0, 2, 2}));
    }


    @Test
    void xorTest() {
        byte[] array = StringArrayUtils.fromHexString("AA00");
        ByteArrays.xor(array, StringArrayUtils.fromHexString("00AA"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("AAAA"), array);
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteArrays.xor(null, null));
        array = new byte[0];
        ByteArrays.xor(array, new byte[0]);
        Assertions.assertArrayEquals(new byte[0], array);
        array = new byte[1];
        ByteArrays.xor(array, new byte[1]);
        Assertions.assertArrayEquals(new byte[1], array);
    }


    @Test
    void reverseTest() {
        byte[] array = StringArrayUtils.fromHexString("443322");
        ByteArrays.reverse(array);
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("223344"), array);
        byte[] expected = StringArrayUtils.fromHexString("EFCDAB9078563412");
        byte[] tested = StringArrayUtils.fromHexString("1234567890ABCDEF");
        ByteArrays.reverse(tested);
        Assertions.assertArrayEquals(expected, tested);
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteArrays.reverse(null));
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            array = new byte[]{i};
            ByteArrays.reverse(array);
            Assertions.assertArrayEquals(new byte[]{i}, array);
        }
    }



}