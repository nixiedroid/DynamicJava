package basics;

import com.nixiedroid.bytes.converter.ByteArrayConverter;
import com.nixiedroid.bytes.converter.ByteArrayConverterDefault;
import com.nixiedroid.bytes.converter.Endianness;
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
        converter.writeLong(expected,0, dummyLong, Endianness.LITTLE_ENDIAN);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array,byteArrOffset,ZERO);
        converter.writeLong(expected,0, 0, Endianness.LITTLE_ENDIAN);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, shortLong);
        converter.writeLong(expected,0, shortLong, Endianness.LITTLE_ENDIAN);
        Assertions.assertArrayEquals(expected, array);

        U.putLong(array, byteArrOffset, dummyInt);
        converter.writeLong(expected,0, dummyInt, Endianness.LITTLE_ENDIAN);
        Assertions.assertArrayEquals(expected, array);

    }
}
