package invoke;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

public class MethodReferenceTest {
    public static Object consumer(Supplier<Object> sup) {
       return sup.get();
    }

    @Test
    void testMethodReference() {
        Assertions.assertTrue( consumer(() -> new Object()) instanceof Object);
    }
}
