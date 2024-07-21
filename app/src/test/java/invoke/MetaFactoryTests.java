package invoke;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.*;
import java.util.function.Supplier;


public class MetaFactoryTests {
    @Test
    void lambdaTest() throws Throwable {
        //Lookup with access to target method
        MethodHandles.Lookup lookup = MethodHandles.lookup();

        /**
         * {@link stringSupplier() }
         */
        MethodType targetMT = MethodType.methodType(String.class);
        MethodHandle target = lookup.findStatic(MetaFactoryTests.class, "stringSupplier", targetMT);

        MethodType invokedFunction = MethodType.methodType(Supplier.class);
        MethodType invokedFunctionType = MethodType.methodType(Object.class);

        // Create the call site
        CallSite callSite = LambdaMetafactory.metafactory(
                lookup,
                "get",
                invokedFunction,
                invokedFunctionType,
                target,
                targetMT
        );

        // Get the factory for the lambda
        MethodHandle factory = callSite.getTarget();

        // Create the lambda function
        @SuppressWarnings("unchecked")
        Supplier<String> lambda = (Supplier<String>) factory.invoke();

        //Execute lambda function
        Assertions.assertEquals("Hello from Lambda", lambda.get());
    }

    static String stringSupplier() {
        return "Hello from Lambda";
    }
}
