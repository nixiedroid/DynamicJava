package com.nixiedroid.threads;

import java.util.ArrayList;

public interface ThreadRunnable  extends Runnable {
    default Thread start(){
        Thread thr = new Thread(this);
        thr.start();
        return thr;
    }
}
