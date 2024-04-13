package com.nixiedroid.modules.models;

import com.nixiedroid.interfaces.ThrowableFunction;
import com.nixiedroid.modules.Context;

public abstract class AllocateInstance implements ThrowableFunction<Class<?>,Object > {
    public static class ForJava7 extends AllocateInstance {
        @Override
        public Object apply(Class<?> input) throws InstantiationException {
            return Context.i().getUnsafe().allocateInstance(input);
        }
    }
}
