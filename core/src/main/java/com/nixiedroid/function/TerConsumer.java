package com.nixiedroid.function;
/**
 * Represents an operation that accepts three input arguments and returns no result.
 * This is a three-arity specialization of {@link java.util.function.Consumer}.
 *
 * <p>This is a functional interface whose functional method is {@link #accept(Object, Object, Object)}.
 *
 * @param <A> the type of the first argument to the operation
 * @param <B> the type of the second argument to the operation
 * @param <C> the type of the third argument to the operation
 */
@FunctionalInterface
public interface TerConsumer<A, B, C> {

    /**
     * Performs this operation on the given arguments.
     *
     * @param var1 the first input argument
     * @param var2 the second input argument
     * @param var3 the third input argument
     */
    void accept(A var1, B var2, C var3);
}