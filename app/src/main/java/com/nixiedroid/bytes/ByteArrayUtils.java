package com.nixiedroid.bytes;

import java.io.OutputStream;
import java.math.BigInteger;
import java.util.Locale;

@SuppressWarnings("unused")

public final class ByteArrayUtils {
    public static final  class chunk {
        //TO VALUES
        //SIGNED PART


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
            return ((long) b[0] & 0xff) << 56 | ((long) b[1] & 0xff) << 48 | ((long) b[2] & 0xff) << 40 | ((long) b[3] & 0xff) << 32 | ((long) b[4] & 0xff) << 24 | ((long) b[5] & 0xff) << 16 | ((long) b[6] & 0xff) << 8 | ((long) b[7] & 0xff);
        }

        public static long toInt64L(byte[] b) {
            return ((long) b[7] & 0xff) << 56 | ((long) b[6] & 0xff) << 48 | ((long) b[5] & 0xff) << 40 | ((long) b[4] & 0xff) << 32 | ((long) b[3] & 0xff) << 24 | ((long) b[2] & 0xff) << 16 | ((long) b[1] & 0xff) << 8 | ((long) b[0] & 0xff);
        }


        public static int toUInt16B(byte[] b) {
            return (short) ((b[1] & 0xFF) | (b[0] & 0xFF) << 8);
        }

        public static int toUInt16L(byte[] b) {
            return (short) ((b[0] & 0xFF) | (b[1] & 0xFF) << 8);
        }

        public static long toUInt32B(byte[] b) {
            return (b[3] & 0xFF) | (b[2] & 0xFF) << 8 | (b[1] & 0xFF) << 16 | (b[0] & 0xFF) << 24;
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
    public static final  class ranged {

        public static int toUInt8(byte[] b, int start){
            return (b[start] & 0xff);
        }
        public static short toInt16B(byte[] b, int start) {
            return (short) ((b[start+1] & 0xFF) | (b[start] & 0xFF) << 8);
        }
        public static short toInt16L(byte[] b,int start) {
            return (short) ((b[start] & 0xFF) | (b[start+1] & 0xFF) << 8);
        }
        public static int toInt32B(byte[] b,int start) {
            return (b[start+3] & 0xFF) | (b[start+2] & 0xFF) << 8 | (b[start+1] & 0xFF) << 16 | (b[start] & 0xFF) << 24;
        }

        public static int toInt32L(byte[] b,int start) {
            return (b[start] & 0xFF) | (b[start+1] & 0xFF) << 8 | (b[start+2] & 0xFF) << 16 | (b[start+3] & 0xFF) << 24;
        }

        public static long toInt64B(byte[] b,int start) {
            return ((long) b[start] & 0xff) << 56 | ((long) b[start+1] & 0xff) << 48 | ((long) b[start+2] & 0xff) << 40 | ((long) b[start+3] & 0xff) << 32 | ((long) b[start+4] & 0xff) << 24 | ((long) b[start+5] & 0xff) << 16 | ((long) b[start+6] & 0xff) << 8 | ((long) b[start+7] & 0xff);
        }

        public static long toInt64L(byte[] b,int start) {
            return ((long) b[start+7] & 0xff) << 56 | ((long) b[start+6] & 0xff) << 48 | ((long) b[start+5] & 0xff) << 40 | ((long) b[start+4] & 0xff) << 32 | ((long) b[start+3] & 0xff) << 24 | ((long) b[start+2] & 0xff) << 16 | ((long) b[start+1] & 0xff) << 8 | ((long) b[start] & 0xff);
        }


        public static int toUInt16B(byte[] b,int start) {
            return (short) ((b[start+ 1 ] & 0xFF) | (b[start] & 0xFF) << 8);
        }

        public static int toUInt16L(byte[] b,int start) {
            return (short) ((b[start] & 0xFF) | (b[start+1] & 0xFF) << 8);
        }

        public static long toUInt32B(byte[] b,int start) {
            return (b[start+3] & 0xFF) | (b[start+2] & 0xFF) << 8 | (b[start+1] & 0xFF) << 16 | (long) (b[start] & 0xFF) << 24;
        }

        public static long toUInt32L(byte[] b,int start) {
            return (b[start] & 0xFF) | (b[start+1] & 0xFF) << 8 | (b[start+2] & 0xFF) << 16 | (long) (b[start + 3] & 0xFF) << 24;
        }

        public static BigInteger toUInt64L(byte[] b,int start) {
            byte[] chunk = new byte[16];
            System.arraycopy(b,start,chunk,0,16);
            return new BigInteger(reverse(chunk));
        }

        public static BigInteger toUInt64B(byte[] b,int start) {
            byte[] chunk = new byte[16];
            System.arraycopy(b,start,chunk,0,16);
            return new BigInteger(chunk);
        }
    }
    public static final class fast{
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
    private static ByteArrayUtils INSTANCE = new ByteArrayUtils();
    private ByteArrayUtils(){}
    public static ByteArrayUtils i() {return INSTANCE;}

    public <T extends Number> T fromBytes(final byte[] bytes,final  Endiannes endiannes){
        if (bytes == null) throw new IllegalArgumentException("Input array is null");
        int count = bytes.length -1;
        long out = 0;
        if (count >= Long.BYTES) throw new IllegalArgumentException();
        switch (endiannes){
            case LITTLE -> {
                for (int i = 0; i <= count; i++) {
                    out |= ((long)bytes[i]&0xFF) << (i <<3);
                }
            }
            case BIG -> {
                for (int i = 0; i <= count; i++) {
                    out |= ((long)bytes[i]&0xFF) << (count - i <<3);
                }
            }
        }
        switch (bytes.length){
            case 1  -> {
                return (T) Byte.valueOf((byte)out);
            }
            case 2 -> {
                return (T) Short.valueOf((short)out);
            }
            case 4  -> {
                return (T) Integer.valueOf((int)out);
            }
            default -> {
                return (T) Long.valueOf(out);
            }
        }
    }

    public <T extends Number> byte[] toBytes(T object, Endiannes endiannes) {
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
        byte[] out = new byte[count+1];
        switch (endiannes) {
            case BIG -> {
                for (int i = 0; i <= count; i++) {
                    out[i] = (byte) ((object.longValue() >> ((count - i) << 3)) & 0xFF);
                }
            }
            case LITTLE -> {
                for (int i = 0; i <= count; i++) {
                    out[i] = (byte) ((object.longValue() >> (i << 3)) & 0xFF);
                }
            }
        }
        return out;
    }

    public static byte[] fromHexString(final String encoded) {
        if (encoded==null) return null;
        if ((encoded.length() % 2) != 0) throw new IllegalArgumentException("Input string must contain an even number of characters");
        final byte[] result = new byte[encoded.length() / 2];
        final char[] enc = encoded.toCharArray();
        for (int i = 0; i < enc.length; i += 2) {
            result[i / 2] = (byte) Integer.parseInt(String.valueOf(enc[i]) + enc[i + 1], 16);
            //CAtch number format exception
        }
        return result;
    }
    public static byte[] xor(byte[] first, byte[] second){
        if (first.length != second.length) throw new IllegalArgumentException("Arrays must have equal length");
        byte[] out = new byte[first.length];
        for (int i = 0; i< first.length;i++)
            out[i] = (byte) (first[i] ^ second[i]);
        return out;
    }
    public static byte[] UTF16LEtoBytes(String str) {
        final int length = str.length();
        final char[] buffer = new char[length];
        str.getChars(0, length, buffer, 0);
        final byte[] b = new byte[length*2];
        for (int j = 0; j < length; j++) {
            b[j*2] = (byte) (buffer[j] & 0xFF);
            b[j*2+1] = (byte) (buffer[j] >> 8);
        }
        return b;
    }
    public static byte[] UTF8toBytes(String str) {
        final int length = str.length();
        final char[] buffer = new char[length];
        final byte[] b = new byte[length];
        str.getChars(0, length, buffer, 0);
        for (int j = 0; j < length; j++) {
            b[j] = (byte) (buffer[j] & 0xFF);
        }
        return b;
    }
    public static byte[] reverse(byte[] esrever) {
        int i = 0;
        int j = esrever.length - 1;
        byte tmp;
        while (j > i) {
            tmp = esrever[j];
            esrever[j] = esrever[i];
            esrever[i] = tmp;
            j--;
            i++;
        }
        return esrever;
    }
    @Deprecated
    public static void print(byte[] data) {
        System.out.print("Hex value: ");
        for (byte b : data) {
            System.out.printf("%02X", b & 0xFF);
        }
        System.out.println();
    }
    public static String toString(byte[] data) {
        StringBuilder out = new StringBuilder();
        for (byte b : data) {
            out.append(String.format("%02X", b & 0xFF));
        }
        return out.toString();
    }
}
