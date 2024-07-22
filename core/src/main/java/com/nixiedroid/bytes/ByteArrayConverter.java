package com.nixiedroid.bytes;

public interface ByteArrayConverter {
    int FF = 0xFF;
    int BYTE = 1;
    int SHORT = 2;
    int INTEGER = 4;
    int LONG = 8;

    byte toByte(byte[] b, int start, Endiannes e);

    short toShort(byte[] b, int start, Endiannes e);

    int toInteger(byte[] b, int start, Endiannes e);

    long toLong(byte[] b, int start, Endiannes e);

    float toFloat(byte[] b, int start, Endiannes e);

    double toDouble(byte[] b, int start, Endiannes e);


    void fromByte(byte[] b, int start, byte by, Endiannes e);

    void fromShort(byte[] b, int start, short s, Endiannes e);

    void fromInteger(byte[] b, int start, int i, Endiannes e);

    void fromLong(byte[] b, int start, long l, Endiannes e);

    void fromFloat(byte[] b, int start, float f, Endiannes e);

    void fromDouble(byte[] b, int start, double d, Endiannes e);
}
