package classes;

import java.lang.invoke.MethodHandle;

@SuppressWarnings({"unused", "FieldMayBeFinal"})
public class InnerClassSample {
    private static final int ELEVEN =11;
    private int value = 10;
    private int sValue = ELEVEN;

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
            System.out.println(InnerClassSample.this.value);
            System.out.println(InnerClassSample.this.sValue);
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
            System.out.println(InnerClassSample.this.value);
            System.out.println(InnerClassSample.this.sValue);
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

@SuppressWarnings("unused")
class OuterClass {
    private final MethodHandle outerPrivateMethod;

    OuterClass(MethodHandle outerPrivateMethod) {
        this.outerPrivateMethod = outerPrivateMethod;
        new InnerClassSample.SInnerClass(2);
        InnerClassSample mh = new InnerClassSample();
        InnerClassSample.InnerClass ic = mh.new InnerClass(3);

        (new InnerClassSample()).new InnerClass(5);
    }

    public void execute() {
        try {
            this.outerPrivateMethod.invoke();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
