package com.nixiedroid.bytes.interfaces;

import com.nixiedroid.bytes.converter.Endianness;

@SuppressWarnings("unused")
public interface PrimitiveWriter extends EndiannesConsumer {

    void writeByte(byte by, Endianness e);

    void writeShort(short s, Endianness e);

    void writeInteger(int i, Endianness e);

    void writeLong(long l, Endianness e);

    void writeFloat(float f, Endianness e);

    void writeDouble(double d, Endianness e);

    void writeByte(byte by);

    void writeShort(short s);

    void writeInteger(int i);

    void writeLong(long l);

    void writeFloat(float f);

    void writeDouble(double d);
}
