package com.nixiedroid.function;

import java.util.function.BiConsumer;

public class BiConsumerAdapter<A, B> implements ThrowableBiConsumer<A, B> {

    private final BiConsumer<? super A, ? super B> consumer;

    public BiConsumerAdapter(BiConsumer<? super A, ? super B> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void accept(A var1, B var2) throws Throwable {
        this.consumer.accept(var1, var2);
    }
}
