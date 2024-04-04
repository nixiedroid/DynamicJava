```java
public class Clazz {
    private Clazz() {
        //code
    }
    /**
     Static methods cannot be overridden but they can
     be hidden. The m() method of B is not overriding
     (not subject to polymorphism) the m() of A,
     but it will hide it. If you call m() in B 
     (NOT A.m() or B.v. Just m()), 
     the one of B will be called
     and not A. Since this is not subjected to
     polymorphism, the call m() in A will never be
     redirected to the one in B.
     **/
    public final static Clazz getInstance() {
        return Holder.getInstance();
    }

    private static class Holder {
        private static final Clazz INSTANCE = new Clazz();

        private static Clazz getInstance() {
            return INSTANCE;
        }
    }
}
```
