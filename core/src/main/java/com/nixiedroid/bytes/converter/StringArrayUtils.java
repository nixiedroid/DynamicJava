package com.nixiedroid.bytes.converter;

import java.nio.charset.StandardCharsets;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class StringArrayUtils {

    private static final Pattern hexString = Pattern.compile("[0-9a-fA-F]+");

    private static final int HEX = 16;
    private static final int FF = 0xFF;

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

    public static byte[] utf16toBytes(final String str) {
        return utf16toBytes(str, Endianness.LITTLE_ENDIAN);
    }

    public static byte[] utf16toBytes(final String str, Endianness endianness) {
        if (str == null) throw new IllegalArgumentException();
        final int length = str.length();
        final char[] buffer = new char[length];
        str.getChars(0, length, buffer, 0);
        final byte[] b = new byte[length * 2];
        for (int j = 0; j < length; j++) {
            switch (endianness) {
                case LITTLE_ENDIAN:
                    b[j * 2] = (byte) (buffer[j] & FF);
                    b[j * 2 + 1] = (byte) (buffer[j] >> 8);
                    break;
                case BIG_ENDIAN:
                    b[j * 2 + 1] = (byte) (buffer[j] & FF);
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
            b[j] = (byte) (buffer[j] & FF);
        }
        return b;
    }


    public static String StringFromBytes(byte[] bytes) { //ASCII TABLE
        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static String toString(final byte[] data) {
        if (data == null) throw new IllegalArgumentException();
        StringJoiner out = new StringJoiner("");
        for (byte b : data) {
            out.add(String.format("%02X", b & FF));
        }
        return out.toString();
    }
}
