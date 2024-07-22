package com.nixiedroid.runtime;

import java.lang.ref.WeakReference;
@SuppressWarnings("unused")
public final class Util {
    private static final int TWENTY = 20;

    private Util() {
    }

    public static void printMemUsage(){
        System.out.println("Available heap size = " +
                (Runtime.getRuntime().freeMemory()>> TWENTY) + "MiB");
        System.out.println("Max heap size = " +
                (Runtime.getRuntime().totalMemory()>> TWENTY) + "MiB");
        System.out.println("Max jvm fat size = " +
                (Runtime.getRuntime().maxMemory()>> TWENTY) + "MiB");
    }
    @SuppressWarnings("UnusedAssignment")
    public static void performGC() {
        synchronized (Util.class) {
            new Thread(() -> {
                Object o = new Object();
                WeakReference<Object> ref = new WeakReference<>(o);
                o = null;
                while (ref.get() != null) {
                    System.gc();
                }
            }).start();
        }
    }

    public static void performGC(final Object o) {
        synchronized (Util.class) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    WeakReference<Object> ref = new WeakReference<>(o);
                    while (ref.get() != null) {
                        System.gc();
                    }
                }
            }).start();
        }
    }
}
