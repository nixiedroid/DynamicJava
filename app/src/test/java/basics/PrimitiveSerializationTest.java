package basics;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitiveSerializationTest {

    @Test
    void quickByteArrayFill() {
        final long dummyLong = 0x1234_5678_8765_4321L;
        final long shortLong = 0x1234;
        final int dummyInt = 8765_4321;
        final long ZERO = 0;

        sun.misc.Unsafe U = UnsafeWrapper.getUnsafe();
        byte[] array = new byte[8], expected;
        long byteArrOffset = U.arrayBaseOffset(byte[].class);

        U.putLong(array, byteArrOffset, dummyLong);
        expected = ByteArrayUtils.fast.int64ToBytesL(dummyLong);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array,byteArrOffset,ZERO);
        expected = ByteArrayUtils.fast.int64ToBytesL(0);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, shortLong);
        expected = ByteArrayUtils.fast.int64ToBytesL(shortLong);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, dummyInt);
        expected = ByteArrayUtils.fast.int64ToBytesL(dummyInt);
        Assertions.assertArrayEquals(expected, array);

    }
}
