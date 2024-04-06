package com.nixiedroid.stuff;

import com.nixiedroid.unsafe.Unsafe;

public class Cool {
    private static sun.misc.Unsafe unsafe = Unsafe.getUnsafe();

    public static void main(String[] args) {
        countTo655536();
        aNEQa();
//        stuckOverflow(0);
//        stuckOverflow(0);
//        stuckOverflow(0);
        new Thread(new Fork()).start();
    }

    public static void countTo655536() {
        short i = 0;
        char k = 0;
        for (int j = 0; j < 10; j++) {
            System.out.print((i += 16384) + " "); //short is signed
            System.out.println((int) (k += 16384)); //char is unsigned
        }
    }

    public static void aNEQa() {
        System.out.println("is " + Float.NaN + " equals to " + Float.NaN + " ?: " + (Float.NaN == Float.NaN));
    }
    public static void isStackIsUp(){
//        long lon =  unsafe.getAddress()
//        System.out.println();
    }

    public static void stuckOverflow(int counter) {
        counter++;
        try {
            stuckOverflow(counter);
        } catch (StackOverflowError e) {
            System.out.println("End is " + counter);
        }
    }

    static class Runn implements Runnable {
        @Override
        public void run() {
            System.out.println("RUNNING...");
            stuckOverflow(0);
        }

    }

    static class Fork implements Runnable {
        @Override
        public void run() {
            new Thread(new Fork()).start();
            new Thread(new Fork()).start();
        }
    }

    static class Thr extends Thread {
        @Override
        public void run() {
            System.out.println("THREADING...");
            stuckOverflow(0);
        }
    }
}
