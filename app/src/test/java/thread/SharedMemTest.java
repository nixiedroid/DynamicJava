package thread;

import org.junit.jupiter.api.Test;

public class SharedMemTest {

    private static int number = 7;
    private static boolean ready;

    /**
     * RWTest could loop forever because the value of ready might never become visible to the reader thread. Even
     * more strangely, RWTest could print zero because the write to ready might be made visible to the reader thread
     * before the write to number, a phenomenon known as reordering. There is no guarantee that operations in one thread
     * will be performed in the order given by the program, as long as the reordering is not detectable from within that thread
     * ‐ even if the reordering is apparent to other threads. When the main thread writes first to number and then to done
     * without synchronization, the reader thread could see those writes happen in the opposite order ‐ or not at all.
     */
    @Test
    void RWTest() {


        new ReaderThread().start();


        ready = true;
    }




    private static class ReaderThread extends Thread {
        public void run() {
            while (!ready)
                Thread.yield();
            System.out.println(number);
        }
    }
}
