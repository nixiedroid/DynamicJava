package com.nixiedroid.bytes;

import org.jetbrains.annotations.Contract;

@SuppressWarnings("unused")

public final class ByteArrays {

    private ByteArrays() {
        throw new Error();
    }

    @Contract(mutates = "param1")
    public static void xor(final byte[] first, final byte[] second) {
        if (first == null || second == null) throw new IllegalArgumentException("Null Array");
        if (first.length != second.length) throw new IllegalArgumentException("Wrong Length");
        final int length = first.length;
        for (int i = 0; i < length; i++) first[i] ^= second[i];
    }

    @Contract(mutates = "param1")
    public static void reverse(final byte[] input) {
        if (input == null) throw new IllegalArgumentException();
        int i = 0, j = input.length - 1;
        byte tmp;
        while (j > i) {
            tmp = input[j];
            input[j] = input[i];
            input[i] = tmp;
            j--;
            i++;
        }
    }

    @Contract(mutates = "param1")
    public static boolean equals(final byte[] first, final byte[] second) {
        if (first == null && second == null) return true;
        if (first == null || second == null) return false;
        if (first == second) return true;
        if (first.length == 0 && second.length == 0) return true;
        if (first.length != second.length) return false;
        for (int i = 0; i < first.length; i++) {
            if (first[i] != second[i]) return false;
        }
        return true;
    }

}
