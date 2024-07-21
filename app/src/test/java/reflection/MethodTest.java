package reflection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class MethodTest {
    private final byte FORTY_TWO = 42;

    int intFunction(String str) {
        return FORTY_TWO;
    }

    void intConsumer(int v) {
        System.out.println("A");
    }

    void integerConsumer(Integer v) {
        System.out.println("B");
    }

    public <T extends Number> T genericMethod(int value, Class<T> type) {
        return switch (value) {
            case 1 -> type.cast(this.FORTY_TWO);
            case 2 -> type.cast((short) this.FORTY_TWO);
            case 4 -> type.cast((int) this.FORTY_TWO);
            case 8 -> type.cast((long) this.FORTY_TWO);
            default -> type.cast((float) this.FORTY_TWO);
        };
    }

    @SuppressWarnings("unchecked")
    public <T extends Number> T genericMethodUnchecked(int value) {
        return switch (value) {
            case 1 -> (T) Byte.valueOf(this.FORTY_TWO);
            case 2 -> (T) Short.valueOf(this.FORTY_TWO);
            case 4 -> (T) Integer.valueOf(this.FORTY_TWO);
            case 8 -> (T) Long.valueOf(this.FORTY_TWO);
            default -> (T) Float.valueOf(this.FORTY_TWO);
        };
    }

    @Test
    void genericMethodTest() {
        Assertions.assertEquals(Byte.class, genericMethod(1, Byte.class).getClass());
        Assertions.assertEquals(this.FORTY_TWO, genericMethod(1, Byte.class).intValue());
        Assertions.assertEquals(Byte.class, genericMethodUnchecked(1).getClass());
        Assertions.assertEquals(this.FORTY_TWO, genericMethodUnchecked(1).intValue());
    }

    @Test
    void methodsTest() throws ReflectiveOperationException {
        Method m = MethodTest.class.getDeclaredMethod("intFunction", String.class);
        Method intM = MethodTest.class.getDeclaredMethod("intConsumer", int.class);
        Method integerM = MethodTest.class.getDeclaredMethod("integerConsumer", Integer.class);
        int i = (int) m.invoke(this, "HEllO");
        intM.invoke(this, 2);
        integerM.invoke(this, 2);

        Method gM = MethodTest.class.getMethod("genericMethod", int.class, Class.class);
        Method gMU = MethodTest.class.getMethod("genericMethodUnchecked", int.class);


    }

    @Test
    void TestLookupLambda() throws Exception {
        metaSupplier(() -> 42);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> clazz = cl.loadClass("reflection.MethodTest");
        System.out.println(clazz.getName());
    }

    void metaSupplier(Supplier<Integer> sup) {
        System.out.println(sup.get());
    }

    public final static class H {

    }
}
