package com.nixiedroid.interfaces;

import java.util.Objects;
import java.util.function.Function;

public interface ThrowableBiFunction<A,B,R> {
    R apply(A var1,B var2) throws Throwable;

    default <V> ThrowableBiFunction<A, B, V> andThen(
            Function<? super R, ? extends V> after
    ) {
        Objects.requireNonNull(after);
        return (a,b) -> after.apply(this.apply(a,b));
    }
}
