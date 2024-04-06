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
    public static boolean isStackGrowsUp(long address){
        int x = 1;
        if (address == 0){
            return isStackGrowsUp(Unsafe.getAddress(x));
        } else {
            return Unsafe.getAddress(x) >= address;
        }
    }
//    public static Set<String> getBrokenHashSet(){
//        AtomicReference<HashSet<String>> ref = new AtomicReference<>();
//        try {
//            new HashSet<String>(null) {
//                protected void finalize() {
//                    ref.set(this);
//                }
//            };
//        } catch (NullPointerException ignored) {}
//        while (ref.get() == null) {
//            System.gc();
//        }
//        return ref.get();
//    }
}
