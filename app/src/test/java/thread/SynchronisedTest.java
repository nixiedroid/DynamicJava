package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.util.ArrayList;
import java.util.List;

class SynchronisedTest {

    private static final long TWO_SECONDS = 2L;
    @SuppressWarnings("unused")
    final Object monitor = new Object();
    final Object res1 = new Error();
    final Object res2 = new Error();
    Runnable task1 = () -> {
        synchronized (this.res1) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this.res2) {
                System.out.printf("res1: %s; res2: %s\n", this.res1, this.res2);
            }
        }
    };
    Runnable task2 = () -> {
        synchronized (this.res2) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            synchronized (this.res1) {
                System.out.printf("res2: %s; res1: %s\n", this.res2, this.res1);
            }
        }
    };
    private int value = 0;

    /**
     * Returns a unique value.
     */
    public int getNext() {
        //    synchronized (this.monitor) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException ignored) {
        }
        this.value = (int) Math.pow(this.value, 2);
        this.value = (int) Math.sqrt(this.value);
        return this.value++;
        //    }
    }

    @Test
    void testMultipleThreadsAccessGlobalVariable() throws Exception {
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
        Assertions.assertEquals(this.value, amount);
        System.out.println("Result sum: " + this.value + ", should be " + amount);
    }

    /**
     * Example of NOT deadlock<br><br>
     * <p>
     * Reentrancy facilitates encapsulation of locking behavior, and thus simplifies the development of object‐oriented
     * concurrent code. Without reentrant locks, the very natural‐looking code, in which a subclass overrides a
     * synchronized method and then calls the superclass method, would deadlock. Because the doSomething methods in
     * SyncClass and ExtraSyncClass are both synchronized, each tries to acquire the lock on the SyncClass before proceeding.
     * But if intrinsic locks were not reentrant, the call to super.doSomething would never be able to acquire the lock because
     * it would be considered already held, and the thread would permanently stall waiting for a lock it can never acquire.
     * Reentrancy saves us from deadlock in situations like this
     */
    @Test
    void NoDeadlock() {
        new ExtraSyncClass().doSomething();
    }

    @Test
    void waitTest() {
        Factory factory = new Factory();
        Mining_Drill drill = new Mining_Drill(factory);
        Train train = new Train(factory);

        Thread drr = new Thread(drill);
        Thread trr = new Thread(train);
        drr.start();
        trr.start();
        try {
            Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(TWO_SECONDS), () -> {
                try {
                    drr.join();
                    trr.join();
                } catch (InterruptedException ignored) {
                }
            });
        } catch (
                AssertionFailedError e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();

    }

    @Test
    void deadlock() {
        Thread drr = new Thread(this.task1);
        Thread trr = new Thread(this.task2);
        drr.start();
        trr.start();
        try {
            Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(TWO_SECONDS), () -> {
                try {
                    drr.join();
                    trr.join();
                } catch (InterruptedException ignored) {
                }
            });
        } catch (AssertionFailedError e) {
            Assertions.assertTrue(true);
            return;
        }
        Assertions.fail();
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

    static class Factory {
        private int iron_piece = 0;

        public synchronized void get() {
            while (this.iron_piece < 1) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
            this.iron_piece--;
            System.out.println("GOT: " + (1 + this.iron_piece));
            notify();
        }

        public synchronized void put() {
            while (this.iron_piece >= 3) {
                try {
                    wait();
                } catch (InterruptedException ignored) {
                }
            }
            this.iron_piece++;
            System.out.println("PUT: " + this.iron_piece);
            notify();
        }
    }

    static class Mining_Drill implements Runnable {

        Factory factory;

        Mining_Drill(Factory store) {
            this.factory = store;
        }

        public void run() {
            final int THIRTY =30;
            for (int i = 1; i <= THIRTY; i++) {
                this.factory.put();
            }
        }
    }

    static class Train implements Runnable {

        Factory factory;

        Train(Factory store) {
            this.factory = store;
        }

        public void run() {
            for (int i = 1; i <= 4; i++) {
                this.factory.get();
            }
        }
    }

    class ThreadedSupplier implements Runnable {
        int i;

        @Override
        public void run() {
            this.i = getNext();
        }
    }


}
