package thread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class ConcurrentTest {

    final Object monitor = new Object();
    private int value = 0;

    /**
     * Returns a unique value.
     */
    public int getNext() {
        synchronized (this.monitor) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
            }
            this.value = (int) Math.pow(this.value, 2);
            this.value = (int) Math.sqrt(this.value);
            return this.value++;
        }
    }

    @Test
    void test() throws Exception {
        final int amount = 150;
        List<ThreadedSupplier> sups = new ArrayList<>();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            ThreadedSupplier ts = new ThreadedSupplier();
            sups.add(ts);
            threads.add(new Thread(ts));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
//        for (int i = 0; i < amount; i++) {
//            if (i != sups.get(i).getI()) {
//                System.out.println("Desync: " + i + " - " + sups.get(i).getI());
//            }
//        }
        System.out.println("Result sum: " + this.value + ", should be " + amount);
    }

    class ThreadedSupplier implements Runnable {
        int i;

        @Override
        public void run() {
            this.i = getNext();
        }

        public int getI() {
            return this.i;
        }
    }

}
