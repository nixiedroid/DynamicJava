package unsafe;

import com.nixiedroid.unsafe.Util;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;


class ClUtilTest {

    @Test
    void performGc() {
        Object o = new Object();
        WeakReference<Object> ref = new WeakReference<>(o);
        assertNotNull(o);
        assertNotNull(ref.get());
        o = null;
        assertNull(o);
        assertNotNull(ref.get());
        try {
            Method m = Util.class.getDeclaredMethod("performGcHalting");
            m.setAccessible(true);
            m.invoke(null);
            m.setAccessible(false);
        } catch (Exception e) {
            fail("Should not have thrown Exception");
        }
        assertNull(o);
        assertNull(ref.get());
    }

}