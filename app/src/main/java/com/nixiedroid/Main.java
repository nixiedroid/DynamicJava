package com.nixiedroid;

import com.nixiedroid.threads.ThreadImpl;
import com.nixiedroid.threads.ThreadMgr;


public class Main {

    Main() throws Throwable {
        //new ClassRedaer();
        //new Functions();
        new ThreadMgr();
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



