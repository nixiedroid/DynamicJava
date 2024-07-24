package com.nixiedroid.reflection.toolchain;

import com.nixiedroid.function.ThrowableFunction;

interface AllocateInstanceFunction extends ThrowableFunction<Class<?>, Object> {

    class Java7 implements AllocateInstanceFunction {

        private final sun.misc.Unsafe U;

        Java7() {
            this.U = Context.get(UnsafeSupplier.class).get();
        }

        @Override
        public Object apply(Class<?> clazz) throws InstantiationException {
            return this.U.allocateInstance(clazz);
        }

    }

}
