import com.nixiedroid.bytes.StringArrayUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import samples.Cats;

import java.io.*;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicReference;

public class CoolStuffTest {

    static long hashCollisionGenerator(final long value, final int seed) {
        final long longSeed = (seed & 0xFFFF_FFFFL);
        final long lsh = longSeed << 32;
        return lsh ^ value ^ longSeed;
    }

    @SuppressWarnings({"unused", "InfiniteRecursion"})
    public void stuckOverflow(int counter) {
        counter++;
        try {
            stuckOverflow(counter);
        } catch (StackOverflowError e) {
            System.out.println("End is " + counter);
        }
    }

    @Test
    void throwExceptionQuiet() {
        Exception e = new FileNotFoundException();
        Assertions.assertThrows(e.getClass(), () -> quietlyThrow(e));
    }

    @SuppressWarnings("unchecked")
    private <E extends Throwable> void quietlyThrow(Throwable e) throws E {
        if (e != null) throw (E) e;
    }

    @SuppressWarnings("UnusedAssignment")
    @Test
    void performGC() {
        synchronized (this) {
            new Thread(() -> {
                Object o = new Object();
                WeakReference<Object> ref = new WeakReference<>(o);
                o = null;
                while (ref.get() != null) {
                    System.gc();
                }
            }).start();
        }
    }

    /**
     * <a href="https://habr.com/ru/articles/586994/">LInk</a>
     */
    @SuppressWarnings("DataFlowIssue")
    @Test
    void testBrokenHashSet() {
        AtomicReference<HashSet<String>> ref = new AtomicReference<>();
        try {
            new HashSet<String>(null) {
                @SuppressWarnings("deprecation")
                @Override
                protected void finalize() {
                    ref.set(this);
                }
            };
        } catch (NullPointerException ignored) {
        }
        while (ref.get() == null) {
            System.gc();
        }
        Assertions.assertNotNull(ref.get());
    }


    @Test
    void longHashCodeCollisionTest() {
        for (int i = -10; i <= 10; i++) {
            for (int j = -10; j <= 10; j++) {
                Assertions.assertEquals(
                        Long.hashCode(i),
                        Long.hashCode(
                                hashCollisionGenerator(i, j)
                        ));
            }
        }
    }


    @SuppressWarnings({"SimplifiableAssertion", "ComparisonToNaN"})
    @Test
    void jvmInternalsTest() {
        Assertions.assertNull(java.lang.ClassLoader.class.getClassLoader());
        Assertions.assertTrue(0 < ClassLoader.getSystemClassLoader().getDefinedPackages().length);
        int count = ClassLoader.getPlatformClassLoader().getDefinedPackages().length;
        Assertions.assertEquals("platform", java.sql.Array.class.getClassLoader().getName());
        Assertions.assertEquals(count + 1, ClassLoader.getPlatformClassLoader().getDefinedPackages().length);
        Assertions.assertFalse(Float.NaN == Float.NaN);
        short MAX = Short.MAX_VALUE;
        Assertions.assertEquals(Short.MIN_VALUE, (short) (MAX + 1));
        Assertions.assertEquals(MAX + 1, (char) (MAX + 1));
        byte a = (byte) (Byte.MAX_VALUE + 1);
        System.out.printf("%x", a + 2);
    }


    @Test
    void testJavaSerialization() {
        var h = new Cats.MoreCat(3, 4);
        byte[] b;
        try (
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutput out = new ObjectOutputStream(bos)
        ) {
            out.writeObject(h);
            b = bos.toByteArray();
            System.out.println(StringArrayUtils.toString(b));
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        try (
                ByteArrayInputStream bis = new ByteArrayInputStream(b);
                ObjectInput in = new ObjectInputStream(bis)
        ) {
            Cats.MoreCat h2;
            h2 = (Cats.MoreCat) in.readObject();

            Assertions.assertEquals(3, h2.getA());
            Assertions.assertEquals(4, h2.getB());
        } catch (IOException | ClassNotFoundException e) {
            Assertions.fail("Should not have thrown any exception");
        }
    }

}
