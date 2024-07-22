package com.nixiedroid;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
Results
ByteSerialisation.testArrayUtilsExistent    avgt   10      6,263 �   0,035  ns/op
ByteSerialisation.testArrayUtilsNew         avgt   10     18,599 �   0,116  ns/op
ByteSerialisation.testArrayUtilsOffset      avgt   10      6,282 �   0,062  ns/op
ByteSerialisation.testUnsafe                avgt   10      1,338 �   0,043  ns/op
ByteSerialisation.testUnsafePutByte         avgt   10      1,337 �   0,012  ns/op
ByteSerialisation.testUnsafePutByteOffset   avgt   10      1,567 �   0,026  ns/op
ByteSerialisation.testUnsafePutByteReverse  avgt   10      1,333 �   0,005  ns/op
ByteSerialisation.testUnsafeReverse         avgt   10      1,331 �   0,005  ns/op
 */

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 10, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 10, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@SuppressWarnings({"unused"})
public class ByteSerialisation {

//    final static sun.misc.Unsafe U;
//    final static long byteArrOffset;
//    final static Random random;
//    static byte[] array = new byte[8];
//
//    static {
//        random = new Random();
//        U = UnsafeWrapper.getUnsafe();
//        byteArrOffset = U.arrayBaseOffset(byte[].class);
//    }
//
//    @State(Scope.Thread)
//    public static class BenchState {
//      public long i= random.nextLong();
//    }
//
//    byte[] array2;
//
//
//    @Benchmark
//    public void testUnsafe(Blackhole blackhole, BenchState state) {
//        U.putLong(array, byteArrOffset, state.i);
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testUnsafeReverse(Blackhole blackhole, BenchState state) {
//        long i = Long.reverseBytes(state.i);
//        U.putLong(array, byteArrOffset, i);
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testUnsafePutByte(Blackhole blackhole, BenchState state) {
//        U.putByte(array, byteArrOffset, (byte) (state.i& 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 8) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 16) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 24) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 32) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 40) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 48) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 56) & 0xFF));
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testUnsafePutByteOffset(Blackhole blackhole, BenchState state) {
//        int offset = 7;
//        U.putByte(array, offset - byteArrOffset, (byte) (state.i& 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 8) & 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 16) & 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 24) & 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 32) & 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 40) & 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 48) & 0xFF));
//        U.putByte(array, offset - byteArrOffset, (byte) ((state.i>> 56) & 0xFF));
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testUnsafePutByteReverse(Blackhole blackhole, BenchState state) {
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 56) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 48) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 40) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 32) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 24) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 16) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i>> 8) & 0xFF));
//        U.putByte(array, byteArrOffset, (byte) ((state.i) & 0xFF));
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testArrayUtilsNew(Blackhole blackhole, BenchState state) {
//        this.array2 = new byte[]{
//                (byte) (state.i& 0xFF),
//                (byte) ((state.i>> 8) & 0xFF),
//                (byte) ((state.i>> 16) & 0xFF),
//                (byte) ((state.i>> 24) & 0xFF),
//                (byte) ((state.i>> 32) & 0xFF),
//                (byte) ((state.i>> 40) & 0xFF),
//                (byte) ((state.i>> 48) & 0xFF),
//                (byte) ((state.i>> 56) & 0xFF)
//        };
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testArrayUtilsExistent(Blackhole blackhole, BenchState state) {
//        array[0] = (byte) (state.i& 0xFF);
//        array[1] = (byte) ((state.i>> 8) & 0xFF);
//        array[2] = (byte) ((state.i>> 16) & 0xFF);
//        array[3] = (byte) ((state.i>> 24) & 0xFF);
//        array[4] = (byte) ((state.i>> 32) & 0xFF);
//        array[5] = (byte) ((state.i>> 40) & 0xFF);
//        array[6] = (byte) ((state.i>> 48) & 0xFF);
//        array[7] = (byte) ((state.i>> 56) & 0xFF);
//        blackhole.consume(array);
//    }
//
//    @Benchmark
//    public void testArrayUtilsOffset(Blackhole blackhole, BenchState state) {
//        int offset = 7;
//        array[offset] = (byte) (state.i& 0xFF);
//        array[offset - 1] = (byte) ((state.i>> 8) & 0xFF);
//        array[offset - 2] = (byte) ((state.i>> 16) & 0xFF);
//        array[offset - 3] = (byte) ((state.i>> 24) & 0xFF);
//        array[offset - 4] = (byte) ((state.i>> 32) & 0xFF);
//        array[offset - 5] = (byte) ((state.i>> 40) & 0xFF);
//        array[offset - 6] = (byte) ((state.i>> 48) & 0xFF);
//        array[offset - 7] = (byte) ((state.i>> 56) & 0xFF);
//        blackhole.consume(array);
//    }
}
