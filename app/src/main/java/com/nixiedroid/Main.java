package com.nixiedroid;


@SuppressWarnings("UtilityClassWithoutPrivateConstructor")
public final class Main {

    @SuppressWarnings("RedundantThrows")
    Main() throws Throwable{

    }


    //com.sun.tools.javac.launcher <executes *.java files. Not *.class
    //java.util.jar jar reader

    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    @SuppressWarnings({"JavadocReference", "InstantiationOfUtilityClass"})
    public static void main(String[] args)  {
        try {
            new Main();
        } catch (Throwable th){
            throw new RuntimeException(th);
        }
    }

}



