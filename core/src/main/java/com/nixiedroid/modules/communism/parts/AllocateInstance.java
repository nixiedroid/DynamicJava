package com.nixiedroid.modules.communism.parts;

import com.nixiedroid.modules.communism.Context;

public abstract class AllocateInstance {
    public abstract Object allocate(Class<?> input) throws InstantiationException;

    public static class ForJava7 extends AllocateInstance {
        @Override
        public Object allocate(Class<?> input) throws InstantiationException {
            return Context.i().getUnsafe().allocateInstance(input);
        }

    }
}
