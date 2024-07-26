package com.nixiedroid.function;

/**
 * Represents a function that accepts one argument and produces a result,
 * potentially throwing a {@link Throwable}. This is a one-arity specialization of {@link java.util.function.Function}.
 *
 * <p>This is a functional interface whose functional method is {@link #apply(Object)}.
 *
 * @param <A> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <T> the type of exception to throw
 */
@FunctionalInterface
public interface ThrowableFunction<A, R, T extends Throwable> {

    /**
     * Applies this function to the given argument.
     *
     * @param var1 the function argument
     * @return the function result
     * @throws T if unable to compute a result
     */
    R apply(A var1) throws T;
}
