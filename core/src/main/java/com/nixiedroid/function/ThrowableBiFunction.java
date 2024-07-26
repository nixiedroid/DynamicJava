package com.nixiedroid.function;

/**
 * Represents a function that accepts two arguments and produces a result,
 * potentially throwing a {@link Throwable}. This is a two-arity specialization of {@link java.util.function.BiFunction}.
 *
 * <p>This is a functional interface whose functional method is {@link #apply(Object, Object)}.
 *
 * @param <A> the type of the first argument to the function
 * @param <B> the type of the second argument to the function
 * @param <R> the type of the result of the function
 * @param <T> the type of exception to Throw
 */
@FunctionalInterface
public interface ThrowableBiFunction<A, B, R,T extends Throwable> {

    /**
     * Applies this function to the given arguments.
     *
     * @param var1 the first function argument
     * @param var2 the second function argument
     * @return the function result
     * @throws T if unable to compute a result
     */
    R apply(A var1, B var2) throws T;
}