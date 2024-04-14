
/*
javac.exe -g -cp . MethodInvocationSamples.java
javap -c -p *.class
java -cp  . MainClazz
 */
interface IFace {
    void interfaceMethod();
    /*
    interface com.nixiedroid.function.IFace {
    public abstract void interfaceMethod();
     */
}

class SuperClass implements IFace {

    SuperClass(int i) {
        super();
//        0: aload_0
//        1: invokespecial #1                  // Method java/lang/Object."<init>":()V
//        4: return
    }

    static void classMethod() {
        //0: return
    }

    void instanceMethod() {
        //0: return
    }

    final void finalInstanceMethod() {
        //0: return
    }

    public void interfaceMethod() {
        //0: return
    }
}

class SubClass extends SuperClass {

    SubClass() {
        this(0);
//        0: aload_0
//        1: iconst_0
//        2: invokespecial #1                  // Method "<init>":(I)V
//        5: return
    }

    SubClass(int i) {
        super(i);
//        0: aload_0
//        1: iload_1
//        2: invokespecial #7                  // Method com/nixiedroid/function/SuperClass."<init>":(I)V
//        5: return
    }

    private void privateMethod() {
        //0: return
    }

    void instanceMethod() {
        //0: return
    }

    final void anotherFinalInstanceMethod() {
        //0: return
    }

    void exampleInstanceMethod() {
        instanceMethod();             // invokevirtual
        super.instanceMethod();       // invokespecial
        privateMethod();              // invokespecial
        finalInstanceMethod();        // invokevirtual
        anotherFinalInstanceMethod(); // invokevirtual
        interfaceMethod();            // invokevirtual
        classMethod();                // invokestatic
//        0: aload_0
//        1: invokevirtual #10                 // Method instanceMethod:()V
//        4: aload_0
//        5: invokespecial #14                 // Method com/nixiedroid/function/SuperClass.instanceMethod:()V
//        8: aload_0
//        9: invokevirtual #15                 // Method privateMethod:()V
//        12: aload_0
//        13: invokevirtual #18                 // Method finalInstanceMethod:()V
//        16: aload_0
//        17: invokevirtual #21                 // Method anotherFinalInstanceMethod:()V
//        20: aload_0
//        21: invokevirtual #24                 // Method interfaceMethod:()V
//        24: invokestatic  #27                 // Method classMethod:()V
//        27: return
    }
}

class MethodInvocationSamples {

    public static void main(String args[]) {
        SubClass sc = new SubClass(); // invokespecial (of an <init>)
        SubClass.classMethod();       // invokestatic
        sc.classMethod();             // invokestatic
        sc.instanceMethod();          // invokevirtual
        sc.finalInstanceMethod();     // invokevirtual
        sc.interfaceMethod();         // invokevirtual
        IFace face = sc;
        face.interfaceMethod();        // invokeinterface
//        0: new           #7                  // class com/nixiedroid/function/SubClass
//        3: dup
//        4: invokespecial #9                  // Method com/nixiedroid/function/SubClass."<init>":()V
//        7: astore_1
//        8: invokestatic  #10                 // Method com/nixiedroid/function/SubClass.classMethod:()V
//        11: aload_1
//        12: pop
//        13: invokestatic  #10                 // Method com/nixiedroid/function/SubClass.classMethod:()V
//        16: aload_1
//        17: invokevirtual #13                 // Method com/nixiedroid/function/SubClass.instanceMethod:()V
//        20: aload_1
//        21: invokevirtual #16                 // Method com/nixiedroid/function/SubClass.finalInstanceMethod:()V
//        24: aload_1
//        25: invokevirtual #19                 // Method com/nixiedroid/function/SubClass.interfaceMethod:()V
//        28: aload_1
//        29: astore_2
//        30: aload_2
//        31: invokeinterface #22,  1           // InterfaceMethod com/nixiedroid/function/IFace.interfaceMethod:()V
//        36: return
    }
}