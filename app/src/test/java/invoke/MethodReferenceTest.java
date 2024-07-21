package invoke;

import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class MethodReferenceTest {
    @Test
    void testMethodReference() {


    }

    String  fun(Function<Integer, String> fun) {
        return fun.apply(6);
    }
}
