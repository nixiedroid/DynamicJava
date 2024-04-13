package com.nixiedroid.interfaces;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

@FunctionalInterface
public interface QuadFunction<A, B, C, D,R> {
    R apply(A var1, B var2, C var3, D var4);

    default <V> QuadFunction< A, B, C, D,V> andThen(
            Function<? super R, ? extends V> after
    ) {
        Objects.requireNonNull(after);
        return (a,b,c,d) -> after.apply(this.apply(a,b,c,d));
    }
}
