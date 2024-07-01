package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class FutureTest {


    private static final int TWO_SECONDS = 2000;
    private static final int TWO_HUNDRED = 200;

    @Test
    void futureTaskTest() {
        Thread main = Thread.currentThread();
        ExecutorService exec = Executors.newSingleThreadExecutor();
        Future<Integer> future = exec.submit(() -> {
            System.out.println("Start Future");
            try {
                Thread.sleep(TWO_SECONDS);
            } catch (InterruptedException ignored) {
            }
            return TWO_HUNDRED;
        });

        Thread recv =  new Thread(() -> {
            while (!future.isDone()) {
                Thread.onSpinWait();
                try {
                    System.out.println("Got Future: " + future.get());
                    main.interrupt();
                } catch (InterruptedException | ExecutionException e) {
                    Assertions.fail();
                }
            }
        });
        recv.start();
        System.out.println("Main Thread Still Running");
        while (!Thread.interrupted()){Thread.onSpinWait();}
    }
}
