package com.nixiedroid;

import com.nixiedroid.bytes.ByteArrayUtils;
import com.nixiedroid.classloaders.CryptedClassLoader;
import com.nixiedroid.classloaders.DummyClassLoader;
import com.nixiedroid.classloaders.FileClassLoader;
import com.nixiedroid.modules.ModuleManager;
import com.nixiedroid.modules.util.Modules;
import com.nixiedroid.premain.Handler;
import com.nixiedroid.runtime.Info;
import com.nixiedroid.samples.Cats;
import com.nixiedroid.samples.Clazz;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class Stuff {
    public static void classLoaderLazyDemo() {
        FileClassLoader cl = new FileClassLoader("resources", "clazz") {
            @Override
            public void loadClassInputLogging(String string) {
                System.out.println("Loading " + string);
            }
        };
        Class<?> cla;
        try {
            cla = cl.loadClass("resources.Cat");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        System.out.println("-----------");
        try {
            cla.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    public static void printAll() throws Exception {
        print("\nMain OK");
        Handler.listAllLoadedAppClasses();
        new DummyClassLoader().loadClass("com.nixiedroid.plugins.Plugin").getConstructor().newInstance();
        Handler.listAllLoadedAppClasses();
        print("");
        print("ClassLoader of Classloader is " + java.lang.ClassLoader.class.getClassLoader());

        print("Num of App loaded packages: " + ClassLoader.getSystemClassLoader().getDefinedPackages().length);
        print("Platform loaded packages:" + Arrays.toString(ClassLoader.getPlatformClassLoader().getDefinedPackages()));
        print("ClassLoader of java.sql.Array is " + java.sql.Array.class.getClassLoader().getName());
        print("Platform loaded packages:" + Arrays.toString(ClassLoader.getPlatformClassLoader().getDefinedPackages()));
        // epicText();
    }
    public static void epicText() {
        ModuleLayer.boot().modules().stream().collect(Collectors.groupingBy(m -> Optional.ofNullable(m.getClassLoader()).map(ClassLoader::getName).orElse("boot"), Collectors.mapping(Module::getName, Collectors.toCollection(TreeSet::new)))).entrySet().stream().sorted(Comparator.comparingInt(e -> List.of("boot", "platform", "app").indexOf(e.getKey()))).map(e -> e.getKey() + "\n\t" + String.join("\n\t", e.getValue())).forEach(Stuff::print);
    }
    public static void printSystemPaths() {
        print(System.getProperty("java.class.path"));
        print(System.getProperty("jdk.module.path"));
    }
    public static void print(String str) {
        System.out.println(str);
    }
    public static void loadPlugin() {
        String name = "com.nixiedroid.plugins.Plugin";
        ClassLoader classLoader = new CryptedClassLoader("com.nixiedroid.plugins", "enc");

        try {
            Class<?> cl = classLoader.loadClass(name);
            Object plugin = cl.getDeclaredConstructor().newInstance();
            print("ClassLoader of plugin is " + cl.getClassLoader().getName());
        } catch (Exception ignored) {
        }

    }
    public static void encrypt() {
        File rawFile = new File("./Plugin.clazz");
        File encFile = new File("./Plugin.enc");
        byte[] data = null;
        try {
            data = CryptedClassLoader.encrypt(Files.readAllBytes(rawFile.toPath()));
            Files.write(encFile.toPath(), data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static void modulesTest() {
        String FLOAT_CONSTANTS_CLASS_NAME = "jdk.internal.math.FloatConsts";
        String THREAD_SLEEPING_EVENT_CLASS_NAME = "jdk.internal.event.ThreadSleepEvent";
        System.out.println(Info.getVersion());
        ModuleManager.poke();
        System.out.println(Main.class.getModule());
        System.out.println(ModuleManager.class.getModule());
        try {
            Class<?> cl = Class.forName(FLOAT_CONSTANTS_CLASS_NAME);
            System.out.println(cl.getModule());
            ModuleManager.allowAccess(Main.class, cl, false);
            Field field = cl.getDeclaredField("SIGNIFICAND_WIDTH");
            System.out.println(field.get(null));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void communism() {
        try {
            Modules modules = new Modules();
            modules.exportAllToAll();
        } catch (Throwable exc) {
            throw new RuntimeException(exc);
        }
    }
    public static void accessInaccessible() {
        try {
            Class<?> cl = Class.forName("jdk.internal.misc.VM");
            Method m = cl.getMethod("initLevel");
            int dat = (int) m.invoke(null);
            System.out.println(dat);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public static void countTo655536() {
        short i = 0;
        char k = 0;
        for (int j = 0; j < 10; j++) {
            System.out.print((i += 16384) + " "); //short is signed
            System.out.println((int) (k += 16384)); //char is unsigned
        }
    }
    public static void everyThingIsInt(){
        byte a = (byte) 130;
        System.out.printf("%x\n",a+2);
    }
    public static void aNEQa() {
        System.out.println("is " + Float.NaN + " equals to " + Float.NaN + " ?: " + (Float.NaN == Float.NaN));
    }
    public static void stuckOverflow(int counter) {
        counter++;
        try {
            stuckOverflow(counter);
        } catch (StackOverflowError e) {
            System.out.println("End is " + counter);
        }
    }
    public static void testByteArray() {
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

            assert (3 == h2.getA());
            assert (4 == h2.getB());
        } catch (IOException | ClassNotFoundException e) {
            throw new AssertionError(e);
        }
    }
    public static void internalUnsafeTest(){
        sun.misc.Unsafe unsafe = com.nixiedroid.unsafe.UnsafeWrapper.getUnsafe();
        //jdk.internal.misc.Unsafe
        Object o;
        Clazz cat  = new Clazz();
        try {
            Field f = unsafe.getClass().getDeclaredField("theInternalUnsafe");
            f.setAccessible(true);
            o = f.get(null);
            Method m = o.getClass().getMethod("objectFieldOffset",Field.class);
            Field catF = Clazz.class.getDeclaredField("sInteger");
            System.out.println(m.invoke(o,catF));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void reflections(){
        Class<Clazz> cactus = Clazz.class;
        System.out.println(cactus);
        System.out.println(Modifier.toString(cactus.getModifiers()));
        System.out.println(cactus.accessFlags());
        System.out.println();
        Field[] fields = cactus.getDeclaredFields();
        for (Field f: fields) {
            System.out.println(f.getName() + " is " + f.getType());
        }
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
    public static void accessHandle() {
        var mt = MethodType.methodType(void.class, String.class);
        var lookup = MethodHandles.lookup();
        System.out.println(getModifiers(lookup.lookupModes()));
        try {
            var mhs = lookup.findStatic(Clazz.class, "sayStatic", mt);
            var mhd = lookup.findVirtual(Clazz.class, "sayDynamic", mt);

            var msp = Clazz.class.getDeclaredMethod("privSayStatic", String.class);
            msp.setAccessible(true);
            var mhsp = lookup.unreflect(msp);


            var getter = lookup.findGetter(Clazz.class, "sInteger", int.class);
            var varp = Clazz.class.getDeclaredField("sInteger");
            for (Method m : Clazz.class.getDeclaredMethods()) {
                System.out.println(m.getName());
            }
            varp.setAccessible(true);
            // var varph = lookup.unreflectVarHandle(varp);


            mhsp.invoke("hello");
            mhs.invoke("hello");
            mhd.invoke(new Clazz(), "hola");
            //   System.out.println(varph.get(new Clazz()));
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

    }
}
