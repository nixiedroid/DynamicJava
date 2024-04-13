package samples.interfaces;

public interface Walking {
    //protected void walk(); -> not allowed
    //public void walk();    -> redundant
    //void walk();           -> OK
    //private void walk();   -> requires body

    //protected static void walk(); -> static requires body, protected is not allowed
    //public static void walk();    -> static requires body, public is redundant
    //static void walk();           -> static requires body
    //private static void walk();   -> requires body

    //protected void walk(){}; -> not allowed
    //public void walk(){};    -> Interface abstract methods cannot have body
    //void walk(){};           -> Interface abstract methods cannot have body
    //private void walk(){};   -> OK

    //protected static void walk(){}; -> protected is not allowed
    //public static void walk(){};    -> public is redundant
    //static void walk(){};           -> OK. Accessible only via Walking.walk();
    //private static void walk(){};   -> OK.

    //protected int a = 3; -> not allowed
    //public int a = 3;    -> redundant
    //int a = 3;           -> OK
    //private int a = 3;   -> not allowed

    //protected static int a = 3; -> static is redundant, protected is not allowed
    //public static int a = 3;    -> static is redundant, public is redundant
    //static int a = 3;           -> static is redundant
    //private static int a = 3;   -> static is redundant, private is not allowed

    //class ***         -> OK
    //* class *** -> Illegal combination of modifiers: '*' and 'public'

}
