package com.nixiedroid.modules.models;

import com.nixiedroid.modules.Context;

public abstract class AllocateInstance {
    public abstract Object allocate(Class<?> input) throws InstantiationException;

    public static class ForJava7 extends AllocateInstance {
        @Override
        public Object allocate(Class<?> input) throws InstantiationException {
            return Context.i().getUnsafe().allocateInstance(input);
        }

    }
}
