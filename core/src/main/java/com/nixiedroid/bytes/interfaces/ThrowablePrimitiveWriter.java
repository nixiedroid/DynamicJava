package com.nixiedroid.bytes.interfaces;

import com.nixiedroid.bytes.converter.Endianness;

@SuppressWarnings("unused")
public interface ThrowablePrimitiveWriter<E extends Throwable> extends EndiannesConsumer {

    void writeByte(byte by, Endianness e) throws E;

    void writeShort(short s, Endianness e) throws E;

    void writeInteger(int i, Endianness e) throws E;

    void writeLong(long l, Endianness e) throws E;

    void writeFloat(float f, Endianness e) throws E;

    void writeDouble(double d, Endianness e) throws E;

    void writeByte(byte by) throws E;

    void writeShort(short s) throws E;

    void writeInteger(int i) throws E;

    void writeLong(long l) throws E;

    void writeFloat(float f) throws E;

    void writeDouble(double d) throws E;
}
