package com.nixiedroid.interfaces;

import java.util.Objects;
import java.util.function.Function;

public interface ThrowableFunction<A,R> {
    R apply(A var1) throws Throwable;

    default <V> ThrowableFunction<A, V> andThen(
            Function<? super R, ? extends V> after
    ) {
        Objects.requireNonNull(after);
        return (a) -> after.apply(this.apply(a));
    }
}
