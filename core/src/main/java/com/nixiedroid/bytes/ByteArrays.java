package com.nixiedroid.bytes;

import java.util.Arrays;

@SuppressWarnings("unused")

public final class ByteArrays {

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

    public static boolean equals(byte[] a, int aFromIndex, int aToIndex,
                                 byte[] b, int bFromIndex, int bToIndex) {
        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.length == 0) {
            return b.length == 0;
        }

        int lenA = aToIndex - aFromIndex;
        int lenB = bToIndex - bFromIndex;

        if (lenB == 0) {
            return lenA == 0;
        }

        int result = 0;
        result |= lenA - lenB;

        //This code is from some crypto stuff
        //This method ensures that the time taken to compare the two
        //subarrays does not depend on their contents, preventing timing attacks:
        //TODO: Speed up, by ignoring constant time
        for (int indexA = 0; indexA < lenA; indexA++) {
            int indexB = ((indexA - lenB) >>> 31) * indexA; //If (indexA >= lenB) indexB==0
            result |= a[aFromIndex + indexA] ^ b[bFromIndex + indexB];
        }

        return result == 0;
    }


    public static boolean equals(byte[] a, byte[] b){
        return Arrays.equals(a,b);
    }
}
