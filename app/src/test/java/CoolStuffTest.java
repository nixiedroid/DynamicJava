import com.nixiedroid.Main;
import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.modules.ModuleManager;
import com.nixiedroid.modules.util.Modules;
import com.nixiedroid.runtime.Info;
import samples.Cats;
import samples.Clazz;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

public class CoolStuffTest {
    public static void accessInaccessible() throws ReflectiveOperationException {
        Class<?> cl = Class.forName("jdk.internal.misc.VM");
        Method m = cl.getMethod("initLevel");
        int dat = (int) m.invoke(null);
        System.out.println(dat);
    }

    public static String getModifiers(int modifiers) {
        StringBuilder sb = new StringBuilder("Modifiers:");
        if ((modifiers & Modifier.PUBLIC) != 0) sb.append(" Public");
        if ((modifiers & Modifier.PRIVATE) != 0) sb.append(" Private");
        if ((modifiers & Modifier.PROTECTED) != 0) sb.append(" Protected");
        if ((modifiers & Modifier.STATIC) != 0) sb.append(" Package");
        if ((modifiers & Modifier.STATIC << 1) != 0) sb.append(" Module");
        if ((modifiers & Modifier.STATIC << 2) != 0) sb.append(" Unconditional");
        if ((modifiers & Modifier.STATIC << 3) != 0) sb.append(" Original");
        if (modifiers == -1) sb.append(" Trusted");
        return sb.toString();
    }

    @Test
    void epicText() {
        System.out.println(System.getProperty("java.class.path"));
        System.out.println(System.getProperty("jdk.module.path"));
        Map<String, TreeSet<String>> map = new HashMap<>();
        for (Module m : ModuleLayer.boot().modules()) {
            String name = m.getName();
            map.computeIfAbsent(
                    Optional.ofNullable(m.getClassLoader()).
                            map(ClassLoader::getName)
                            .orElse("boot"), k -> new TreeSet<>()
            ).add(name);
        }
        List<Map.Entry<String, TreeSet<String>>> toSort = new ArrayList<>(map.entrySet());
        toSort.sort(Comparator.comparingInt(e -> List.of("boot", "platform", "app").indexOf(e.getKey())));
        for (Map.Entry<String, TreeSet<String>> e : toSort) {
            String s = e.getKey() + "\n\t" + String.join("\n\t", e.getValue());
            System.out.println(s);
        }
    }

    @Test
    @SuppressWarnings("internal")
    void internalUnsafeTest() {
        sun.misc.Unsafe unsafe = com.nixiedroid.unsafe.UnsafeWrapper.getUnsafe();
        //jdk.internal.misc.Unsafe
        Object o;
        Clazz cat = new Clazz();
        try {
            Field f = unsafe.getClass().getDeclaredField("theInternalUnsafe");
            f.setAccessible(true);
            o = f.get(null);
            Method m = o.getClass().getMethod("objectFieldOffset", Field.class);
            Field catF = Clazz.class.getDeclaredField("sInteger");
            Assertions.assertEquals(4, m.invoke(o, catF));
        } catch (IllegalAccessException e) {
            System.out.println("Disallowed, but OK");
        } catch (ReflectiveOperationException e){
            Assertions.fail(e);
        }

    }

    @Test
    void longhashCodeCollisionTest(){
        for (int i = -10; i <= 10; i++) {
            for (int j = -10; j <=10; j++) {
                Assertions.assertEquals(
                        Long.hashCode(i),
                        Long.hashCode(
                                hashCollisionGenerator(i,j)
                        ));
            }
        }

    }
    static long hashCollisionGenerator(final long value, final int seed) {
        final long longSeed = (seed & 0xFFFF_FFFFL);
        final long lsh = longSeed << 32;
        return lsh ^ value ^ longSeed;
    }

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
        System.out.printf("%x\n", a + 2);
    }

    @Test
    void ModulesIllegalAccess() {
        Assertions.assertThrows(IllegalAccessException.class, () -> accessInaccessible());

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
            System.out.println(ByteArrayUtils.toString(b));
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
            throw new AssertionError(e);
        }
    }

    @Test
    void reflections() {
        Class<Clazz> cactus = Clazz.class;
        System.out.println(cactus);
        System.out.println(Modifier.toString(cactus.getModifiers()));
        System.out.println();
        Field[] fields = cactus.getDeclaredFields();
        for (Field f : fields) {
            System.out.println(f.getName() + " is " + f.getType());
        }
    }

    @Test
    void accessHandle() {
        var mt = MethodType.methodType(void.class, String.class);
        var lookup = MethodHandles.lookup();
        System.out.println(getModifiers(lookup.lookupModes()));
        try {
            var mhs = lookup.findStatic(Clazz.class, "sayStatic", mt);
            var mhd = lookup.findVirtual(Clazz.class, "sayDynamic", mt);
            var msp = Clazz.class.getDeclaredMethod("privSayStatic", String.class);
            msp.setAccessible(true);
           // var mhsp = lookup.unreflect(msp);
         //   var getter = lookup.findGetter(Clazz.class, "sInteger", int.class);
            var varp = Clazz.class.getDeclaredField("sInteger");
            for (Method m : Clazz.class.getDeclaredMethods()) {
                System.out.println(m.getName());
            }
            varp.setAccessible(true);
            // var varph = lookup.unreflectVarHandle(varp);
          //  mhsp.invoke("hello");
            mhs.invoke("hello");
            mhd.invoke(new Clazz(), "hola");
            //   System.out.println(varph.get(new Clazz()));
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    public static void modulesTest() {
        String FLOAT_CONSTANTS_CLASS_NAME = "jdk.internal.math.FloatConsts";
        String THREAD_SLEEPING_EVENT_CLASS_NAME = "jdk.internal.event.ThreadSleepEvent";
        System.out.println(Info.getVersion());
        ModuleManager.poke();
        ModuleManager.poke();
        System.out.println(Main.class.getModule());
        System.out.println(ModuleManager.class.getModule());
        try {
            Class<?> cl = Class.forName(FLOAT_CONSTANTS_CLASS_NAME);
            ModuleManager.allowAccess(Main.class, FLOAT_CONSTANTS_CLASS_NAME, false);
            Field field = cl.getDeclaredField("SIGNIFICAND_WIDTH");
            System.out.println(field.get(null));
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
    public static void communism() {
        try {
            Modules.exportAllToAll();
        } catch (Throwable exc) {
            throw new RuntimeException(exc);
        }
    }

    public static void stuckOverflow(int counter) {
        counter++;
        try {
            stuckOverflow(counter);
        } catch (StackOverflowError e) {
            System.out.println("End is " + counter);
        }
    }

}
