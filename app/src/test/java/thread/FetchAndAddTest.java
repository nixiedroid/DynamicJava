package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

class FetchAndAddTest {
    private int expected = 1; //56
    private int faa = 1;
    private final AtomicInteger atomicFAA = new AtomicInteger(1);

    @Test
    void testFAA() {
        List<Thread> threads = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            this.expected +=i;
            int fi=i;
            Thread thr =  new Thread(()->{
                this.atomicFAA.addAndGet(fi);
                //this.i = this.i + val;
                this.faa+=fi;
            });
            threads.add(thr);
            thr.start();
        }
        for(Thread thr : threads){
            if (thr.isAlive()) {
                try {
                    thr.join();
                } catch (InterruptedException e) {
                   Assertions.fail("Should not have thrown any exception");
                }
            }
        }
        //Assertions.assertNotEquals(this.expected, this.faa);
        Assertions.assertEquals(this.expected, this.atomicFAA.get());
    }
}
