package com.nixiedroid.function;

import java.util.function.BiConsumer;

/**
 * An adapter class that implements the {@link ThrowableBiConsumer} interface
 * by wrapping a standard {@link BiConsumer}.
 *
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 */
public class BiConsumerAdapter<A, B> implements ThrowableBiConsumer<A, B> {

    private final BiConsumer<? super A, ? super B> consumer;

    /**
     * Constructs a {@code BiConsumerAdapter} with the specified {@link BiConsumer}.
     *
     * @param consumer the consumer to be wrapped
     */
    public BiConsumerAdapter(BiConsumer<? super A, ? super B> consumer) {
        this.consumer = consumer;
    }

    /**
     * Performs this operation on the given arguments.
     *
     * @param var1 the first input argument
     * @param var2 the second input argument
     * @throws Throwable if unable to perform the operation
     */
    @Override
    public void accept(A var1, B var2) throws Throwable {
        this.consumer.accept(var1, var2);
    }
}