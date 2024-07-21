package invoke;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.*;

public class CallSiteTests {
    static String getStr() {
        return "hello";
    }
    String getStrVirtual() {
        return "helloVirtual";
    }

    @Test
    void createConstantCallSiteTest() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(double.class, int.class, String.class);
        MethodHandle mh = lookup.findVirtual(this.getClass(), "a", type);
        CallSite site = new ConstantCallSite(mh);
        Assertions.assertEquals(4., (double) site.dynamicInvoker().invoke(this, 4, "gg"));
    }

    @Test
    void createMutableCallSiteTest() throws Throwable {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType type = MethodType.methodType(String.class);
        CallSite site = new MutableCallSite(type);
        MethodHandle mh = site.dynamicInvoker();
        MethodHandle MH_upcase = lookup.findVirtual(String.class, "toUpperCase", type);
        MethodHandle worker1 = MethodHandles.filterReturnValue(mh, MH_upcase);
        site.setTarget(MethodHandles.constant(String.class, "Rocky"));
        Assertions.assertEquals("ROCKY", (String) worker1.invokeExact());
        site.setTarget(MethodHandles.constant(String.class, "test"));
        Assertions.assertEquals("TEST", (String) worker1.invokeExact());
        site.setTarget(lookup.findStatic(this.getClass(), "getStr", type));
        Assertions.assertEquals("HELLO", (String) worker1.invokeExact());
        Assertions.assertThrows(
                WrongMethodTypeException.class,
                ()->site.setTarget(lookup.findVirtual(this.getClass(), "getStrVirtual", type))
        );
    }

    double a(int b, String c) {
        return b;
    }
}
