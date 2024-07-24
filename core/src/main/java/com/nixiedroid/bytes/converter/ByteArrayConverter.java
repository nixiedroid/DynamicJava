package com.nixiedroid.bytes.converter;

public interface ByteArrayConverter {
    int FF = 0xFF;
    int BYTE = 1;
    int SHORT = 2;
    int INTEGER = 4;
    int LONG = 8;

    byte readByte(byte[] b, int start, Endianness e);

    short readShort(byte[] b, int start, Endianness e);

    int readInteger(byte[] b, int start, Endianness e);

    long readLong(byte[] b, int start, Endianness e);

    float readFloat(byte[] b, int start, Endianness e);

    double readDouble(byte[] b, int start, Endianness e);


    void writeByte(byte[] b, int start, byte by, Endianness e);

    void writeShort(byte[] b, int start, short s, Endianness e);

    void writeInteger(byte[] b, int start, int i, Endianness e);

    void writeLong(byte[] b, int start, long l, Endianness e);

    void writeFloat(byte[] b, int start, float f, Endianness e);

    void writeDouble(byte[] b, int start, double d, Endianness e);
}
