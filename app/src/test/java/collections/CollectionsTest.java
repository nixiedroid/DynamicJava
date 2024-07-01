package collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;


public class CollectionsTest {

    @Test
    void Test() {
        Set<Integer> exp = new LinkedHashSet<>(List.of(1,2,3,4,5,6,7,8,9));
        final Set<Integer> ints = new TreeSet<>(List.of(4, 5, 1, 6, 7, 9, 8, 5, 7, 3, 4, 2, 9));
        Assertions.assertEquals(ints,exp);
        Assertions.assertEquals(ints.size(),exp.size());
        Iterator<Integer> it1 = exp.iterator(),it2 = ints.iterator();
        while (it1.hasNext()){
            Assertions.assertEquals(it1.next(),it2.next());
        }

        Set<Integer> reverseInts = new TreeSet<>((i1, i2) -> i2-i1);
        reverseInts.addAll(List.of(4, 5, 1, 6, 7, 9, 8, 5, 7, 3, 4, 2, 9));
        exp = new LinkedHashSet<>(List.of(9,8,7,6,5,4,3,2,1));
        Assertions.assertEquals(reverseInts,exp);
        Assertions.assertEquals(reverseInts.size(),exp.size());
        it1 = exp.iterator();
        it2 = reverseInts.iterator();
        while (it1.hasNext()){
            Assertions.assertEquals(it1.next(),it2.next());
        }

        List<Integer> vec = new Vector<>(List.of(4, 5, 4, 7));
        Assertions.assertEquals(vec,List.of(4,5,4,7));
        Stack<Integer> stack = new Stack<>();
        Assertions.assertThrows(EmptyStackException.class, stack::peek);
        stack.push(5);
        stack.push(6);
        Assertions.assertEquals(6,stack.pop());
        Assertions.assertEquals(5,stack.pop());

        Deque<Integer> deque = new ArrayDeque<>();
        deque.push(5);
        deque.push(6);
        Assertions.assertEquals(6,deque.peek());
        Assertions.assertEquals(6,deque.peekFirst());
        Assertions.assertEquals(5,deque.peekLast());

        Set<Integer> lhs = new LinkedHashSet<>(List.of(4, 5, 1, 6, 7, 9, 8, 5, 7, 3, 4, 2, 9));
        exp = new LinkedHashSet<>(List.of(4,5,1,6,7,9,8,3,2));
        Assertions.assertEquals(lhs,exp);
        Assertions.assertEquals(lhs.size(),exp.size());
        it1 = exp.iterator();
        it2 = lhs.iterator();
        while (it1.hasNext()){
            Assertions.assertEquals(it1.next(),it2.next());
        }

    }
}

