package invoke;

import com.nixiedroid.interfaces.ThrowableBiFunction;
import com.nixiedroid.interfaces.ThrowableTerConsumer;
import com.nixiedroid.modules.ModuleManager2;
import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import samples.Clazz;
import samples.MHtestObj;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodHandlesTest {

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
        final int ELEVEN = 11;
        SprivIntHandle.invoke(obj, ELEVEN);
        final int THREE_TWO_ONE = 321;
        SpIntHandle.invoke(obj, THREE_TWO_ONE);
        System.out.println(GpIntHandle.invoke(obj));
        System.out.println(GprivIntHandle.invoke(obj));
    }


    @Test
    void initRawClass() throws Throwable {
        MethodHandles.Lookup l = new ModuleManager2().getBrokenLookup();
        MethodHandle cons = l.findConstructor(Class.class, MethodType.methodType(void.class, ClassLoader.class, Class.class));
        Assertions.assertThrows(IllegalAccessException.class, () -> cons.invoke(null, null));
        Class<?> unsafeClass = sun.misc.Unsafe.class;
        Class<?> intUnsafeClass = Class.forName("jdk.internal.misc.Unsafe");
        //jdk.internal.misc.Unsafe
        Object intUnsafe;
        try {
            MethodHandle getter = l.findStaticGetter(unsafeClass, "theInternalUnsafe", intUnsafeClass);
            intUnsafe = getter.invoke();
            MethodHandle allocateInstance = l.findVirtual(intUnsafeClass, "allocateInstance", MethodType.methodType(Object.class, Class.class));
            Assertions.assertThrows(IllegalAccessException.class, () -> allocateInstance.invoke(intUnsafe, Class.class));
        } catch (ReflectiveOperationException e) {
            Assertions.fail(e);
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
            var varp = Clazz.class.getDeclaredField("sInteger");
            for (Method m : Clazz.class.getDeclaredMethods()) {
                System.out.println(m.getName());
            }
            varp.setAccessible(true);
            mhs.invoke("hello");
            mhd.invoke(new Clazz(), "hola");
            //   System.out.println(varph.get(new Clazz()));
        } catch (Throwable e) {
            Assertions.fail("Should not have thrown any exception");
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
    ThrowableBiFunction<Class<?>, String, Long> objectFieldOffset() throws Throwable {
        final int TRUSTED = -1;

        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        boolean is64Bit = sunArchDataModel.contains("64");
        final long allowedModesFieldMemoryOffset = is64Bit ? 12L : 8L;

        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> Uclass = cl.loadClass("sun.misc.Unsafe");
        Class<?> intUnsafeClass = cl.loadClass("jdk.internal.misc.Unsafe");

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        lookup = MethodHandles.privateLookupIn(Uclass, lookup);
        Object U = lookup.findStaticGetter(Uclass, "theUnsafe", Uclass).invoke();

        MethodType putIntType = MethodType.methodType(void.class, Object.class, long.class, int.class);
        MethodHandle putInt = lookup.findVirtual(Uclass, "putInt", putIntType);
        putInt.invoke(U, lookup, allowedModesFieldMemoryOffset, TRUSTED);

        Object iU = lookup.findStaticGetter(Uclass, "theInternalUnsafe", intUnsafeClass).invoke();

        MethodType oFieldOffsetType = MethodType.methodType(long.class, Class.class, String.class);
        MethodHandle oFieldOffset = lookup.findVirtual(intUnsafeClass, "objectFieldOffset", oFieldOffsetType);
        return (c, s) -> (Long) oFieldOffset.invoke(iU, c, s);
    }

    ThrowableBiFunction<Object, Long, Integer> getInt() throws Throwable {
        final int TRUSTED = -1;

        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        boolean is64Bit = sunArchDataModel.contains("64");
        final long allowedModesFieldMemoryOffset = is64Bit ? 12L : 8L;

        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> Uclass = cl.loadClass("sun.misc.Unsafe");
        Class<?> intUnsafeClass = cl.loadClass("jdk.internal.misc.Unsafe");

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        lookup = MethodHandles.privateLookupIn(Uclass, lookup);
        Object U = lookup.findStaticGetter(Uclass, "theUnsafe", Uclass).invoke();

        MethodType putIntType = MethodType.methodType(void.class, Object.class, long.class, int.class);
        MethodHandle putInt = lookup.findVirtual(Uclass, "putInt", putIntType);
        putInt.invoke(U, lookup, allowedModesFieldMemoryOffset, TRUSTED);

        Object iU = lookup.findStaticGetter(Uclass, "theInternalUnsafe", intUnsafeClass).invoke();

        MethodType getIntType = MethodType.methodType(int.class, Object.class, long.class);
        MethodHandle getInt = lookup.findVirtual(intUnsafeClass, "getInt", getIntType);
        return (o, offset) -> (Integer) getInt.invoke(iU, o, offset);
    }

    ThrowableTerConsumer<Object, Long, Integer> putInt() throws Throwable {
        final int TRUSTED = -1;

        String sunArchDataModel = System.getProperty("sun.arch.data.model");
        boolean is64Bit = sunArchDataModel.contains("64");
        final long allowedModesFieldMemoryOffset = is64Bit ? 12L : 8L;

        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Class<?> Uclass = cl.loadClass("sun.misc.Unsafe");
        Class<?> intUnsafeClass = cl.loadClass("jdk.internal.misc.Unsafe");

        MethodHandles.Lookup lookup = MethodHandles.lookup();
        lookup = MethodHandles.privateLookupIn(Uclass, lookup);
        Object U = lookup.findStaticGetter(Uclass, "theUnsafe", Uclass).invoke();

        MethodType putIntType = MethodType.methodType(void.class, Object.class, long.class, int.class);
        MethodHandle putInt = lookup.findVirtual(Uclass, "putInt", putIntType);
        putInt.invoke(U, lookup, allowedModesFieldMemoryOffset, TRUSTED);

        Object iU = lookup.findStaticGetter(Uclass, "theInternalUnsafe", intUnsafeClass).invoke();

        MethodHandle putIntInternal = lookup.findVirtual(intUnsafeClass, "putInt", putIntType);

        return (o, offset, x) -> putIntInternal.invoke(iU, o, offset, x);
    }
}
