package com.nixiedroid.function;

/**
 * Represents an operation that accepts a single input argument and returns no result,
 * potentially throwing a {@link Throwable}. This is a one-arity specialization of {@link java.util.function.Consumer}.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(Object)}.
 *
 * @param <A> the type of the input to the operation
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface ThrowableConsumer<A> {

    /**
     * Performs this operation on the given argument.
     *
     * @param var1 the input argument
     * @throws Throwable if unable to perform the operation
     */
    void accept(A var1) throws Throwable;
}