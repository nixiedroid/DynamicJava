package com.nixiedroid.exceptions;

/**
 * A utility class that provides methods to throw exceptions and errors,
 * including methods to throw exceptions with a return value and methods to throw
 * exceptions that terminate the JVM.
 */
@SuppressWarnings("RedundantThrows")
public final class Thrower {

    private Thrower() {
    }

    /**
     * Throws the specified exception and returns {@code null}.
     * <p>
     * The return statement is unreachable and serves to satisfy the compiler's requirement
     * for a return value. The method will always throw the exception and never actually return.
     *
     * @param <R> the type of the return value (which is not used)
     * @param exc the exception to throw
     * @return {@code null} (unreachable)
     * @throws Throwable the exception to be thrown
     */
    public static <R> R throwExceptionWithReturn(Throwable exc) {
        throwException(exc);
        return null; // Actually, unreachable
    }

    /**
     * Throws the specified exception.
     *
     * @param <E> the type of the exception to throw
     * @param exc the exception to throw
     * @throws E the exception to be thrown
     */
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwException(Throwable exc) throws E {
        throw (E) exc; // Throws actual exception
    }

    /**
     * Throws an {@link Error} initialized with the specified exception.
     *
     * @param <E> the type of the exception to throw (used for type checking)
     * @param exc the exception to be wrapped in an {@link Error}
     * @throws E the error to be thrown
     */
    public static <E extends Throwable> void throwExceptionAndDie(Throwable exc) throws E {
        throw new Error(exc); // Throws actual exception
    }

    /**
     * Throws an {@link Error} initialized with the specified message.
     *
     * @param <E>     the type of the error to throw (used for type checking)
     * @param message the message to be used in the {@link Error}
     * @throws E the error to be thrown
     */
    public static <E extends Throwable> void throwExceptionAndDie(String message) throws E {
        throw new Error(message); // Throws actual exception
    }
}