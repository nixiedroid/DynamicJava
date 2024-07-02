package classes;

import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class OffMemoryClassesTest {

    @Test
    void test() throws Throwable {
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        Unsafe U = UnsafeWrapper.getUnsafe();
        UnsafeWrapper.Hook hook = new UnsafeWrapper.Hook();
        Class<?> hookClass = hook.getClass();
        Class<?> intUnsafeClass = cl.loadClass("jdk.internal.misc.Unsafe");
        UnsafeWrapper.moveToJavaBase(hookClass);
        hook.whereAmI();
        MethodHandles.Lookup l = hook.getLookup(intUnsafeClass);
        System.out.println(l.lookupClass().getName());
        MethodType mt = MethodType.methodType(intUnsafeClass);
        MethodHandle mh = l.findStatic(intUnsafeClass,"getUnsafe",mt);
        Object o  = mh.invoke();
        mt = MethodType.methodType(long.class, Class.class, String.class);
        mh = l.findVirtual(intUnsafeClass,"objectFieldOffset",mt);
        Class<?> lClass = MethodHandles.Lookup.class;
        System.out.println(mh.invoke(o,lClass,"allowedModes"));
    }
}
