package com.nixiedroid.unsafe;

import java.lang.ref.WeakReference;

@SuppressWarnings("unused")
public class Util {
    public static void halt(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }

    @SuppressWarnings("UnusedAssignment")
    private static void performGcHalting() {
        Object o = new Object();
        WeakReference<Object> ref = new WeakReference<>(o);
        o = null;
        while (ref.get() != null) {
            System.gc();
        }
    }
    public static synchronized void performGc() {
        new Thread(Util::performGcHalting).start();
    }

    public static String toString(byte data) {
        return String.format("%02x", data & 0xFF);
    }

}