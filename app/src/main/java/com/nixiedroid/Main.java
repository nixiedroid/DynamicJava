package com.nixiedroid;


import com.nixiedroid.classblob.ClassReader;
import com.nixiedroid.function.Functions;

public class Main {

    Main() throws Throwable {
        new ClassReader();
        new Functions();
    }

    //com.sun.tools.javac.launcher <executes *.java files. Not *.class
    //java.util.jar jar reader

    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    @SuppressWarnings("JavadocReference")
    public static void main(String[] args)  {
        try {
            new Main();
        } catch (Throwable th){
            throw new RuntimeException(th);
        }
    }

}



