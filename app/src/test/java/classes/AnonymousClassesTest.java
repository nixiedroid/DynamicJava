package classes;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.fail;

public class AnonymousClassesTest {
    static int sq = 6;
    int a = 8;
    int aq = 4;

    @Test
    void testInnerClasses() {
        try {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            Class<Canary> cClass = Canary.class;
            cl.loadClass("classes.AnonymousClassesTest$Canary");
            System.out.println(cClass.getName());
            Class.forName("classes.AnonymousClassesTest$Canary");
            new Canary2();
        } catch (Throwable t) {
            fail("Should not have thrown any exception: \n " + t);
        }
        new Anon() {
            @Override
            public void getInfo() {
                System.out.println(this.getClass().getName());
                System.out.println(AnonymousClassesTest.this.a + " " + a);
            }
        };
    }

    static class Canary {
        static {

            System.out.println("Hello from Inside" + sq);
        }

        int a = 4;
    }

    abstract static class Anon {
        static {
            System.out.println("Hello");
        }

        {
            getInfo();
        }

        abstract public void getInfo();
    }

    class Canary2 {
        int a = 4;

        {
            System.out.println("Super " + super.getClass().getName() + " " + this.a  + " " + AnonymousClassesTest.this.a);
        }
    }
}

