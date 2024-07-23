package com.nixiedroid.function;

import java.util.Objects;

public interface ThrowableBiConsumer<A,B> {
    void accept(A var1,B var2) throws Throwable ;

    default ThrowableBiConsumer<A,B> andThen(ThrowableBiConsumer<? super A,? super B> after) {
        Objects.requireNonNull(after);
        return (a,b) -> {
            this.accept(a,b);
            after.accept(a,b);
        };
    }
}
