package com.nixiedroid.exceptions;

public class Thrower {
    public static <R> R throwExceptionWithReturn(Throwable exc) {
        throwException(exc);
        return null; //Actually, unreachabe
    }
    @SuppressWarnings("unchecked")
    public static <E extends Throwable> void throwException(Throwable exc) throws E{
        throw (E)exc; //Throws actual exception
    }
    public static <E extends Throwable> void throwExceptionAndDie(Throwable exc) throws E{
        throw new Error(exc); //Throws actual exception
    }
    public static <E extends Throwable> void throwExceptionAndDie(String message) throws E{
        throw new Error(message); //Throws actual exception
    }
}
