package classes;

import com.nixiedroid.runtime.Info;
import com.nixiedroid.unsafe.UnsafeWrapper;

public class CompressedOOPS {
    public static void main(String[] args) {
        // false 16 16 1 8
        // true 16 16 1 4
        System.out.println(Info.isCompressedOOPS());
        byte[] arr;
        System.out.println(UnsafeWrapper.getUnsafe().arrayBaseOffset(byte[].class));
        System.out.println(UnsafeWrapper.getUnsafe().arrayBaseOffset(Object[].class));
        System.out.println(UnsafeWrapper.getUnsafe().arrayIndexScale(byte[].class));
        System.out.println(UnsafeWrapper.getUnsafe().arrayIndexScale(Object[].class));
        int objBaseOffset = UnsafeWrapper.getUnsafe().arrayBaseOffset(Object[].class);
        Canary c = new Canary(4);
        Object[] objArray = new Object[1];
        objArray[0] = c;
        long address = UnsafeWrapper.getUnsafe().getLong(objArray,objBaseOffset);
        System.out.printf("%08X",address);
        System.out.println(UnsafeWrapper.getUnsafe().getAddress(address));
        System.out.println(UnsafeWrapper.getUnsafe().getLong(address));
        for (int i = -1; i < 10; i++) {
            System.out.printf("%02X",UnsafeWrapper.getUnsafe().getByte(address+i));
        }

    }

    static class Canary{
        final int a;
        public Canary(int a) {
            this.a = a;
        }
    }
}
