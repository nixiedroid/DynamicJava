package com.nixiedroid.classes.assembler;


 public final class ByteVectorFactory {
     private ByteVectorFactory() {
     }

     public static ByteVector create() {
        return new ByteVectorImpl();
    }

    static ByteVector create(int sz) {
        return new ByteVectorImpl(sz);
    }
}