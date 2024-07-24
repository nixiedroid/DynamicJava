package com.nixiedroid.bytes.buffer;

import com.nixiedroid.bytes.converter.Endianness;

public interface Buffer {

    void reallocate(int size);

    void setEndiannes(Endianness e);

    void setPointer(int pointer);
    int getPointer();
    void resetPointer();

    boolean isFull();

    void free();

    byte readByte(Endianness e);

    short readShort(Endianness e);

    int readInteger(Endianness e);

    long readLong(Endianness e);

    float readFloat(Endianness e);

    double readDouble(Endianness e);

    void writeByte(byte by, Endianness e);

    void writeShort(short s, Endianness e);

    void writeInteger(int i, Endianness e);

    void writeLong(long l, Endianness e);

    void writeFloat(float f, Endianness e);

    void writeDouble(double d, Endianness e);


    byte readByte();

    short readShort();

    int readInteger();

    long readLong();

    float readFloat();

    double readDouble();


    void writeByte(byte by);

    void writeShort(short s);

    void writeInteger(int i);

    void writeLong(long l);

    void writeFloat(float f);

    void writeDouble(double d);
}
