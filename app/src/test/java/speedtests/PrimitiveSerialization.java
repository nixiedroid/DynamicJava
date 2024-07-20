package speedtests;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PrimitiveSerialization {

    @Test
    void quickByteArrayFill(){
        final long dummyLong = 0x1234_5678_8765_4321L;

        sun.misc.Unsafe U = UnsafeWrapper.getUnsafe();
        byte[] array = new byte[8];
        long byteArrOffset = U.arrayBaseOffset(byte[].class);
        System.out.println(ByteArrayUtils.toString(array));

        U.putLong(array,byteArrOffset, dummyLong);

        System.out.println(ByteArrayUtils.toString(array));

        byte[] expected = ByteArrayUtils.fast.int64ToBytesL(dummyLong);

        Assertions.assertArrayEquals(expected, array);
    }
}
