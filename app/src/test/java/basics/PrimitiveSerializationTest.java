package basics;

import com.nixiedroid.bytes.ByteArrayConverter;
import com.nixiedroid.bytes.ByteArrayConverterDefault;
import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitiveSerializationTest {

    @Test
    void quickByteArrayFill() {
        ByteArrayConverter converter = new ByteArrayConverterDefault();
        final long dummyLong = 0x1234_5678_8765_4321L;
        final long shortLong = 0x1234;
        final int dummyInt = 8765_4321;
        final long ZERO = 0;

        sun.misc.Unsafe U = UnsafeWrapper.getUnsafe();
        byte[] array = new byte[8], expected = new byte[8];
        long byteArrOffset = U.arrayBaseOffset(byte[].class);

        U.putLong(array, byteArrOffset, dummyLong);
        converter.int64ToBytesL(expected,0, dummyLong);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array,byteArrOffset,ZERO);
        converter.int64ToBytesL(expected,0, 0);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, shortLong);
        converter.int64ToBytesL(expected,0, shortLong);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, dummyInt);
        converter.int64ToBytesL(expected,0, dummyInt);
        Assertions.assertArrayEquals(expected, array);

    }
}
