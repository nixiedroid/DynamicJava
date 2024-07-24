package unsafe;

import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Test;
import sun.misc.Unsafe;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import static org.junit.jupiter.api.Assertions.*;

class UnsafeTest {
    @Test
    void unsafeInstanceTest() {
        sun.misc.Unsafe unsafe = null;
        assertThrowsExactly(SecurityException.class, sun.misc.Unsafe::getUnsafe);
        ClassLoader cl = ClassLoader.getSystemClassLoader();
        try {
            Class<?> Uclass = cl.loadClass("sun.misc.Unsafe");
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(Uclass, MethodHandles.lookup());
            MethodHandle mh = lookup.findStaticGetter(Uclass, "theUnsafe", Uclass);
            try {
                unsafe = (Unsafe) mh.invoke();
            } catch (Throwable e) {
                fail("Should not have thrown any exception: \n " + e);
            }
        } catch (ReflectiveOperationException e) {
            fail("Should not have thrown any exception: \n " + e);
        }
        assertNotNull(unsafe);
        unsafe = null;
        try {
            Class<?> Uclass = cl.loadClass("sun.misc.Unsafe");
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(Uclass, MethodHandles.lookup());
            MethodHandle mh = lookup.findConstructor(Uclass, MethodType.methodType(void.class));
            try {
                unsafe = (Unsafe) mh.invoke();
            } catch (Throwable e) {
                fail("Should not have thrown any exception: \n " + e);
            }
        } catch (ReflectiveOperationException e) {
            fail("Should not have thrown any exception: \n " + e);
        }
        assertNotNull(unsafe);
    }

    @Test
    void throwException() {
        assertThrowsExactly(IllegalArgumentException.class,
                () -> UnsafeWrapper.getUnsafe().throwException(new IllegalArgumentException()));
        assertThrowsExactly(RuntimeException.class,
                () -> UnsafeWrapper.getUnsafe().throwException(new RuntimeException()));
        assertThrowsExactly(Exception.class,
                () -> UnsafeWrapper.getUnsafe().throwException(new Exception()));
        assertThrowsExactly(Error.class,
                () -> UnsafeWrapper.getUnsafe().throwException(new Error()));
        assertThrowsExactly(Throwable.class,
                () -> UnsafeWrapper.getUnsafe().throwException(new Throwable()));
        assertThrowsExactly(IOException.class,
                () -> UnsafeWrapper.getUnsafe().throwException(new IOException()));
    }

    @Test
    void crashVM() {
        System.out.println("Lets pretend, JVM Successfully crashed");
        assertTrue(true);
        //Unsafe.crashVM();
    }

}
