package com.nixiedroid.classblob;

import java.util.StringJoiner;

/**
 * implementation is similar to:
 * @see java.lang.reflect.Modifier
 */
public class ClassHeaderFlags {
    private final int flags;

    public ClassHeaderFlags(int flags) {
        //TODO Add verification
        this.flags = flags;
    }
    public int toInt(){
        return flags;
    }

    public static final int MAIN_CLASS = 0x01;

    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        if ((flags & MAIN_CLASS) != 0)        sj.add("main");
//        if ((mod & PROTECTED) != 0)     sj.add("protected");
//        if ((mod & PRIVATE) != 0)       sj.add("private");
//
//        /* Canonical order */
//        if ((mod & ABSTRACT) != 0)      sj.add("abstract");
//        if ((mod & STATIC) != 0)        sj.add("static");
//        if ((mod & FINAL) != 0)         sj.add("final");
//        if ((mod & TRANSIENT) != 0)     sj.add("transient");
//        if ((mod & VOLATILE) != 0)      sj.add("volatile");
//        if ((mod & SYNCHRONIZED) != 0)  sj.add("synchronized");
//        if ((mod & NATIVE) != 0)        sj.add("native");
//        if ((mod & STRICT) != 0)        sj.add("strictfp");
//        if ((mod & INTERFACE) != 0)     sj.add("interface");

        return sj.toString();
    }
}
