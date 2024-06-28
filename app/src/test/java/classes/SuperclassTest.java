package classes;

public class SuperclassTest {
    public static void main(String[] args) {
        System.out.println(new Subclass().supSuperA());
        System.out.println(new SupSuperClass(4).getA());
        System.out.println(new SuperClass().getA());
        System.out.println(new Subclass().superA());
        System.out.println(new Subclass().getA());
    }
}

class Subclass extends SuperClass {
    int a = 4;

    int getA(){
        return a;
    }
    int superA(){
        return super.getA();
    }
    int supSuperA(){
        return  ((SupSuperClass)this).getA();
    }

}

class SuperClass extends SupSuperClass {
    private int a = 2;

    public SuperClass() {
        super(2);
        System.out.println(super.hashCode());
        System.out.println(super.equals(this));
        System.out.println(super.toString());
        System.out.println(super.getClass());
        synchronized (this) {
            super.notify();
        }
    }

    @Override
    int getA() {
        return a;
    }
}
class SupSuperClass{
    private int a =1;
    int getA() {
        return this.a;
    }

    public SupSuperClass(int a) {
        System.out.println(this.hashCode());
        System.out.println(equals(this));
        System.out.println(toString());
        System.out.println(this.getClass());
        synchronized (this) {
            this.notify();
        }
    }
}
