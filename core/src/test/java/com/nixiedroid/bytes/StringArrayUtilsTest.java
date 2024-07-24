package com.nixiedroid.bytes;

import com.nixiedroid.bytes.converter.Endianness;
import com.nixiedroid.bytes.converter.StringArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class StringArrayUtilsTest {

    @Test
    void fromHexStringTest() {
        String hexString = "ABCD15364216";
        String hexStringPartiallyValid = "ABCD153642160";
        byte[] expected = new byte[]{(byte) 0xAB, (byte) 0xCD, 0x15, 0x36, 0x42, 0x16};
        byte[] EMPTY = new byte[0];

        Assertions.assertArrayEquals(EMPTY, StringArrayUtils.fromHexString(null));
        Assertions.assertArrayEquals(EMPTY, StringArrayUtils.fromHexString(""));
        Assertions.assertArrayEquals(EMPTY, StringArrayUtils.fromHexString("          "));
        Assertions.assertArrayEquals(EMPTY, StringArrayUtils.fromHexString("HELLO THIS IS WRONG STRING"));
        Assertions.assertArrayEquals(expected, StringArrayUtils.fromHexString(hexString));
        Assertions.assertArrayEquals(expected, StringArrayUtils.fromHexString(hexStringPartiallyValid));
        Assertions.assertArrayEquals(new byte[]{10}, StringArrayUtils.fromHexString("A"));
    }

    @Test
    void utf16toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringArrayUtils.utf16toBytes(null));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("31003100"), StringArrayUtils.utf16toBytes("11"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("00000000"), StringArrayUtils.utf16toBytes("\0\0"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("3A003A00"), StringArrayUtils.utf16toBytes("::"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString(
                "480065006C006C006F00200077006F0072006C0064002100"
        ), StringArrayUtils.utf16toBytes("Hello world!"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("00310031"), StringArrayUtils.utf16toBytes("11", Endianness.BIG_ENDIAN));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("00000000"), StringArrayUtils.utf16toBytes("\0\0", Endianness.BIG_ENDIAN));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("003A003A"), StringArrayUtils.utf16toBytes("::", Endianness.BIG_ENDIAN));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString(
                "00480065006C006C006F00200077006F0072006C00640021"
        ), StringArrayUtils.utf16toBytes("Hello world!", Endianness.BIG_ENDIAN));

    }

    @Test
    void utf8toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringArrayUtils.utf8toBytes(null));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("3131"), StringArrayUtils.utf8toBytes("11"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("0000"), StringArrayUtils.utf8toBytes("\0\0"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("3A3A"), StringArrayUtils.utf8toBytes("::"));
        Assertions.assertArrayEquals(StringArrayUtils.fromHexString("48656C6C6F20776F726C6421"), StringArrayUtils.utf8toBytes("Hello world!"));
    }





    @Test
    void testToString() {
        Assertions.assertEquals("AABBAA", StringArrayUtils.toString(StringArrayUtils.fromHexString("AABBAA")));
        Assertions.assertEquals("", StringArrayUtils.toString(StringArrayUtils.fromHexString("")));
        Assertions.assertEquals("", StringArrayUtils.toString(new byte[0]));
        Assertions.assertEquals("01", StringArrayUtils.toString(new byte[]{1}));
        Assertions.assertEquals("0B", StringArrayUtils.toString(new byte[]{11}));
        Assertions.assertThrows(IllegalArgumentException.class, () -> StringArrayUtils.toString(null));
    }


}