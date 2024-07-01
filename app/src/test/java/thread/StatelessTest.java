package thread;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class StatelessTest {
    @Test
    void createStatelessThreadsTest() throws Throwable{
        final int amount = 10;
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            threads.add(new Thread(() -> {
                int a =1;
                a++;
                System.out.print(a); //why?
            }));
        }
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }
    }
}
