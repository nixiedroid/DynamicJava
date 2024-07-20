package basics;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

public class SortingAlgorithmsTest {


    private static int[] generateRandomArray(int size, int bound) {
        Random random = new Random();
        int[] array = new int[size];

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(bound);
        }

        return array;
    }

    @Test
    void bubbleSort() {
        int[] array = generateRandomArray(30, 100);
        int[] bSorted = bSort(array);
        Arrays.sort(array);
        Assertions.assertArrayEquals(bSorted, array);
    }

    int[] bSort(int[] a) {
        final int length = a.length;
        int[] out = new int[length];
        System.arraycopy(a,0,out,0,length);
        for (int i = 0; i < length - 1; i++) {
            for (int j = i; j < length; j++) {
                if (out[i] > out[j]) {
                    out[i] ^= out[j];
                    out[j] ^= out[i];
                    out[i] ^= out[j];
                }
            }
        }
        return out;
    }
}
