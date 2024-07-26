package com.nixiedroid.function;

/**
 * Represents a function that accepts four arguments and produces a result,
 * potentially throwing a {@link Throwable}. This is a four-arity specialization of {@link java.util.function.Function}.
 *
 * <p>This is a functional interface whose functional method is {@link #apply(Object, Object, Object, Object)}.
 *
 * @param <A> the type of the first argument to the function
 * @param <B> the type of the second argument to the function
 * @param <C> the type of the third argument to the function
 * @param <D> the type of the fourth argument to the function
 * @param <R> the type of the result of the function
 */
@FunctionalInterface
public interface ThrowableQuadFunction<A, B, C, D, R,T extends Throwable> {

    /**
     * Applies this function to the given arguments.
     *
     * @param var1 the first function argument
     * @param var2 the second function argument
     * @param var3 the third function argument
     * @param var4 the fourth function argument
     * @return the function result
     * @throws T if unable to compute a result
     */
    R apply(A var1, B var2, C var3, D var4) throws T;
}