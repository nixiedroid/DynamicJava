package bytes;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.bytes.Endiannes;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        Assertions.assertTrue(ByteArrayUtils.isEquals(null, null));
        Assertions.assertFalse(ByteArrayUtils.isEquals(null, new byte[0]));
        Assertions.assertFalse(ByteArrayUtils.isEquals(new byte[0], null));
        Assertions.assertTrue(ByteArrayUtils.isEquals(new byte[0], new byte[0]));
        Assertions.assertTrue(ByteArrayUtils.isEquals(new byte[]{0, 1, 2}, new byte[]{0, 1, 2}));
        Assertions.assertFalse(ByteArrayUtils.isEquals(new byte[]{0, 1, 2}, new byte[]{0, 2, 2}));
    }

    @Test
    void fromHexStringTest() {
        String hexString = "ABCD15364216";
        String hexStringPartiallyValid = "ABCD153642160";
        byte[] expected = new byte[]{(byte) 0xAB, (byte) 0xCD, 0x15, 0x36, 0x42, 0x16};
        byte[] EMPTY = new byte[0];

        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromHexString(null));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromHexString(""));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromHexString("          "));
        Assertions.assertArrayEquals(EMPTY, ByteArrayUtils.fromHexString("HELLO THIS IS WRONG STRING"));
        Assertions.assertArrayEquals(expected, ByteArrayUtils.fromHexString(hexString));
        Assertions.assertArrayEquals(expected, ByteArrayUtils.fromHexString(hexStringPartiallyValid));
        Assertions.assertArrayEquals(new byte[]{10}, ByteArrayUtils.fromHexString("A"));
    }

    @Test
    void xorTest() {
        Assertions.assertArrayEquals(
                ByteArrayUtils.fromHexString("AAAA"),
                ByteArrayUtils.xor(
                        ByteArrayUtils.fromHexString("AA00"),
                        ByteArrayUtils.fromHexString("00AA")
                )
        );
        Assertions.assertThrows(IllegalArgumentException.class, () -> ByteArrayUtils.xor(null, null));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.xor(new byte[0],new byte[0]));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.xor(new byte[0],new byte[1]));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.xor(new byte[1],new byte[0]));
        Assertions.assertArrayEquals(
                ByteArrayUtils.fromHexString("AAAA"),
                ByteArrayUtils.xor(
                        ByteArrayUtils.fromHexString("AA00CC"),
                        ByteArrayUtils.fromHexString("00AA")
                )
        );
        Assertions.assertArrayEquals(
                ByteArrayUtils.fromHexString("AAAA"),
                ByteArrayUtils.xor(
                        ByteArrayUtils.fromHexString("AA00"),
                        ByteArrayUtils.fromHexString("00AACC")
                )
        );
    }

    @Test
    void utf16toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtils.utf16toBytes(null));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("31003100"), ByteArrayUtils.utf16toBytes("11"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("00000000"), ByteArrayUtils.utf16toBytes("\0\0"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("3A003A00"), ByteArrayUtils.utf16toBytes("::"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString(
                "480065006C006C006F00200077006F0072006C0064002100"
        ), ByteArrayUtils.utf16toBytes("Hello world!"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("00310031"), ByteArrayUtils.utf16toBytes("11",Endiannes.BIG));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("00000000"), ByteArrayUtils.utf16toBytes("\0\0",Endiannes.BIG));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("003A003A"), ByteArrayUtils.utf16toBytes("::",Endiannes.BIG));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString(
                "00480065006C006C006F00200077006F0072006C00640021"
        ), ByteArrayUtils.utf16toBytes("Hello world!", Endiannes.BIG));

    }

    @Test
    void utf8toBytesTest() {
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtils.utf8toBytes(null));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("3131"), ByteArrayUtils.utf8toBytes("11"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("0000"), ByteArrayUtils.utf8toBytes("\0\0"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("3A3A"), ByteArrayUtils.utf8toBytes("::"));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("48656C6C6F20776F726C6421"), ByteArrayUtils.utf8toBytes("Hello world!"));
    }

    @Test
    void reverseTest() {
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("223344"), ByteArrayUtils.reverse(ByteArrayUtils.fromHexString("443322")));
        byte[] expected = ByteArrayUtils.fromHexString("EFCDAB9078563412");
        byte[] tested = ByteArrayUtils.fromHexString("1234567890ABCDEF");
        Assertions.assertArrayEquals(expected, ByteArrayUtils.reverse(tested));
        Assertions.assertArrayEquals(ByteArrayUtils.fromHexString("00AA"), ByteArrayUtils.reverse(ByteArrayUtils.fromHexString("AA00")));
        Assertions.assertThrows(IllegalArgumentException.class,()-> ByteArrayUtils.reverse(null));
        Assertions.assertArrayEquals(new byte[0], ByteArrayUtils.reverse(new byte[0]));
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(new byte[]{i}, ByteArrayUtils.reverse(new byte[]{i}));
        }
    }

    @Test
    void print() {
    }

    @Test
    void testToString() {
        Assertions.assertEquals("AABBAA",ByteArrayUtils.toString(ByteArrayUtils.fromHexString("AABBAA")));
        Assertions.assertEquals("",ByteArrayUtils.toString(ByteArrayUtils.fromHexString("")));
        Assertions.assertEquals("",ByteArrayUtils.toString(new byte[0]));
        Assertions.assertEquals("01",ByteArrayUtils.toString(new byte[]{1}));
        Assertions.assertEquals("0B",ByteArrayUtils.toString(new byte[]{11}));
        Assertions.assertThrows(IllegalArgumentException.class,()->ByteArrayUtils.toString(null));
    }

    @Test
    void toBytesTest() {
        for (byte i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            Assertions.assertArrayEquals(
                    ByteArrayUtils.fromHexString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtils.fromHexString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i, Endiannes.LITTLE)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtils.fromHexString(Integer.toHexString(i & 0xFF)),
                    inst.toBytes(i, Endiannes.BIG)
            );
        }
        for (short i = Short.MIN_VALUE; i < Short.MAX_VALUE - 1; i += 2) {
            byte[] expected = fromHexStringShortPadded(i);
            byte[] test = inst.toBytes(i);
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected), test
            );
            test = inst.toBytes(i, Endiannes.LITTLE);
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected), test
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
                    ByteArrayUtils.reverse(expected),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected),
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
                    ByteArrayUtils.reverse(expected),
                    inst.toBytes(i)
            );
            Assertions.assertArrayEquals(
                    ByteArrayUtils.reverse(expected),
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
        return ByteArrayUtils.fromHexString(
                String.format("%1$" + Integer.BYTES + "s",
                        Integer.toHexString(i & 0xFFFF)).replace(' ', '0')
        );
    }

    private byte[] fromHexStringIntPadded(int i) {
        return ByteArrayUtils.fromHexString(
                String.format("%1$" + Integer.BYTES * 2 + "s",
                        Integer.toHexString(i)).replace(' ', '0')
        );
    }

    private byte[] fromHexStringLongPadded(long i) {
        return ByteArrayUtils.fromHexString(
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