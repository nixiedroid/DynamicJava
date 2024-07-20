package com.nixiedroid;

import com.nixiedroid.unsafe.UnsafeWrapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/*
Results
ByteSerialisation.testArrayUtilsExistent    avgt   10     31,995 �   0,169  ns/op
ByteSerialisation.testArrayUtilsNew         avgt   10     44,547 �   0,379  ns/op
ByteSerialisation.testArrayUtilsOffset      avgt   10     31,837 �   0,077  ns/op
ByteSerialisation.testUnsafe                avgt   10     30,934 �   0,082  ns/op
ByteSerialisation.testUnsafePutByte         avgt   10     31,210 �   1,090  ns/op
ByteSerialisation.testUnsafePutByteOffset   avgt   10     30,925 �   0,057  ns/op
ByteSerialisation.testUnsafePutByteReverse  avgt   10     31,025 �   0,489  ns/op

 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 10, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 10, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@SuppressWarnings({"unused", "MagicNumber"})
public class ByteSerialisation {

    final static sun.misc.Unsafe U;
    final static long byteArrOffset;
    final static Random random;
    static byte[] array = new byte[8];

    static {
        random = new Random();
        U = UnsafeWrapper.getUnsafe();
        byteArrOffset = U.arrayBaseOffset(byte[].class);
    }


    byte[] array2;


    @Benchmark
    public void testUnsafe(Blackhole blackhole) {
        long i;
        i = random.nextLong();
        U.putLong(array, byteArrOffset, i);
        blackhole.consume(array);

    }

    @Benchmark
    public void testUnsafePutByte(Blackhole blackhole) {
        long i;
        i = random.nextLong();
        U.putByte(array, byteArrOffset, (byte) (i & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 8) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 16) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 24) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 32) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 40) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 48) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 56) & 0xFF));
        blackhole.consume(array);
    }

    @Benchmark
    public void testUnsafePutByteOffset(Blackhole blackhole) {
        long i;
        i = random.nextLong();
        int offset = 7;
        U.putByte(array, offset - byteArrOffset, (byte) (i & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 8) & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 16) & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 24) & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 32) & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 40) & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 48) & 0xFF));
        U.putByte(array, offset - byteArrOffset, (byte) ((i >> 56) & 0xFF));
        blackhole.consume(array);
    }

    @Benchmark
    public void testUnsafePutByteReverse(Blackhole blackhole) {
        long i;
        i = random.nextLong();
        U.putByte(array, byteArrOffset, (byte) ((i >> 56) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 48) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 40) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 32) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 24) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 16) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i >> 8) & 0xFF));
        U.putByte(array, byteArrOffset, (byte) ((i) & 0xFF));
        blackhole.consume(array);
    }

    @Benchmark
    public void testArrayUtilsNew(Blackhole blackhole) {
        long i;
        i = random.nextLong();
        this.array2 = new byte[]{
                (byte) (i & 0xFF),
                (byte) ((i >> 8) & 0xFF),
                (byte) ((i >> 16) & 0xFF),
                (byte) ((i >> 24) & 0xFF),
                (byte) ((i >> 32) & 0xFF),
                (byte) ((i >> 40) & 0xFF),
                (byte) ((i >> 48) & 0xFF),
                (byte) ((i >> 56) & 0xFF)
        };
        blackhole.consume(array);
    }

    @Benchmark
    public void testArrayUtilsExistent(Blackhole blackhole) {
        long i;
        i = random.nextLong();
        array[0] = (byte) (i & 0xFF);
        array[1] = (byte) ((i >> 8) & 0xFF);
        array[2] = (byte) ((i >> 16) & 0xFF);
        array[3] = (byte) ((i >> 24) & 0xFF);
        array[4] = (byte) ((i >> 32) & 0xFF);
        array[5] = (byte) ((i >> 40) & 0xFF);
        array[6] = (byte) ((i >> 48) & 0xFF);
        array[7] = (byte) ((i >> 56) & 0xFF);
        blackhole.consume(array);
    }

    @Benchmark
    public void testArrayUtilsOffset(Blackhole blackhole) {
        long i;
        int offset = 7;
        i = random.nextLong();
        array[offset] = (byte) (i & 0xFF);
        array[offset - 1] = (byte) ((i >> 8) & 0xFF);
        array[offset - 2] = (byte) ((i >> 16) & 0xFF);
        array[offset - 3] = (byte) ((i >> 24) & 0xFF);
        array[offset - 4] = (byte) ((i >> 32) & 0xFF);
        array[offset - 5] = (byte) ((i >> 40) & 0xFF);
        array[offset - 6] = (byte) ((i >> 48) & 0xFF);
        array[offset - 7] = (byte) ((i >> 56) & 0xFF);
        blackhole.consume(array);
    }
}
