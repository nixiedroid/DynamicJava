package records;

import com.nixiedroid.records.Book;
import com.nixiedroid.records.BookCopy;
import com.nixiedroid.records.LongContainer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Set;

public class RecordsTest {
    @Test
    void equalsTest() {
        Book book = new Book(318, "1984");
        Book book2 = new Book(318, "1984");
        Book book3 = new Book(324, "419");
        Assertions.assertEquals(book, book2);
        Assertions.assertNotEquals(book, book3);
    }
    @Test
    void constructorTest(){
        Assertions.assertThrows(IllegalArgumentException.class,()->new Book(-1,""));
    }

    @Test
    void toStringTest(){
        Book book = new Book(318, "1984");
        Assertions.assertEquals("Book[pages=318, name=1984]",book.toString());
    }

    @Test
    void serialisationTest(){
        Book book = new Book(318, "1984");
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(book);
            Assertions.assertEquals(95,baos.size());
            oos.close();
            baos.close();
        }catch(IOException e){
            e.printStackTrace();
            Assertions.fail();
        }
    }
    @Test
    void hashcode() {
        Set<Book> books = new HashSet<>();
        Book book = new Book(318, "1984");
        Book book2 = new Book(318, "1984");
        Book book3 = new Book(324, "419");
        books.add(book);
        Assertions.assertEquals(1,books.size());
        books.add(book2);
        Assertions.assertEquals(1,books.size());
        books.add(book3);
        Assertions.assertEquals(2,books.size());
    }

    @Test
    void hashcodeCollide(){
        LongContainer c,b;
        for (int i = -10; i <= 10; i++) {
            c = new LongContainer(i);
            for (int j = -10; j <=10; j++) {
                b = new LongContainer(hashCollisionGenerator(i,j));
                Assertions.assertEquals(c.hashCode(),b.hashCode());
            }
        }
    }
    static long hashCollisionGenerator(final long value, final int seed) {
        final long longSeed = (seed & 0xFFFF_FFFFL);
        final long lsh = longSeed << 32;
        return lsh ^ value ^ longSeed;
    }
}
