package com.nixiedroid.bytes.buffer;

import com.nixiedroid.bytes.interfaces.PrimitiveReader;
import com.nixiedroid.bytes.interfaces.PrimitiveWriter;

@SuppressWarnings("unused")
public interface Buffer extends PrimitiveReader, PrimitiveWriter {

    void reallocate(int size);

    int getPointer();

    void setPointer(int pointer);

    void resetPointer();

    boolean isFull();

    void free();
}
