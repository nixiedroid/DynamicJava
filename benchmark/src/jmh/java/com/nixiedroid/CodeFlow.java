package com.nixiedroid;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@State(Scope.Benchmark)
@Fork(value = 1)
@Warmup(iterations = 10, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@Measurement(iterations = 10, timeUnit = TimeUnit.MILLISECONDS, time = 5000)
@SuppressWarnings({"unused"})
public class CodeFlow {

    final static Random random = new Random();

    @Benchmark
    public void if2IntBenchmark(BenchState state, Blackhole bh) {
        if (state.twoo == 0) {
            bh.consume(state.dummy);
        } else {
            bh.consume(state.dummy);
        }
    }

    @Benchmark
    public void if3IntBenchmark(BenchState state, Blackhole bh) {
        if (state.tree == 0) {
            bh.consume(state.dummy);
        } else if (state.tree == 1) {
            bh.consume(state.dummy);
        } else {
            bh.consume(state.dummy);
        }
    }

    @Benchmark
    public void if2EnumBenchmark(BenchState state, Blackhole bh) {
        if (state.val2 == TwooEnum.A) {
            bh.consume(state.dummy);
        } else {
            bh.consume(state.dummy);
        }
    }

    @Benchmark
    public void if3EnumBenchmark(BenchState state, Blackhole bh) {
        if (state.val3 == TriEnum.A) {
            bh.consume(state.dummy);
        } else if (state.val3 == TriEnum.B) {
            bh.consume(state.dummy);
        } else if (state.val3 == TriEnum.C) {
            bh.consume(state.dummy);
        }
    }

    @Benchmark
    public void sw2IntBenchmark(BenchState state, Blackhole bh) {
        switch (state.twoo){
            case 0:
                bh.consume(state.dummy);
                break;
            case 1:
                bh.consume(state.dummy);
                break;
        }
    }

    @Benchmark
    public void sw3IntBenchmark(BenchState state, Blackhole bh) {
        switch (state.tree){
            case 0:
                bh.consume(state.dummy);
                break;
            case 1:
                bh.consume(state.dummy);
                break;
            case 2:
                bh.consume(state.dummy);
                break;
            default:
                bh.consume(state.dummy);
                break;
        }
    }

    @Benchmark
    public void sw2EnumBenchmark(BenchState state, Blackhole bh) {
        switch (state.val2){
            case TwooEnum.A:
                bh.consume(state.dummy);
                break;
            case TwooEnum.B:
                bh.consume(state.dummy);
                break;
            default:
                bh.consume(state.dummy);
                break;
        }
    }

    @Benchmark
    public void sw3EnumBenchmark(BenchState state, Blackhole bh) {
        switch (state.val3){
            case TriEnum.A:
                bh.consume(state.dummy);
                break;
            case TriEnum.B:
                bh.consume(state.dummy);
                break;
            case TriEnum.C:
                bh.consume(state.dummy);
                break;
            default:
                bh.consume(state.dummy);
                break;
        }
    }


    enum TriEnum {
        A, B, C
    }


    enum TwooEnum {
        A, B
    }

    @State(Scope.Thread)
    public static class BenchState {
        public int dummy = random.nextInt();
        public int twoo = random.nextInt(2);
        public int tree = random.nextInt(3);
        public TriEnum val3 = TriEnum.values()[this.tree];
        public TwooEnum val2 = TwooEnum.values()[this.twoo];
    }
}
