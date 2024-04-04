package com.nixiedroid.magic.parts;

import com.nixiedroid.magic.Context;

public abstract class AllocateInstance {

    public static class ForJava7 extends AllocateInstance {
        public Object allocate(Class<?> input) throws InstantiationException {
            return Context.i().getUnsafe().allocateInstance(input);
        }

    }
}
