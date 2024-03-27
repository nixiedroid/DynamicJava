package unsafe;

import com.nixiedroid.unsafe.Util;
import org.junit.jupiter.api.Test;

import java.lang.ref.WeakReference;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

class UtilTest {

    @SuppressWarnings("ConstantValue")
    @Test
    void performGc() {
        Object o = new Object();
        WeakReference<Object> ref = new WeakReference<>(o);
        o = null;
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