package com.nixiedroid.bytes;

public interface ByteArrayConverter {
    int FF = 0xFF;
    int BYTE = 1;
    int SHORT = 2;
    int INTEGER = 4;
    int LONG = 8;

    byte readByte(byte[] b, int start, Endiannes e);

    short readShort(byte[] b, int start, Endiannes e);

    int readInteger(byte[] b, int start, Endiannes e);

    long readLong(byte[] b, int start, Endiannes e);

    float readFloat(byte[] b, int start, Endiannes e);

    double readDouble(byte[] b, int start, Endiannes e);


    void writeByte(byte[] b, int start, byte by, Endiannes e);

    void writeShort(byte[] b, int start, short s, Endiannes e);

    void writeInteger(byte[] b, int start, int i, Endiannes e);

    void writeLong(byte[] b, int start, long l, Endiannes e);

    void writeFloat(byte[] b, int start, float f, Endiannes e);

    void writeDouble(byte[] b, int start, double d, Endiannes e);
}
