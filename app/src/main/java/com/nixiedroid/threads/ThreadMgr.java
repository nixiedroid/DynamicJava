package com.nixiedroid.threads;

public class ThreadMgr {
    private /*volatile*/ boolean isRunning = true;

    public ThreadMgr() {

        volatileSample();

    }

    private void volatileSample() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            this.isRunning = false;
        }).start();
        while (this.isRunning) {
        }
    }

    private void fetchAndAdd() {
        ThreadContext ctx = new ThreadContext();
        for (int i = 1; i <= 10; i++) {
            new ThreadImpl(ctx, i).start();
        }
        System.out.println("Result of mad FAA should be 46, but is: " + ctx.getFaa());
        System.out.println("Result of good FAA should be 46, but is: " + ctx.getAtomicFAA());
    }
}
