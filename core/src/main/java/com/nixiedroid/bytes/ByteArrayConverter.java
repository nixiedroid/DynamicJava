package com.nixiedroid.bytes;

public interface ByteArrayConverter {
    int FF = 0xFF;
    int BYTE =1;
    int SHORT =2;
    int INTEGER =4;
    int LONG =8;

    byte toInt8(byte[] b, int start);

    short toInt16B(byte[] b, int start);
    short toInt16L(byte[] b, int start);

    int toInt32B(byte[] b, int start);
    int toInt32L(byte[] b, int start);

    long toInt64B(byte[] b, int start);
    long toInt64L(byte[] b, int start);

    void int8ToBytes(byte[] b, int start, byte by);

    void int16ToBytesB(byte[] b, int start, short s);
    void int16ToBytesL(byte[] b, int start, short s);

    void int32ToBytesB(byte[] b, int start, int i);
    void int32ToBytesL(byte[] b, int start, int i);

    void int64ToBytesB(byte[] b, int start, long l);
    void int64ToBytesL(byte[] b, int start, long l);
}
