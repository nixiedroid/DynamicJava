package thread;

import com.nixiedroid.exceptions.Thrower;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.concurrent.atomic.DoubleAdder;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ConcurrentTest {

    Lock lock = new ReentrantLock();

    @Test
    void Atomic() {
        AtomicIntegerArray ia = new AtomicIntegerArray(5);
        AtomicInteger ai = new AtomicInteger(4);
        DoubleAdder da = new DoubleAdder();
    }

    @Test
    void lockTest() {
        Runnable lockable = this::sharedOp;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            threads.add(new Thread(lockable));
        }
        threads.forEach(Thread::start);
        threads.forEach(thread -> {
            try {
                thread.join();
            } catch (InterruptedException e) {
            }
        });
    }

    void sharedOp() {
        this.lock.lock();
        System.out.println("Locked");
        try {
            Thread.sleep(100);
            Thrower.throwExceptionAndDie("SUDDENLY, thread died");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.lock.unlock();
        }

        System.out.println("Unlocked");
    }
}
