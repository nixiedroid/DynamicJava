import com.nixiedroid.Main;
import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.classloaders.parser.JavaClassParser;
import com.nixiedroid.modules.ModuleManager;
import com.nixiedroid.modules.ModuleManager2;
import com.nixiedroid.modules.util.Modules;
import com.nixiedroid.runtime.Info;
import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import samples.Cats;
import samples.Clazz;
import samples.MHtestObj;

import java.io.*;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;

public class CoolStuffTest {

    @Test
    void ModulesIllegalAccess() {
        Assertions.assertThrows(IllegalAccessException.class, () -> {
            Class<?> cl = Class.forName("jdk.internal.misc.VM");
            Method m = cl.getMethod("initLevel");
            int dat = (int) m.invoke(null);
            System.out.println(dat);
        });
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

    static long hashCollisionGenerator(final long value, final int seed) {
        final long longSeed = (seed & 0xFFFF_FFFFL);
        final long lsh = longSeed << 32;
        return lsh ^ value ^ longSeed;
    }
    @Test
    public void modulesTest() {
        String FLOAT_CONSTANTS_CLASS_NAME = "jdk.internal.math.FloatConsts";
        System.out.println(Info.getVersion());
        ModuleManager.poke();
        ModuleManager.poke();
        System.out.println(Main.class.getModule());
        System.out.println(ModuleManager.class.getModule());
        try {
            Class<?> cl = Class.forName(FLOAT_CONSTANTS_CLASS_NAME);
            //   ModuleManager.allowAccess(Main.class, FLOAT_CONSTANTS_CLASS_NAME, false);
            Field field = cl.getDeclaredField("SIGNIFICAND_WIDTH");
            System.out.println(field.get(null));
        } catch (ReflectiveOperationException e) {
            Assertions.fail("Should not have thrown any exception");
        }
    }
    @Test
    public void communism() {
        try {
            Modules.exportAllToAll();
        } catch (Throwable exc) {
            Assertions.fail("Should not have thrown any exception");
        }
    }
    @SuppressWarnings("unused")
    public static void stuckOverflow(int counter) {
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
        Assertions.assertThrows(e.getClass(), () -> new ExceptionMuffler().accept(e));
    }

    static class ExceptionMuffler implements Consumer<Throwable> {
        @SuppressWarnings("unchecked")
        private  <E extends Throwable> void quietlyThrow(Throwable e) throws E {
            if (e != null) throw (E) e;
        }

        @Override
        public void accept(Throwable throwable) {
            quietlyThrow(throwable);
        }
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
            String s = e.getKey() + "\t" + String.join("\t", e.getValue());
            System.out.println(s);
        }
    }

    @Test
    @SuppressWarnings("internal")
    void internalUnsafeTest() throws Throwable {
        Class<?> unsafeClass = sun.misc.Unsafe.class;
        Class<?> intUnsafeClass = Class.forName("jdk.internal.misc.Unsafe");
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        lookup = MethodHandles.privateLookupIn(unsafeClass, lookup);
        //jdk.internal.misc.Unsafe
        Object intUnsafe;
        Clazz cat = new Clazz();
        try {
            MethodHandle getter = lookup.findStaticGetter(unsafeClass, "theInternalUnsafe", intUnsafeClass);
            intUnsafe = getter.invoke();
            MethodHandle getInt = lookup.findVirtual(intUnsafeClass, "getInt", MethodType.methodType(int.class, Object.class, long.class));
            MethodHandle oFieldOffset = lookup.findVirtual(intUnsafeClass, "objectFieldOffset", MethodType.methodType(long.class, Field.class));
            Field catF = Clazz.class.getDeclaredField("sInteger");
            Assertions.assertEquals(4, getInt.invoke(intUnsafe, cat, oFieldOffset.invoke(intUnsafe, catF)));
        } catch (IllegalAccessException e) {
            System.out.println("Disallowed, but OK");
        } catch (ReflectiveOperationException e) {
            Assertions.fail(e);
        }

    }

    @Test
    void longhashCodeCollisionTest() {
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

    @Test
    void getterAndSetter() throws Throwable {
        Class<?> clazz = MHtestObj.class;

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodHandles.Lookup privLookup = MethodHandles.privateLookupIn(clazz, lookup);
        Object obj = UnsafeWrapper.getUnsafe().allocateInstance(clazz);
        //lookup.findConstructor(clazz,MethodType.methodType(void.class)).invoke();
        MethodHandle GpIntHandle = privLookup.findGetter(clazz, "pInt", int.class);
        MethodHandle GprivIntHandle = privLookup.findGetter(clazz, "prInt", int.class);
        MethodHandle SpIntHandle = privLookup.findSetter(clazz, "pInt", int.class);
        MethodHandle SprivIntHandle = privLookup.findSetter(clazz, "prInt", int.class);
        System.out.println(GpIntHandle.invoke(obj));
        System.out.println(GprivIntHandle.invoke(obj));
        SprivIntHandle.invoke(obj, 11);
        SpIntHandle.invoke(obj, 321);
        System.out.println(GpIntHandle.invoke(obj));
        System.out.println(GprivIntHandle.invoke(obj));
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
        System.out.printf("%x", a + 2);
    }

    String blob = "CAFEBABE0000003400110A0004000D08000E07000F0700100100063C696E6974" +
            "3E010003282956010004436F646501000F4C696E654E756D6265725461626C65" +
            "01000367657401001428294C6A6176612F6C616E672F537472696E673B01000A" +
            "536F7572636546696C6501000B53616D706C652E6A6176610C0005000601000F" +
            "48656C6C6F2066726F6D20424C4F42010001480100106A6176612F6C616E672F" +
            "4F626A656374002000030004000000000002000000050006000100070000001D" +
            "00010001000000052AB70001B100000001000800000006000100000001000100" +
            "09000A000100070000001B00010001000000031202B000000001000800000006" +
            "0001000000030001000B00000002000C";
    @Test
    void testHiddenClass() throws Throwable{
        byte[] cb = ByteArrayUtils.fromHexString(this.blob);
        Assertions.assertEquals(this.blob,ByteArrayUtils.toString(cb));
        JavaClassParser.ClassInfo ci =  JavaClassParser.create(cb);
        Assertions.assertEquals("H",ci.getName());
        MethodHandles.Lookup lookup = MethodHandles.lookup().defineHiddenClass(cb,false);
        Class<?> hiddenClass = lookup.lookupClass();
        MethodHandle get = lookup.findVirtual(hiddenClass,"get",MethodType.methodType(String.class));
        Object hello = hiddenClass.getDeclaredConstructor().newInstance();
        Assertions.assertEquals("Hello from BLOB",get.invoke(hello));
    }

    @Test
    void initRawClass() throws Throwable {
        MethodHandles.Lookup l = new ModuleManager2().getBrokenLookup();
        MethodHandle cons = l.findConstructor(Class.class, MethodType.methodType(void.class, ClassLoader.class, Class.class));
        Assertions.assertThrows(IllegalAccessException.class, () -> cons.invoke(null,null));
        Class<?> unsafeClass = sun.misc.Unsafe.class;
        Class<?> intUnsafeClass = Class.forName("jdk.internal.misc.Unsafe");
        //jdk.internal.misc.Unsafe
        Object intUnsafe;
        try {
            MethodHandle getter = l.findStaticGetter(unsafeClass, "theInternalUnsafe", intUnsafeClass);
            intUnsafe = getter.invoke();
            MethodHandle allocateInstance = l.findVirtual(intUnsafeClass, "allocateInstance", MethodType.methodType(Object.class, Class.class));
            Assertions.assertThrows(IllegalAccessException.class, () -> allocateInstance.invoke(intUnsafe,Class.class));
        } catch (ReflectiveOperationException e) {
            Assertions.fail(e);
        }


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
            Assertions.fail("Should not have thrown any exception");
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
            Assertions.fail("Should not have thrown any exception");
        }
    }

}
