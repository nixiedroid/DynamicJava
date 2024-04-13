package com.nixiedroid.interfaces;

import java.util.Objects;
import java.util.function.Consumer;

public interface ThrowableConsumer<A> {
    void accept(A var1) throws Throwable ;

    default ThrowableConsumer<A> andThen(Consumer<? super A> after) {
        Objects.requireNonNull(after);
        return (t) -> {
            this.accept(t);
            after.accept(t);
        };
    }
}
