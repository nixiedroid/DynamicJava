package com.nixiedroid;

import com.nixiedroid.runtime.Info;
import com.nixiedroid.samples.Clazz;
import com.nixiedroid.unsafe.UnsafeWrapper;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;

public class Main {
    private final Object internalUnsafe;
    private final sun.misc.Unsafe unsafe;
    private final MethodHandle objectFieldOffset;
    private final MethodHandles.Lookup brokenLookup;
    private final MethodHandle forname0;
    private final MethodHandle getDeclaredFields0;
    private final Class<?> moduleClass;
    private final Map<String, ?> nameToModule;

    public Main() throws Throwable {
        brokenLookup = thisWillBreakSoon();
        forname0 = getForname0();
        unsafe = UnsafeWrapper.getUnsafe();
        internalUnsafe = getInternalUnsafe();
        objectFieldOffset = getFieldOffset();
        moduleClass = forname0("java.lang.Module");
        getDeclaredFields0 = brokenLookup.findSpecial(
                Class.class,
                "getDeclaredFields0",
                MethodType.methodType(Field[].class,boolean.class),
                Class.class);
        Class<?> moduleLayerClass = forname0("java.lang.ModuleLayer");
        var boot = ModuleLayer.boot();
        Field[] fields = (Field[]) getDeclaredFields0.invokeWithArguments(boot.getClass(),false);
        System.out.println(fields.length);
        Map<String, ?> temp = null;
        for (Field field : fields) {
            if (field.getName().equals("nameToModule")) {
                Long offset = (Long) objectFieldOffset.invoke(internalUnsafe,field);
                temp = (Map<String, ?>) unsafe.getObject(boot,offset);
                break;
            }
        }
        nameToModule = temp;
    }
    private Class<?> forname0(String name) throws Throwable {
        return (Class<?>) forname0.invoke(
                name,
                false,
                this.getClass().getClassLoader(),
                this.getClass());
    }

    public static void main(String[] args) throws Throwable {
        new Main();
    }

    private MethodHandles.Lookup thisWillBreakSoon() {
        int TRUSTED = -1;
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        try {
            Field modes = MethodHandles.Lookup.class.getDeclaredField("allowedModes");
        } catch (NoSuchFieldException e) {
            System.out.println("No such Field for java 8");
        }

        final long allowedModesFieldMemoryOffset = Info.isIs64Bit() ? 12L : 8L;
        UnsafeWrapper.getUnsafe().putInt(lookup, allowedModesFieldMemoryOffset, TRUSTED);
        return lookup;
    }

    private Object getInternalUnsafe() throws Throwable {
        Class<?> intUnsafeClass = forname0("jdk.internal.misc.Unsafe");
        return brokenLookup.findStaticVarHandle(
                sun.misc.Unsafe.class,
                "theInternalUnsafe",
                intUnsafeClass).get();
    }
    private MethodHandle getFieldOffset() throws Throwable {
        var mt = MethodType.methodType(long.class, Field.class);
        return brokenLookup.findVirtual(internalUnsafe.getClass(),
                        "objectFieldOffset",
                        mt);
    }

    private void unsafeCode() {
        int[] arr = new int[]{1};
        sun.misc.Unsafe unsafe = UnsafeWrapper.getUnsafe();
        // unsafe.arrayBaseOffset(arr.getClass());
        System.out.println(arr.getClass().getName());
        int base = unsafe.arrayBaseOffset(arr.getClass());
        System.out.println(base);


    }

    private MethodHandle getForname0() throws NoSuchMethodException, IllegalAccessException {
        var forName0mt = MethodType.methodType(Class.class, String.class, boolean.class, ClassLoader.class, Class.class);
        return brokenLookup.findStatic(Class.class, "forName0", forName0mt);
    }

    private void accessHandle() {
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

    private String getModifiers(int modifiers) {
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


}
