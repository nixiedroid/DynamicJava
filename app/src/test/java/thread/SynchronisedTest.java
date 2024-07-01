package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class SynchronisedTest {

    @SuppressWarnings("unused")
    final Object monitor = new Object();
    private int value = 0;

    /**
     * Returns a unique value.
     */
    public int getNext() {
        //    synchronized (this.monitor) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {}
        this.value = (int) Math.pow(this.value, 2);
        this.value = (int) Math.sqrt(this.value);
        return this.value++;
        //    }
    }

    @Test
    void test() throws Exception {
        final int amount = 150;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            ThreadedSupplier ts = new ThreadedSupplier();
            threads.add(new Thread(ts));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
        Assertions.assertNotEquals(this.value,amount);
        System.out.println("Result sum: " + this.value + ", should be " + amount);
    }

    class ThreadedSupplier implements Runnable {
        int i;

        @Override
        public void run() {
            this.i = getNext();
        }
    }

    static class SyncClass {
        public synchronized void doSomething() {
            System.out.println("InternalSync");
        }
    }
    static class ExtraSyncClass extends SyncClass {
        public synchronized void doSomething() {
            System.out.println("ExternalSync");
            super.doSomething();
        }
    }

    /**
     * Example of NOT deadlock<br><br>
     *
     * Reentrancy facilitates encapsulation of locking behavior, and thus simplifies the development of object‐oriented
     * concurrent code. Without reentrant locks, the very natural‐looking code, in which a subclass overrides a
     * synchronized method and then calls the superclass method, would deadlock. Because the doSomething methods in
     * SyncClass and ExtraSyncClass are both synchronized, each tries to acquire the lock on the SyncClass before proceeding.
     * But if intrinsic locks were not reentrant, the call to super.doSomething would never be able to acquire the lock because
     * it would be considered already held, and the thread would permanently stall waiting for a lock it can never acquire.
     * Reentrancy saves us from deadlock in situations like this
     */
    @Test
    void deadlock(){
        new ExtraSyncClass().doSomething();
    }



}
