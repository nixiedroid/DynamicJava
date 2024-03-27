package unsafe;

import com.nixiedroid.unsafe.type.Pointer;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class PointerTest {

    @Test
    void address() {
        Pointer p = new Pointer(20);
        assertEquals(20,p.address());
        assertThrows(NullPointerException.class,()->new Pointer(0));
        assertDoesNotThrow(()->new Pointer(-1));
    }

    @Test
    void validate() {
        Pointer p = Pointer.ZERO_POINTER;
        assertThrows(IllegalArgumentException.class,()->Pointer.validate(p));
        assertThrows(IllegalArgumentException.class,()-> Pointer.validate(null));
        assertThrows(NullPointerException.class,()-> Pointer.validate(new Pointer(0)));
    }

    @Test
    void testEquals() {
        Pointer first = new Pointer(42);
        Pointer second = new Pointer(42);
        Pointer notEquals = new Pointer(43);
        assertNotEquals(first, second);
        assertNotEquals(first, notEquals);
        assertNotEquals(first,null);
        assertNotEquals(0x1234_5678_9ABC_D0EFL,0x9ABC_D0EF_1234_5678L);
        assertNotEquals(new Pointer(0x1234_5678_9ABC_D0EFL),new Pointer(0x9ABC_D0EF_1234_5678L));
        assertNotEquals(first,new Pointer(42));
    }

    @Test
    void testHashCode() {
        assertNotEquals(new Pointer(1).hashCode(),new Pointer(1).hashCode());
        assertNotEquals(new Pointer(0x1234_5678_9ABC_D0EFL).hashCode(),new Pointer(0x9ABC_D0EF_1234_5678L).hashCode());
        Random random = new Random();
        int halfFirst = random.nextInt();
        int halfSecond = random.nextInt();
        long firstAddress = halfFirst + ((long) halfSecond << 32);
        long collidingAddress = halfSecond+ ((long) halfFirst << 32);
        assertNotEquals(firstAddress,collidingAddress);
        assertNotEquals(new Pointer(firstAddress).hashCode(),new Pointer(collidingAddress).hashCode());
    }
}