package com.nixiedroid.samples;

public class Fork implements Runnable {
    @Override
    public void run() {
        new Thread(new Fork()).start();
        new Thread(new Fork()).start();
    }
}
