package com.nixiedroid.function;

/**
 * Represents a supplier of results which can potentially throw a {@link Throwable}.
 * There is no requirement that a new or distinct result be returned each time the supplier is invoked.
 *
 * <p>This is a functional interface whose functional method is {@link #get()}.
 *
 * @param <R> the type of results supplied by this supplier
 * @param <T> the type of Exception thrown by this supplier
 */
@SuppressWarnings("unused")
@FunctionalInterface
public interface ThrowableSupplier<R, T extends Throwable> {

    /**
     * Gets a result.
     *
     * @return a result
     * @throws T if unable to compute a result
     */
    R get() throws T;
}