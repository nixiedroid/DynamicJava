package com.nixiedroid.function;

public interface ThrowableBiFunction<A,B,R> {
    R apply(A var1,B var2) throws Throwable;

}
