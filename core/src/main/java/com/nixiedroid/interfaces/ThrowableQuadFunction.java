package com.nixiedroid.interfaces;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface ThrowableQuadFunction<A, B, C, D, R>  {
    R apply(A var1, B var2, C var3, D var4) throws Throwable;

    default <V> ThrowableQuadFunction<A, B, C, D, V> andThen(
            Function<? super R, ? extends V> after
    ) {
        Objects.requireNonNull(after);
        return (a,b,c,d) -> {
            return after.apply(this.apply(a,b,c,d));
        };
    }
}