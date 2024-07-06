package classes;

import com.nixiedroid.exceptions.Thrower;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class SuperclassTest {
    @Test
    void test() {
        Assertions.assertEquals(4, new SubClass().supSuperA());
        Assertions.assertEquals(1, new SupSuperClass().getA());
        Assertions.assertEquals(2, new SuperClass().getA());
        Assertions.assertEquals(2, new SubClass().superA());
        Assertions.assertEquals(4, new SubClass().getA());
    }

    @Test
    void specialTest() {
        SubClass sub = new SubClass();
        //caller class must be a subclass below the method
        Assertions.assertThrows(
                IllegalAccessException.class,
                () -> getSpecial(SuperClass.class, SubClass.class, sub)
        );
        Assertions.assertEquals(
                4,
                getSpecial(
                        SubClass.class,
                        SubClass.class,
                        sub
                )
        );
        Assertions.assertEquals(
                2,
                getSpecial(
                        SubClass.class,
                        SuperClass.class,
                        sub
                )
        );
        Assertions.assertEquals(
                2,
                getSpecial(
                        SubClass.class,
                        SupSuperClass.class,
                        sub
                )
        );
        Assertions.assertEquals(
                1,
                getSpecial(
                        SuperClass.class,
                        SupSuperClass.class,
                        sub
                )
        );
    }

    <T extends SupSuperClass> int getSpecial(
            Class<? extends SupSuperClass> from,
            Class<? extends SupSuperClass> to,
            T object) {
        MethodType mt = MethodType.methodType(int.class);
        try {
            MethodHandles.Lookup l = MethodHandles.privateLookupIn(
                    from, MethodHandles.lookup()
            );
            MethodHandle mh = l.findSpecial(
                    to,
                    "getA",
                    mt,
                    from
            );
            try {
                return (int) mh.invoke(object);
            } catch (Throwable t) {
                Thrower.throwException(t);
            }
        } catch (ReflectiveOperationException e) {
            Thrower.throwException(e);
        }
        return 0;
    }

    @SuppressWarnings("ClassTooDeepInInheritanceTree")
    static class SubClass extends SuperClass {
        int a = 4;

        @Override
        int getA() {
            return this.a;
        }

        int superA() {
            return super.getA();
        }

        int supSuperA() {
            return ((SupSuperClass) this).getA();
        }

    }

    @SuppressWarnings("FieldCanBeLocal")
    static class SuperClass extends SupSuperClass {
        private final int a = 2;

        @SuppressWarnings("EqualsWithItself")
        public SuperClass() {
            super();
            Assertions.assertTrue(equals(this));
            Assertions.assertNotNull(toString());
            Assertions.assertNotNull(this.getClass().getSimpleName());
        }

        @Override
        int getA() {
            return this.a;
        }
    }

    static class SupSuperClass {
        private final int a = 1;

        @SuppressWarnings("EqualsWithItself")
        public SupSuperClass() {
            Assertions.assertTrue(equals(this));
            Assertions.assertNotNull(toString());
            Assertions.assertNotNull(this.getClass().getSimpleName());
        }

        int getA() {
            return this.a;
        }

        @Override
        public String toString() {
            return "SupSuperClass{" +
                    "a=" + this.a +
                    '}';
        }
    }

}



