package access;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import samples.Clazz;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.InaccessibleObjectException;

public class IllegalAcessTest {
    @Test
    void ModulesIllegalAccess() {
        Assertions.assertThrows(InaccessibleObjectException.class, () -> {
            ClassLoader cl = ClassLoader.getSystemClassLoader();
            Class<?> clazz = cl.loadClass("sun.launcher.LauncherHelper");
            Field f = clazz.getDeclaredField("MAIN_CLASS");
            f.setAccessible(true);
            String str = (String) f.get(null);
            System.out.println(str);
        });
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
}
