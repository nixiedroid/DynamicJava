package com.nixiedroid.modules.toolchain;

import com.nixiedroid.function.ThrowableFunction;

public class AllocateInstanceFunction implements ThrowableFunction<Class<?>, Object> {

    private final sun.misc.Unsafe U;

    public AllocateInstanceFunction(){
        this.U = Context.get(UnsafeSupplier.class).get();
    }

    @Override
    public Object apply(Class<?> clazz) throws InstantiationException {
        return this.U.allocateInstance(clazz);
    }
}
