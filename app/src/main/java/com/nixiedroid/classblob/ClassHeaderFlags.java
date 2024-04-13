package com.nixiedroid.classblob;

import java.io.IOException;
import java.util.StringJoiner;

/**
 * implementation is similar to:
 * @see java.lang.reflect.Modifier
 */
public class ClassHeaderFlags {
    private final int flags;

    public ClassHeaderFlags(int flags) throws IOException {
        if ((flags & BOOTSTRAP_CLASS) != 0){
            if (flags != (BOOTSTRAP_CLASS|NAME_PROVIDED)) throw new IOException("Invalid Bootstap class signature");
        }
        this.flags = flags;
    }
    public int toInt(){
        return flags;
    }

    public static final int MAIN_CLASS = 0x01;
    public static final int BOOTSTRAP_CLASS = 0x02;
    public static final int ENCRYPTED = 0x04;
    public static final int DECRYPTER = 0x08;
    public static final int CLASS_NAME_RETRIEVER =0x10;
    public static final int NAME_PROVIDED = 0x20;

    public boolean isFlagSet(int flag){
        return ((flags & flag) != 0);
    }

    public String toString() {
        StringJoiner sj = new StringJoiner(" ");
        if ((flags & MAIN_CLASS) != 0)        sj.add("main");
        if ((flags & BOOTSTRAP_CLASS) != 0)        sj.add("bootstrap");
        if ((flags & DECRYPTER) != 0)        sj.add("decrypter");
        if ((flags & ENCRYPTED) != 0)        sj.add("encrypted");
        if ((flags & CLASS_NAME_RETRIEVER) != 0)        sj.add("namesRetriver");
        if ((flags & NAME_PROVIDED) != 0)        sj.add("named");
        return sj.toString();
    }
}
