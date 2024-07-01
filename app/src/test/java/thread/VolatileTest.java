package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


class VolatileTest {
    private static final long FIVE_SECONDS = 5L;
    private /*volatile*/ boolean isRunning = true;
    /**
     * Timeout Exception here is Required =)
     */
    @Test
    public void volatileTest() {
        Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(FIVE_SECONDS),this::executeTest);
    }

    @SuppressWarnings({"StatementWithEmptyBody"})
    public void executeTest() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
                this.isRunning = false;
            } catch (InterruptedException ignored) {
            }
        }).start();
        while (this.isRunning) {
        }
    }
}
