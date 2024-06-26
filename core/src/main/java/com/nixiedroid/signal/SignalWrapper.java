package com.nixiedroid.signal;

import sun.misc.Signal;
@SuppressWarnings("unused")
public final class SignalWrapper {
    /**
     * Reflection proof private constructor
     */
    private SignalWrapper() {
        throw new UnsupportedOperationException("Unable to create instance of utility class");
    }

    /**
     * Can handle C Signals. Sadly, Windows supports only SIGINT.
     * SIGINT sent by Ctrl+C.
     * By default, Used to terminate app.
     * This behavior can be overwritten.
     * Rendering application unstoppable normal way
     */
    public static void handleSIGINT(){
        Signal.handle(new Signal("INT"), sig -> {

            System.out.println("I'M UNSTOPPABLE!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
                Runtime.getRuntime().exit(0);
            }
            System.out.println("\nJust kidding :)\n");
            Runtime.getRuntime().exit(0);
        });
    }
    /**
     * Default java way to handle termination
     * Can be run only once
     * RuntimeException also causes this to run
     * Called inside Runtime.exit()
     * @see Runtime#exit(int)
     */
    public static void handleTermination(){
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("IM UNSTOPPABLE!");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
            System.out.println("\nJust kidding :)\n");
        }));
    }
}
