package basics;

import com.nixiedroid.bytes.ByteArrayConverter;
import com.nixiedroid.bytes.ByteArrayConverterDefault;
import com.nixiedroid.bytes.Endiannes;
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
        converter.fromLong(expected,0, dummyLong, Endiannes.LITTLE);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array,byteArrOffset,ZERO);
        converter.fromLong(expected,0, 0,Endiannes.LITTLE);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, shortLong);
        converter.fromLong(expected,0, shortLong,Endiannes.LITTLE);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, dummyInt);
        converter.fromLong(expected,0, dummyInt,Endiannes.LITTLE);
        Assertions.assertArrayEquals(expected, array);

    }
}
