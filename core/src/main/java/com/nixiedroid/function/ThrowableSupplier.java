package com.nixiedroid.function;

public interface ThrowableSupplier<T> {
    T get() throws Throwable;
}
