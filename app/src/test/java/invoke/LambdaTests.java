package invoke;

import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class LambdaTests {
    int a;
    @Test
    void supplier() {
        stringApplier(s -> System.out.println( s+ a));
    }

    void stringApplier(Consumer<String> cons) {
        cons.accept("Hello");
    }
}

/*
@Test
    void supplier() {
        stringApplier((v0) -> { // java.util.function.Consumer.accept(java.lang.Object):void
            lambda$supplier$0(v0);
        });
    }

    private static void lambda$supplier$0(String string) {
    System.out.println(string);
}

void stringApplier(Consumer<String> cons) {
    cons.accept("Hello");
}
 */

/*
@Test
    void supplier() {
        PrintStream printStream = System.out;
        Objects.requireNonNull(printStream);
        stringApplier(printStream::println);
    }

    void stringApplier(Consumer<String> cons) {
        cons.accept("Hello");
    }
 */