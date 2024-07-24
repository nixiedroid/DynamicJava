package com.nixiedroid;


import com.nixiedroid.classblob.ClassReader;
import com.nixiedroid.exceptions.Thrower;

@SuppressWarnings("all")
public final class Main {


    Main() throws Throwable{
       // ClassLoader cl = ClassLoader.getSystemClassLoader();
      //  cl.loadClass("com.nixiedroid.Stuff");
      //  Class.forName("com.nixiedroid.Stuff");
        new ClassReader();
    }


    //com.sun.tools.javac.launcher <executes *.java files. Not *.class
    //java.util.jar jar reader

    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    public static void main(String[] args)  {
        try {
            new Main();
        } catch (Throwable th){
            Thrower.throwException(th);
        }
    }

}



