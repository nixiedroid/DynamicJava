package com.nixiedroid.magic.parts;

public abstract class UnsafeGetter {
    protected sun.misc.Unsafe unsafe;

    public sun.misc.Unsafe get(){
        return unsafe;
    }
    public static class ForJava7 extends UnsafeGetter {
        public ForJava7() {
           unsafe = com.nixiedroid.unsafe.Unsafe.getUnsafe();
        }
    }
}
