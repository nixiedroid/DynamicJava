package thread;

import java.util.concurrent.atomic.AtomicInteger;

public class ThreadContext {
    private int faa = 1;
    private AtomicInteger atomicFAA = new AtomicInteger(1);

    public int getFaa() {
        return this.faa;
    }

    public int getAtomicFAA() {
        return this.atomicFAA.get();
    }

    public void MadFetchAndAdd(int val){
        //this.i = this.i + val;
        this.faa+=val;
    }
    public void goodFAA(int val){
        this.atomicFAA.addAndGet(val);
    }


}
