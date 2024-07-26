package com.nixiedroid.bytes.converter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@SuppressWarnings("unused")

public final class ByteArrays {

    private static final int BUF_SIZE = 1024;

    private ByteArrays() {
        throw new Error();
    }

    public static void xor(final byte[] target, final byte[] mask) {
        if (target == null || mask == null) throw new IllegalArgumentException("Null Array");
        if (target.length != mask.length) throw new IllegalArgumentException("Wrong Length");
        for (int i = 0; i < target.length; i++) target[i] ^= mask[i];
    }

    public static void reverse(final byte[] input) {
        if (input == null) throw new IllegalArgumentException();
        int m = 0, k = input.length - 1;
        while (k > m) {
            input[k] ^= input[m]; //
            input[m] ^= input[k]; // Swap bytes
            input[k] ^= input[m]; //
            k--; m++;
        }
    }

    private static void rangeCheck(int arrayLength, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        if (fromIndex < 0) throw new ArrayIndexOutOfBoundsException(fromIndex);
        if (toIndex > arrayLength) throw new ArrayIndexOutOfBoundsException(toIndex);
    }

    public static boolean equals(
            byte[] a, int aFromIndex, int aToIndex,
            byte[] b, int bFromIndex, int bToIndex
    ) {
        if (a == b) return true;
        if (a == null || b == null) return false;

        rangeCheck(a.length, aFromIndex, aToIndex);
        rangeCheck(b.length, bFromIndex, bToIndex);

        int aLength = aToIndex - aFromIndex;
        int bLength = bToIndex - bFromIndex;
        if (aLength != bLength) return false;

        for (int index = 0; index < aLength; index++) {
            if (a[aFromIndex + index] != b[bFromIndex + index]) return false;
        }
        return true;
    }


    public static byte[] toByteArray(InputStream inputStream) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[BUF_SIZE];
            int bytesRead = 0;
            while (-1 != (bytesRead = inputStream.read(buffer))) {
                outputStream.write(buffer, 0, bytesRead);
            }
            return outputStream.toByteArray();
        }
    }

    public static boolean equals(byte[] a, byte[] b){
        return Arrays.equals(a,b);
    }
}
