package thread;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;


class VolatileTest {
    private static final long TWO_SECONDS = 2L;
    private /*volatile*/ boolean isRunning = true;
    /**
     * Timeout Exception here is Required =)
     */
    @Test
    public void volatileTest() {
        try {
            Assertions.assertTimeoutPreemptively(java.time.Duration.ofSeconds(TWO_SECONDS), this::executeTest);
        } catch (AssertionFailedError e){
            Assertions.assertTrue(true);
            return;
        } Assertions.fail();
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
