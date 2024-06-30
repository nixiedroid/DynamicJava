package classes;

import java.lang.invoke.MethodHandle;

public class MethodHandleTests {
    private int value = 10;
    private int sValue = 11;

    public static void main(String[] args) {

    }

    static class SInnerClass {
        int a = 4;
        private int b = 5;

        private SInnerClass() {
        }
        public SInnerClass(int a) {
        }

        void hello() {
        }

        //static void sHello(){}//Java 17+
        private void pHello() {
        }
    }

    private static class SPInnerClass {
        int a = 4;
        private int b = 5;

        private SPInnerClass() {
        }
        public SPInnerClass(int a) {
        }

        void hello() {
        }

        //static void sHello(){}//Java 17+
        private void pHello() {
        }
    }

    class InnerClass {
        int a = 4;
        private int b = 5;

        private InnerClass() {
            System.out.println(MethodHandleTests.this.value);
            System.out.println(MethodHandleTests.this.sValue);
        }
        public InnerClass(int a) {
        }

        void hello() {
        }

        //static void sHello(){}//Java 17+
        private void pHello() {
        }
    }

    private class PInnerClass {
        int a = 4;
        private int b = 5;

        private PInnerClass() {
            System.out.println(MethodHandleTests.this.value);
            System.out.println(MethodHandleTests.this.sValue);
        }
        public PInnerClass(int a) {
        }

        void hello() {
        }

        //static void sHello(){}//Java 17+
        private void pHello() {
        }
    }
}

class OuterClass {
    private final MethodHandle outerPrivateMethod;

    OuterClass(MethodHandle outerPrivateMethod) {
        this.outerPrivateMethod = outerPrivateMethod;
        new MethodHandleTests.SInnerClass(2);
        MethodHandleTests mh = new MethodHandleTests();
        MethodHandleTests.InnerClass ic = mh.new InnerClass(3);

        (new MethodHandleTests()).new InnerClass(5);
    }

    public void execute() {
        try {
            this.outerPrivateMethod.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
