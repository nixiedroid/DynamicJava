package com.nixiedroid.function;

/**
 * Represents an operation that accepts two input arguments and returns no result,
 * potentially throwing a {@link Throwable}. This is a two-arity specialization of {@link java.util.function.BiConsumer}.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(Object, Object)}.
 *
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 */
@FunctionalInterface
public interface ThrowableBiConsumer<A, B> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param var1 the first input argument
     * @param var2 the second input argument
     * @throws Throwable if unable to perform the operation
     */
    void accept(A var1, B var2) throws Throwable;
}
