package com.nixiedroid;

import com.nixiedroid.classblob.ClassRedaer;

import java.io.*;


public class Main {

    Main() throws IOException {
        new ClassRedaer();

    }

    /**
     * @see jdk.internal.misc.MainMethodFinder;
     */
    @SuppressWarnings("JavadocReference")
    public static void main(String[] args) throws Throwable {
        new Main();
    }

}



