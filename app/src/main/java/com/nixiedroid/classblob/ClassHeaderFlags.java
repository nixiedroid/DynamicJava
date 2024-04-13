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
    public static final int BOOTSTRAP_CLASS = 0x02;

    public String toString() {
        StringJoiner sj = new StringJoiner(" ");

        if ((flags & MAIN_CLASS) != 0)        sj.add("main");
        if ((flags & BOOTSTRAP_CLASS) != 0)        sj.add("bootstrap");

        return sj.toString();
    }
}
