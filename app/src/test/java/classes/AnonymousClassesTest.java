package classes;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;

public  class AnonymousClassesTest {
    @Test
    void TestUnsafe(){
        try {
            String smU = "sun.misc.Unsafe";
            String jimU = "jdk.internal.misc.Unsafe";
            Class<?>  unsafeClass = Class.forName(smU);
            Class<?>  theInternalUnsafeClass = Class.forName(jimU);
            MethodHandles.Lookup  l = MethodHandles.privateLookupIn(unsafeClass,MethodHandles.lookup());
            MethodHandle getU = l.findStaticGetter(unsafeClass,"theUnsafe", unsafeClass);
            MethodHandle getInternalUnsafe = l.findStaticGetter(unsafeClass,"theInternalUnsafe", theInternalUnsafeClass);
            Object U = getU.invoke();
            Assertions.assertEquals(smU,U.getClass().getName());
            Object iU = getInternalUnsafe.invoke();
            Assertions.assertEquals(jimU,iU.getClass().getName());
            //l.defineClass()
            System.out.println(l.hasFullPrivilegeAccess());
            //l.defineHiddenClass();
        } catch (Throwable e) {
            Assertions.fail("Sould not have thown any excetion");
        }
    }

}
class Sample{
    int a;
}

