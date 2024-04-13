package com.nixiedroid.bytes;

import com.nixiedroid.exceptions.Thrower;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")

public final class ByteArrayUtils {
    private static final ByteArrayUtils INSTANCE = new ByteArrayUtils();
    private static final int HEX = 16;
    private static final Pattern hexString = Pattern.compile("[0-9a-fA-F]+");

    private ByteArrayUtils() {
    }

    public static ByteArrayUtils i() {
        return INSTANCE;
    }

    public static byte[] fromHexString(final String encoded) {
        if (encoded == null || encoded.isBlank()) return new byte[0];
        Matcher matcher = hexString.matcher(encoded);
        if (!matcher.matches()) return new byte[0];
        int length = encoded.length();
        if ((length % 2) != 0) length--;
        if (length == 0) {
            try {
                return new byte[]{(byte) Integer.parseInt(encoded, HEX)};
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        }
        final byte[] result = new byte[length >> 1];
        final char[] chars = encoded.toCharArray();
        for (int i = 0; i < length; i += 2) {
            try {
                result[i >> 1] = (byte) Integer.parseInt(String.valueOf(chars[i]) + chars[i + 1], HEX);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(e);
            }
        }
        return result;
    }

    public static byte[] xor(final byte[] first, final byte[] second) {
        if (first == null || second == null) throw new IllegalArgumentException("Null Array");
        int length = Math.min(first.length, second.length);
        byte[] out = new byte[length];
        for (int i = 0; i < length; i++)
            out[i] = (byte) (first[i] ^ second[i]);
        return out;
    }
    public static byte[] utf16toBytes(final String str) {
        return utf16toBytes(str,Endiannes.LITTLE);
    }

    public static byte[] utf16toBytes(final String str, Endiannes endiannes) {
        if (str == null) throw new IllegalArgumentException();
        final int length = str.length();
        final char[] buffer = new char[length];
        str.getChars(0, length, buffer, 0);
        final byte[] b = new byte[length * 2];
        for (int j = 0; j < length; j++) {
            switch (endiannes) {
                case LITTLE:
                    b[j * 2] = (byte) (buffer[j] & 0xFF);
                    b[j * 2 + 1] = (byte) (buffer[j] >> 8);
                    break;
                case BIG:
                    b[j * 2 + 1] = (byte) (buffer[j] & 0xFF);
                    b[j * 2] = (byte) (buffer[j] >> 8);
                    break;
            }
        }
        return b;
    }

    public static byte[] utf8toBytes(final String str) { //ASCII TABLE
        if (str == null) throw new IllegalArgumentException();
        final int length = str.length();
        final char[] buffer = new char[length];
        final byte[] b = new byte[length];
        str.getChars(0, length, buffer, 0);
        for (int j = 0; j < length; j++) {
            b[j] = (byte) (buffer[j] & 0xFF);
        }
        return b;
    }
    public static String StringFromBytes(byte[] bytes) { //ASCII TABLE
       return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] reverse(final byte[] input) {
        if (input == null) throw new IllegalArgumentException();
        byte[] out = new byte[input.length];
        System.arraycopy(input, 0, out, 0, out.length);
        int i = 0, j = out.length - 1;
        byte tmp;
        while (j > i) {
            tmp = out[j];
            out[j] = out[i];
            out[i] = tmp;
            j--;
            i++;
        }
        return out;
    }

    @Deprecated
    public static void print(byte[] data) {
        System.out.print("Hex value: ");
        for (byte b : data) {
            System.out.printf("%02X", b & 0xFF);
        }
        System.out.println();
    }

    public static String toString(final byte[] data) {
        if (data == null) throw new IllegalArgumentException();
        StringJoiner out = new StringJoiner("");
        for (byte b : data) {
            out.add(String.format("%02X", b & 0xFF));
        }
        return out.toString();
    }

    public static boolean isEquals(final byte[] first, final byte[] second) {
        return Arrays.equals(first, second);
//        if (first == second) return true;
//        if (first == null || second == null) return false;
//        if (first.length == 0 && second.length == 0) return true;
//        if (first.length != second.length) return false;
//        for (int i = 0; i < first.length; i++) {
//            if (first[i] != second[i]) return false;
//        }
//        return true;
    }

    public <T extends Number> T fromBytes(final byte[] bytes) {
        return fromBytes(bytes, Endiannes.LITTLE);
    }

    @SuppressWarnings("unchecked")
    public <T extends Number> T fromBytes(final byte[] bytes, final Endiannes endiannes) {
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        int count = bytes.length - 1;
        long out = 0;
        if (count >= Long.BYTES) throw new IllegalArgumentException();
        switch (endiannes) {
            case LITTLE:
                for (int i = 0; i <= count; i++) {
                    out |= ((long) bytes[i] & 0xFF) << (i << 3);
                }
                break;
            case BIG:
                for (int i = 0; i <= count; i++) {
                    out |= ((long) bytes[i] & 0xFF) << (count - i << 3);
                }
                break;
        }
        switch (bytes.length) {
            case 1:
                return (T) Byte.valueOf((byte) out);
            case 2:
                return (T) Short.valueOf((short) out);
            case 4:
                return (T) Integer.valueOf((int) out);
            default:
                return (T) Long.valueOf(out);
        }
    }

    public <T extends Number> byte[] toBytes(final T object, final Endiannes endiannes) {
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
        switch (endiannes) {
            case BIG:
                for (int i = 0; i <= count; i++) {
                    out[i] = (byte) ((object.longValue() >> ((count - i) << 3)) & 0xFF);
                }
                break;
            case LITTLE:
                for (int i = 0; i <= count; i++) {
                    out[i] = (byte) ((object.longValue() >> (i << 3)) & 0xFF);
                }
                break;
        }
        return out;
    }

    public <T extends Number> byte[] toBytes(final T object) {
        return toBytes(object, Endiannes.LITTLE);
    }

    public static final class chunk {
        //TO VALUES
        //SIGNED PART
        private chunk() {
            Thrower.throwExceptionAndDie("Utility class");
        }

        public static short toInt16B(byte[] b) {
            return (short) ((b[1] & 0xFF) | (b[0] & 0xFF) << 8);
        }

        public static short toInt16L(byte[] b) {
            return (short) ((b[0] & 0xFF) | (b[1] & 0xFF) << 8);
        }

        public static int toInt32B(byte[] b) {
            return (b[3] & 0xFF) | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
        }

        public static int toInt32L(byte[] b) {
            return (b[0] & 0xFF) | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (b[3] & 0xFF) << 24;
        }

        public static long toInt64B(byte[] b) {
            return ((long) b[0] & 0xFF) << 56 | ((long) b[1] & 0xFF) << 48 | ((long) b[2] & 0xFF) << 40 | ((long) b[3] & 0xFF) << 32 | ((long) b[4] & 0xFF) << 24 | ((long) b[5] & 0xFF) << 16 | ((long) b[6] & 0xFF) << 8 | ((long) b[7] & 0xFF);
        }

        public static long toInt64L(byte[] b) {
            return ((long) b[7] & 0xFF) << 56 | ((long) b[6] & 0xFF) << 48 | ((long) b[5] & 0xFF) << 40 | ((long) b[4] & 0xFF) << 32 | ((long) b[3] & 0xFF) << 24 | ((long) b[2] & 0xFF) << 16 | ((long) b[1] & 0xFF) << 8 | ((long) b[0] & 0xFF);
        }


        public static int toUInt16B(byte[] b) {
            return (short) ((b[1] & 0xFF) | (b[0] & 0xFF) << 8);
        }

        public static int toUInt16L(byte[] b) {
            return (short) ((b[0] & 0xFF) | (b[1] & 0xFF) << 8);
        }

        public static long toUInt32B(byte[] b) {
            return (b[3] & 0xFF) | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (long) (b[0] & 0xFF) << 24;
        }

        public static long toUInt32L(byte[] b) {
            return (b[0] & 0xFF) | (b[1] & 0xFF) << 8 | (b[2] & 0xFF) << 16 | (long) (b[3] & 0xFF) << 24;
        }

        public static BigInteger toUInt64L(byte[] b) {
            return new BigInteger(reverse(b));
        }

        public static BigInteger toUInt64B(byte[] b) {
            return new BigInteger(b);
        }
    }

    public static final class ranged {
        private ranged() {
            Thrower.throwExceptionAndDie("Utility class");
        }

        public static int toUInt8(byte[] b, int start) {
            return (b[start] & 0xFF);
        }

        public static short toInt16B(byte[] b, int start) {
            return (short) ((b[start + 1] & 0xFF) | (b[start] & 0xFF) << 8);
        }

        public static short toInt16L(byte[] b, int start) {
            return (short) ((b[start] & 0xFF) | (b[start + 1] & 0xFF) << 8);
        }

        public static int toInt32B(byte[] b, int start) {
            return (b[start + 3] & 0xFF) | (b[start + 2] & 0xFF) << 8 | (b[start + 1] & 0xFF) << 16 | (b[start] & 0xFF) << 24;
        }

        public static int toInt32L(byte[] b, int start) {
            return (b[start] & 0xFF) | (b[start + 1] & 0xFF) << 8 | (b[start + 2] & 0xFF) << 16 | (b[start + 3] & 0xFF) << 24;
        }

        public static long toInt64B(byte[] b, int start) {
            return ((long) b[start] & 0xFF) << 56 | ((long) b[start + 1] & 0xFF) << 48 | ((long) b[start + 2] & 0xFF) << 40 | ((long) b[start + 3] & 0xFF) << 32 | ((long) b[start + 4] & 0xFF) << 24 | ((long) b[start + 5] & 0xFF) << 16 | ((long) b[start + 6] & 0xFF) << 8 | ((long) b[start + 7] & 0xFF);
        }

        public static long toInt64L(byte[] b, int start) {
            return ((long) b[start + 7] & 0xFF) << 56 | ((long) b[start + 6] & 0xFF) << 48 | ((long) b[start + 5] & 0xFF) << 40 | ((long) b[start + 4] & 0xFF) << 32 | ((long) b[start + 3] & 0xFF) << 24 | ((long) b[start + 2] & 0xFF) << 16 | ((long) b[start + 1] & 0xFF) << 8 | ((long) b[start] & 0xFF);
        }


        public static int toUInt16B(byte[] b, int start) {
            return (short) ((b[start + 1] & 0xFF) | (b[start] & 0xFF) << 8);
        }

        public static int toUInt16L(byte[] b, int start) {
            return (short) ((b[start] & 0xFF) | (b[start + 1] & 0xFF) << 8);
        }

        public static long toUInt32B(byte[] b, int start) {
            return (b[start + 3] & 0xFF) | (b[start + 2] & 0xFF) << 8 | (b[start + 1] & 0xFF) << 16 | (long) (b[start] & 0xFF) << 24;
        }

        public static long toUInt32L(byte[] b, int start) {
            return (b[start] & 0xFF) | (b[start + 1] & 0xFF) << 8 | (b[start + 2] & 0xFF) << 16 | (long) (b[start + 3] & 0xFF) << 24;
        }

        public static BigInteger toUInt64L(byte[] b, int start) {
            byte[] chunk = new byte[16];
            System.arraycopy(b, start, chunk, 0, 16);
            return new BigInteger(reverse(chunk));
        }

        public static BigInteger toUInt64B(byte[] b, int start) {
            byte[] chunk = new byte[16];
            System.arraycopy(b, start, chunk, 0, 16);
            return new BigInteger(chunk);
        }
    }

    public static final class fast {
        private fast() {
            Thrower.throwExceptionAndDie("Utility class");
        }

        public static byte[] int16ToBytesB(short s) {
            return new byte[]{(byte) ((s >> 8) & 0xFF), (byte) (s & 0xFF)};
        }

        public static byte[] int16ToBytesL(short s) {
            return new byte[]{(byte) (s & 0xFF), (byte) ((s >> 8) & 0xFF)};
        }

        public static byte[] int32ToBytesB(int i) {
            return new byte[]{(byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) (i & 0xFF)};
        }

        public static byte[] int32ToBytesL(int i) {
            return new byte[]{(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 24) & 0xFF)};
        }

        public static byte[] int64ToBytesB(long l) {
            return new byte[]{(byte) ((l >> 56) & 0xFF), (byte) ((l >> 48) & 0xFF), (byte) ((l >> 40) & 0xFF), (byte) ((l >> 32) & 0xFF), (byte) ((l >> 24) & 0xFF), (byte) ((l >> 16) & 0xFF), (byte) ((l >> 8) & 0xFF), (byte) (l & 0xFF)};
        }

        public static byte[] int64ToBytesL(long l) {
            return new byte[]{(byte) (l & 0xFF), (byte) ((l >> 8) & 0xFF), (byte) ((l >> 16) & 0xFF), (byte) ((l >> 24) & 0xFF), (byte) ((l >> 32) & 0xFF), (byte) ((l >> 40) & 0xFF), (byte) ((l >> 48) & 0xFF), (byte) ((l >> 56) & 0xFF)};
        }

        //UNSIGNED PART
        //UByte stored in int (uInt16)
        //UShort stored in int (uInt32)
        //UInt aka ULong stored in long (uInt64)
        //ULongLong stored in BigInteger
        public static byte uByteToByte(int s) {
            return (byte) (s & 0xFF);
        }

        public static byte[] uInt16ToBytesB(int s) {
            return new byte[]{(byte) ((s >> 8) & 0xFF), (byte) (s & 0xFF)};
        }

        public static byte[] uInt16ToBytesL(int s) {
            return new byte[]{(byte) (s & 0xFF), (byte) ((s >> 8) & 0xFF)};
        }

        public static byte[] uInt32ToBytesB(long i) {
            return new byte[]{(byte) ((i >> 24) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) (i & 0xFF)};
        }

        public static byte[] uInt32ToBytesL(long i) {
            return new byte[]{(byte) (i & 0xFF), (byte) ((i >> 8) & 0xFF), (byte) ((i >> 16) & 0xFF), (byte) ((i >> 24) & 0xFF)};
        }

        public static byte[] uInt64ToBytesL(BigInteger l) {
            return reverse(l.toByteArray());
        }

        public static byte[] uInt64ToBytesB(BigInteger l) {
            return l.toByteArray();
        }
    }
}
