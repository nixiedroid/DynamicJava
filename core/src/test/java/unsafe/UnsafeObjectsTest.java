package unsafe;

import com.nixiedroid.unsafe.UnsafeWrapper;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import unsafe.samples.TestClass;
import static org.junit.jupiter.api.Assertions.*;


class UnsafeClassInstantiationTest {
    private  void createUtilityObjectReflection() {
        try {
            Constructor<UnsafeWrapper> constructor = ( UnsafeWrapper.class).getDeclaredConstructor();
            constructor.setAccessible(true);
            constructor.newInstance();
            constructor.setAccessible(false);
        } catch (InvocationTargetException e) {
            throw (UnsupportedOperationException) e.getTargetException();
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            fail("Should not have thrown any exception: \n " + e);
        }
    }
    @Test
    void constructor(){
        assertThrows(UnsupportedOperationException.class, this::createUtilityObjectReflection);
    }
    @Test
    public void createInstanceUsingDefaultConstructor() {
        TestClass cl = new TestClass(25, "Smith");
        assertNotNull(cl);
        assertEquals("Tom", cl.finalString);
        assertEquals("Smith", cl.unsetString);
        assertEquals(25, cl.unsetInteger);
        assertEquals(2, cl.finalInt);
        assertEquals(3, cl.preSetInteger);
        assertEquals(4, cl.preSetInteger2);
        assertTrue(cl.finalBoolean);
        assertTrue(cl.preSetBoolean);
        assertEquals(96, TestClass.staticInt);
        assertEquals(34, cl.getSum());
    }

    @Test
    public void createInstanceUsingReflection() {
        TestClass cl = null;
        try {
            Constructor<TestClass> constructor = TestClass.class.getDeclaredConstructor(int.class, String.class);
            cl = constructor.newInstance(25, "Smith");
        } catch (Exception e) {
            fail("Should not have thrown any exception: \n " + e);
        }
        assertNotNull(cl);
        assertEquals("Tom", cl.finalString);
        assertEquals("Smith", cl.unsetString);
        assertEquals(25, cl.unsetInteger);
        assertEquals(2, cl.finalInt);
        assertEquals(3, cl.preSetInteger);
        assertEquals(4, cl.preSetInteger2);
        assertTrue(cl.finalBoolean);
        assertTrue(cl.preSetBoolean);
        assertEquals(96, TestClass.staticInt);
        assertEquals(34, cl.getSum());
    }



    @Test
    public void createInstanceWithoutConstructor() {
        TestClass cl = null;
        try {
            cl = UnsafeWrapper.createDummyInstance(TestClass.class);
        } catch (Exception e) {
            fail("Should not have thrown any exception : \n " + e);
        }
        assertNotNull(cl);
        assertEquals("Tom", cl.finalString); //THIS
        assertNull(cl.unsetString); //
        assertEquals(0, cl.unsetInteger);
        assertEquals(2, cl.finalInt);  //THIS
        assertEquals(0, cl.preSetInteger);
        assertEquals(0, cl.preSetInteger2);
        assertTrue(cl.finalBoolean);  //THIS
        assertFalse(cl.preSetBoolean);
        assertEquals(96, TestClass.staticInt); //THIS. Obviously
        assertEquals(2, cl.getSum());
    }

}
