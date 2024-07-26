package com.nixiedroid.classes.assembler.internal;

public interface ByteVector {
    int  getLength();
    byte get(int index);
    void put(int index, byte value);
    void add(byte value);
    void trim();
    byte[] getData();
}
