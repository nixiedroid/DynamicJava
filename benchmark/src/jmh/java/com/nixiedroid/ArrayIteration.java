package com.nixiedroid;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

/*
ArrayIteration.testCompare                  avgt    5  15888,026 ±  278,108  ns/op
ArrayIteration.testException                avgt    5  13808,498 ± 3431,745  ns/op
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 5, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 5, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@SuppressWarnings("unused")
public class ArrayIteration {
//    static final int SIZE = 10000;
//    volatile static int[] array = new int[SIZE];
//
//    static {
//        Random r = new Random();
//        for (int i = 0; i < array.length; i++) {
//            array[i] = r.nextInt();
//        }
//    }
//
//
//    @Benchmark
//    public void testCompare(Blackhole blackhole) {
//        int i = 0;
//        for (; i < array.length; i++) {
//            blackhole.consume(array[i]);
//        }
//        blackhole.consume(i);
//    }
//
//    @Benchmark
//    public void testException(Blackhole blackhole) {
//        int i = 0;
//        for (; ; i++) {
//            try {
//                blackhole.consume(array[i]);
//            } catch (ArrayIndexOutOfBoundsException e) {
//                break;
//            }
//        }
//        blackhole.consume(i);
//    }
}
