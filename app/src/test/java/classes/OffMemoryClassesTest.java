package classes;

import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

public class OffMemoryClassesTest {
    Object inst;
    @Test
    void TestClassLocation() throws NoSuchFieldException {
        Canary canaryClass = new Canary();
        Canary canaryClass2 = new Canary();
        sun.misc.Unsafe U = UnsafeWrapper.getUnsafe();
        Field f = this.getClass().getDeclaredField("inst");
        long offset =   U.objectFieldOffset(f);
        System.out.println(offset);
        System.out.printf( "%016X",U.getLong(this,offset));

        for (int i = 0; i < 50; i++) {
            System.out.printf("%02X", U.getByte(this, offset -i) & 0xFF);
        }
        System.out.println();
        for (int i = 0; i < 50; i++) {
            System.out.printf("%02X", U.getByte(canaryClass, i) & 0xFF);
        }
        System.out.println("");
        for (int i = 0; i < 50; i++) {
            System.out.printf("%02X", U.getByte(canaryClass2, i) & 0xFF);
        }
    }

    static class Canary {
        static int b = 3;
        int a = 5;
    }
}
