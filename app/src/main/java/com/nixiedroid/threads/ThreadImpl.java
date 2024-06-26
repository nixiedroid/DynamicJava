package com.nixiedroid.threads;

public class ThreadImpl implements ThreadRunnable {
    private final ThreadContext ctx;
    private final int number;
    @Override
    public void run() {
        threadCounters();
    }

    public void threadCounters(){
        this.ctx.MadFetchAndAdd(this.number);
        this.ctx.goodFAA(this.number);
    }


    public ThreadImpl(final  ThreadContext ctx,int threadnum) {
        this.ctx = ctx;
        this.number = threadnum;
    }
}
