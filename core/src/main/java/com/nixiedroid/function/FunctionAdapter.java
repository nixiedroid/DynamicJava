package com.nixiedroid.function;

import java.util.function.Function;

public class FunctionAdapter<A,R> implements ThrowableFunction<A,R> {

    private final Function<? super A, ? extends R> function;

    public FunctionAdapter(Function<? super A, ? extends R> function1) {
        this.function = function1;
    }

    @Override
    public R apply(A var1) throws Throwable {
        return this.function.apply(var1);
    }
}
