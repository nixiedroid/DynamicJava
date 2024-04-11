package com.nixiedroid.bytes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.math.BigInteger;

import static com.nixiedroid.bytes.ByteArrayUtils.*;

class ByteArrayUtilsTest {
    static ByteArrayUtils inst;

    @BeforeAll
    static void init() {
        inst = ByteArrayUtils.i();
    }

    @Test
    void instanceTest() {
        Assertions.assertNotNull(ByteArrayUtils.i());
        Assertions.assertSame(ByteArrayUtils.i(), inst);
        Assertions.assertSame(ByteArrayUtils.i(),ByteArrayUtils.i());
    }

    @Test
    void equalsTest() {
        Assertions.assertTrue(null == null);
        Assertions.assertTrue(isEquals(null, null));
        Assertions.assertFalse(isEquals(null, new byte[0]));
        Assertions.assertFalse(isEquals(new byte[0], null));
        Assertions.assertTrue(isEquals(new byte[0], new byte[0]));
        Assertions.assertTrue(isEquals(new byte[]{0, 1, 2}, new byte[]{0, 1, 2}));
        Assertions.assertFalse(isEquals(new byte[]{0, 1, 2}, new byte[]{0, 2, 2}));
    }

    @Test
    void fromHexStringTest() {
        String hexString = "ABCD15364216";
        String hexStringPartiallyValid = "ABCD153642160";
        byte[] expected = new byte[]{(byte) 0xAB, (byte) 0xCD, 0x15, 0x36, 0x42, 0x16};
        byte[] EMPTY = new byte[0];

        Assertions.assertArrayEquals(EMPTY, fromHexString(null));
        Assertions.assertArrayEquals(EMPTY, fromHexString(""));
        Assertions.assertArrayEquals(EMPTY, fromHexString("          "));
        Assertions.assertArrayEquals(EMPTY, fromHexString("HELLO THIS IS WRONG STRING"));
        Assertions.assertArrayEquals(expected, fromHexString(hexString));
        Assertions.assertArrayEquals(expected, fromHexString(hexStringPartiallyValid));
        Assertions.assertArrayEquals(new byte[]{10}, fromHexString("A"));
    }

    @Test
    void xorTest() {
        Assertions.assertArrayEquals(
                fromHexString("AAAA"),
                xor(
                        fromHexString("AA00"),
                        fromHexString("00AA")
                )
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> xor(null, null));
        Assertions.assertArrayEquals(new byte[0],xor(new byte[0],new byte[0]));
        Assertions.assertArrayEquals(new byte[0],xor(new byte[0],new byte[1]));
        Assertions.assertArrayEquals(new byte[0],xor(new byte[1],new byte[0]));
        Assertions.assertArrayEquals(
                fromHexString("AAAA"),
                xor(
                        fromHexString("AA00CC"),
                        fromHexString("00AA")
                )
        );
        Assertions.assertArrayEquals(
                fromHexString("AAAA"),
                xor(
                        fromHexString("AA00"),
                        fromHexString("00AACC")
                )
        );
    }

    @Test
    void utf16toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()->utf16toBytes(null));
        Assertions.assertArrayEquals(fromHexString("31003100"),utf16toBytes("11"));
        Assertions.assertArrayEquals(fromHexString("00000000"),utf16toBytes("\0\0"));
        Assertions.assertArrayEquals(fromHexString("3A003A00"),utf16toBytes("::"));
        Assertions.assertArrayEquals(fromHexString(
                "480065006C006C006F00200077006F0072006C0064002100"
        ),utf16toBytes("Hello world!"));
        Assertions.assertArrayEquals(fromHexString("00310031"),utf16toBytes("11",Endiannes.BIG));
        Assertions.assertArrayEquals(fromHexString("00000000"),utf16toBytes("\0\0",Endiannes.BIG));
        Assertions.assertArrayEquals(fromHexString("003A003A"),utf16toBytes("::",Endiannes.BIG));
        Assertions.assertArrayEquals(fromHexString(
                "00480065006C006C006F00200077006F0072006C00640021"
        ),utf16toBytes("Hello world!",Endiannes.BIG));

    }

    @Test
    void utf8toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()->utf8toBytes(null));
        Assertions.assertArrayEquals(fromHexString("3131"),utf8toBytes("11"));
        Assertions.assertArrayEquals(fromHexString("0000"),utf8toBytes("\0\0"));
        Assertions.assertArrayEquals(fromHexString("3A3A"),utf8toBytes("::"));
        Assertions.assertArrayEquals(fromHexString("48656C6C6F20776F726C6421"),utf8toBytes("Hello world!"));
    }

    @Test
    void reverseTest() {
        Assertions.assertArrayEquals(fromHexString("223344"), reverse(fromHexString("443322")));
        byte[] expected = fromHexString("EFCDAB9078563412");
        byte[] tested = fromHexString("1234567890ABCDEF");
        Assertions.assertArrayEquals(expected, reverse(tested));
        Assertions.assertArrayEquals(fromHexString("00AA"), reverse(fromHexString("AA00")));
        Assertions.assertThrows(IllegalArgumentException.class,()->reverse(null));
        Assertions.assertArrayEquals(new byte[0],reverse(new byte[0]));
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(new byte[]{i},reverse(new byte[]{i}));
        }
    }

    @Test
    void print() {
    }

    @Test
    void testToString() {
        Assertions.assertEquals("AABBAA",ByteArrayUtils.toString(fromHexString("AABBAA")));
        Assertions.assertEquals("",ByteArrayUtils.toString(fromHexString("")));
        Assertions.assertEquals("",ByteArrayUtils.toString(new byte[0]));
        Assertions.assertEquals("01",ByteArrayUtils.toString(new byte[]{1}));
        Assertions.assertEquals("0B",ByteArrayUtils.toString(new byte[]{11}));
        Assertions.assertThrows(IllegalArgumentException.class,()->ByteArrayUtils.toString(null));
    }

    @Test
    void toBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(
                    fromHexString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    fromHexString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    fromHexString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            byte[] expected = fromHexStringShortPadded(i);
            byte[] test = inst.toBytes(i);
            Assertions.assertArrayEquals(
                    reverse(expected), test
            );
            test = inst.toBytes(i, Endiannes.LITTLE);
            Assertions.assertArrayEquals(
                    reverse(expected), test
            );
            test = inst.toBytes(i, Endiannes.BIG);
            Assertions.assertArrayEquals(
                    expected, test
            );
        }
        final  int STEP_I = 1 << (Integer.SIZE-6);
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE-STEP_I)+1; i += STEP_I ) {
            byte[] expected = fromHexStringIntPadded(i);
            Assertions.assertArrayEquals(
                    reverse(expected),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    reverse(expected),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    expected,
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        final long STEP_L = 1L << (Long.SIZE-6);
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE-STEP_L)+1; i += STEP_L) {
            byte[] expected = fromHexStringLongPadded(i);
            Assertions.assertArrayEquals(
                    reverse(expected),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    reverse(expected),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    expected,
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        Assertions.assertThrows(IllegalArgumentException.class, () -> inst.toBytes(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> inst.toBytes(new BigInteger(String.valueOf(1))));
    }

    private byte[] fromHexStringShortPadded(short i) {
        return fromHexString(
                String.format("%1$" + Integer.BYTES + "s",
                        Integer.toHexString(i & 0xFFFF)).replace(' ', '0')
        );
    }

    private byte[] fromHexStringIntPadded(int i) {
        return fromHexString(
                String.format("%1$" + Integer.BYTES * 2 + "s",
                        Integer.toHexString(i)).replace(' ', '0')
        );
    }

    private byte[] fromHexStringLongPadded(long i) {
        return fromHexString(
                String.format("%1$" + Long.BYTES * 2 + "s",
                        Long.toHexString(i)).replace(' ', '0')
        );
    }

    @Test
    void fromBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertEquals(i, (byte) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (byte) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (byte) inst.fromBytes(inst.toBytes(i), Endiannes.BIG));
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            Assertions.assertEquals(i, (short) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (short) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (short) inst.fromBytes(inst.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
        final  int STEP_I = 1 << (Integer.SIZE-6);
        for (int i = Integer.MIN_VALUE; i < (Integer.MAX_VALUE-STEP_I)+1; i += STEP_I ) {
            Assertions.assertEquals(i, (int) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (int) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (int) inst.fromBytes(inst.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
        final long STEP_L = 1L << (Long.SIZE-6);
        for (long i = Long.MIN_VALUE; i < (Long.MAX_VALUE-STEP_L)+1; i += STEP_L) {
            Assertions.assertEquals(i, (long) inst.fromBytes(inst.toBytes(i)));
            Assertions.assertEquals(i, (long) inst.fromBytes(inst.toBytes(i), Endiannes.LITTLE));
            Assertions.assertEquals(i, (long) inst.fromBytes(inst.toBytes(i,Endiannes.BIG), Endiannes.BIG));
        }
    }
}