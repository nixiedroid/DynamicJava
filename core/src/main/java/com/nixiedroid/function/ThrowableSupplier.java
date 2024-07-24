package com.nixiedroid.function;

/**
 * Represents a supplier of results which can potentially throw a {@link Throwable}.
 * There is no requirement that a new or distinct result be returned each time the supplier is invoked.
 *
 * <p>This is a functional interface whose functional method is {@link #get()}.
 *
 * @param <T> the type of results supplied by this supplier
 */
@FunctionalInterface
public interface ThrowableSupplier<T> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws Throwable if unable to compute a result
     */
    T get() throws Throwable;
}