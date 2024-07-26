package com.nixiedroid.function;

import java.util.function.Function;

/**
 * An adapter class that implements the {@link ThrowableFunction} interface
 * by wrapping a standard {@link Function}.
 *
 * @param <A> the type of the input to the function
 * @param <R> the type of the result of the function
 * @param <T> the type of Exception to throw
 */
public class FunctionAdapter<A, R, T extends Throwable> implements ThrowableFunction<A, R, T> {

    private final Function<? super A, ? extends R> function;

    /**
     * Constructs a {@code FunctionAdapter} with the specified {@link Function}.
     *
     * @param function the function to be wrapped
     */
    public FunctionAdapter(Function<? super A, ? extends R> function) {
        this.function = function;
    }

    /**
     * Applies this function to the given argument.
     *
     * @param var1 the function argument
     * @return the function result
     * @throws T if unable to compute a result
     */
    @Override
    public R apply(A var1) throws T {
        return this.function.apply(var1);
    }
}